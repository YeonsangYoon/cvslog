package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.dto.request.FetchRqCond;
import com.srpinfotec.cvslog.dto.response.FetchRsDto;
import com.srpinfotec.cvslog.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FetchService {
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;
    private final Map<String, Job> jobs;

    public FetchRsDto fetch(FetchRqCond cond){
        try {
            JobExecution jobExecution = jobLauncher.run(jobs.get("FetchCvsLogJob"), getFetchJobParams(cond.getBaseDate()));

            return fetchJobExecutionToDto(jobExecution);

        } catch (JobExecutionException e) {
            throw new CustomException("job 실행 에러");
        }
    }


    private JobParameters getFetchJobParams(@Nullable LocalDate baseDate){
        return new JobParametersBuilder()
                .addLocalDateTime("FetchCvsLogJob", LocalDateTime.now())
                .addLocalDate("basedate", baseDate == null ? LocalDate.now() : baseDate)
                .addLong("chuckSize", 100L)
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
}
