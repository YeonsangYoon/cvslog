package com.srpinfotec.cvslog.service;

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
    private final JobLauncher jobLauncher;
    private final Map<String, Job> jobs;

    public FetchRsDto fetch(FetchRqCond cond){
        try {
            JobExecution jobExecution
                    = jobLauncher.run(jobs.get("FetchCvsLogJob"), getFetchJobParams(cond));

            return fetchJobExecutionToDto(jobExecution);

        } catch (JobExecutionException e) {
            throw new CustomException("job 실행 에러");
        }
    }


    private JobParameters getFetchJobParams(FetchRqCond cond){
        return new JobParametersBuilder()
                .addLocalDateTime("FetchCvsLogJob", LocalDateTime.now())
                .addLocalDate("basedate", cond.getBaseDate())
                .addLong("chuckSize", cond.getChuckSize())
                .toJobParameters();
    }

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

    public FetchRsDto getRecentFetchResult(){
        JobInstance jobInstance = jobExplorer.getLastJobInstance("FetchCvsLogJob");

        if(jobInstance == null)
            return FetchRsDto.nonExecutedJob();

        JobExecution jobExecution = jobExplorer.getLastJobExecution(jobInstance);

        return fetchJobExecutionToDto(jobExecution);
    }

    public FetchRsDto fetchAll() {
        try {
            JobExecution jobExecution = jobLauncher.run(jobs.get("FetchCvsLogJob"),
                    new JobParametersBuilder()
                            .addLocalDateTime("FetchCvsLogJob", LocalDateTime.now())
                            .addLong("chuckSize", 300L)
                            .toJobParameters());

            return fetchJobExecutionToDto(jobExecution);

        } catch (JobExecutionException e) {
            throw new CustomException("job 실행 에러");
        }
    }
}
