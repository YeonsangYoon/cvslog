package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.config.BatchConfig;
import com.srpinfotec.cvslog.dto.request.FetchRqCond;
import com.srpinfotec.cvslog.dto.response.FetchRsDto;
import com.srpinfotec.cvslog.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FetchService {
    private final JobExplorer jobExplorer;
    private final BatchConfig batchConfig;

    public FetchRsDto fetch(){
        try {
            JobExecution jobExecution = batchConfig.runDailyFetchCvsLog();

            return fetchJobExecutionToDto(jobExecution);

        } catch (Exception e) {
            throw new CustomException("job 실행 에러");
        }
    }

    public void fetchAll() {
        try {

            batchConfig.runFetchCvsLogWithoutCommitMsg();

        } catch (Exception e) {
            throw new CustomException("job 실행 에러");
        }
    }

    public void fetchCommitMessage(){
        try{
            batchConfig.runFetchCommitMessage();
        } catch (Exception e){
            throw new CustomException("job 실행 에러");
        }
    }


    /**
     * JobExecution을 FetchDto로 변환
     */
    private FetchRsDto fetchJobExecutionToDto(JobExecution jobExecution){
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
}
