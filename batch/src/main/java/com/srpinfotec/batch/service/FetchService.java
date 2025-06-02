package com.srpinfotec.batch.service;

import com.srpinfotec.batch.BatchConfig;
import com.srpinfotec.batch.slack.SlackMessage;
import com.srpinfotec.batch.web.response.FetchRsDto;
import com.srpinfotec.batch.exception.BatchException;
import com.srpinfotec.core.entity.Commit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchService {
    private final JobExplorer jobExplorer;
    private final BatchConfig batchConfig;
    private final EntityManager entityManager;

    public FetchRsDto manualFetch(){
        // 중복 실행 방지
        List<JobInstance> instances = jobExplorer.getJobInstances("FetchCvsLogJob", 0, 10);

        instances.forEach(jobInstance -> {
            jobExplorer.getJobExecutions(jobInstance).forEach(jobExecution -> {
                if(jobExecution.isRunning()) {
                    throw new BatchException("이미 fetch 실행중.");
                }
            });
        });

        try {
            JobExecution jobExecution = batchConfig.runDailyFetchCvsLog();

            return fetchJobExecutionToDto(jobExecution);

        } catch (Exception e) {
            throw new BatchException("job 실행 에러");
        }
    }

    public void fetchAll() {
        try {

            batchConfig.runFetchCvsLogWithoutCommitMsg();

        } catch (Exception e) {
            throw new BatchException("job 실행 에러");
        }
    }

    public FetchRsDto fetchRecentMonth(int month) {
        try {
            JobExecution jobExecution = batchConfig.runRecentMonthFetchCvsLog(month);
            return fetchJobExecutionToDto(jobExecution);
        } catch (Exception e) {
            throw new BatchException("job 실행 에러");
        }
    }


    /**
     * JobExecution을 FetchDto로 변환
     */
    public FetchRsDto fetchJobExecutionToDto(JobExecution jobExecution){
        if(jobExecution == null){
            return null;
        }

        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();

        long writeCount = 0L;

        for (StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName().equals("RevisionFileToDBStep"))
                writeCount = stepExecution.getWriteCount();
        }

        return new FetchRsDto(
                jobExecution.getStatus().toString(),
                writeCount,
                jobExecution.getLastUpdated()
        );
    }

    /**
     * 최근 Fetch 결과 반환
     */
    public FetchRsDto getRecentFetchResult(){
        JobInstance jobInstance = jobExplorer.getLastJobInstance("FetchCvsLogJob");

        if(jobInstance == null)
            return FetchRsDto.nonExecutedJob();

        JobExecution jobExecution = jobExplorer.getLastJobExecution(jobInstance);

        return fetchJobExecutionToDto(jobExecution);
    }

    @Transactional
    public SlackMessage getRecentCommitSlackMessage(Long count) {
        Commit commit = entityManager.createQuery("select c from Commit c order by c.id desc", Commit.class)
                .setMaxResults(1)
                .getSingleResult();

        if (commit == null) {
            throw new BatchException("최근 커밋이 없습니다.");
        }

        LocalDateTime commitTimeInSeoul = commit.getCommitTime()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();

        return SlackMessage.createCommitAlertMessage(
                commit.getUser().getName(),
                commit.getCommitMsg(),
                commit.getProject().getName(),
                count,
                commitTimeInSeoul
        );
    }
}
