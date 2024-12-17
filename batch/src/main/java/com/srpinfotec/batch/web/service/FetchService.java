package com.srpinfotec.batch.web.service;

import com.srpinfotec.batch.BatchConfig;
import com.srpinfotec.batch.web.dto.response.FetchRsDto;
import com.srpinfotec.batch.exception.BatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
