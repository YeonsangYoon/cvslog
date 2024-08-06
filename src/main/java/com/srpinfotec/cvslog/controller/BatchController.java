package com.srpinfotec.cvslog.controller;

import com.srpinfotec.cvslog.dto.ResponseDto;
import com.srpinfotec.cvslog.dto.response.FetchRsDto;
import com.srpinfotec.cvslog.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BatchController {
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;  // 프록시 객체로 DI 된다.
    private final Map<String, Job> jobs;


    @PostMapping("/fetch")
    public ResponseEntity<ResponseDto> fetchDailyCvsLog(){
        try {
            JobExecution jobExecution = jobLauncher.run(jobs.get("FetchCvsLogJob"), getFetchJobParams());

            return ResponseEntity
                    .ok(ResponseDto
                            .success(fetchJobExecutionToDto(jobExecution)));

        } catch (JobExecutionException e) {
            throw new CustomException("job 실행 에러");
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseDto> lastUpdateFetch(){
        JobInstance jobInstance = jobExplorer.getLastJobInstance("FetchCvsLogJob");

        if(jobInstance == null)
            return ResponseEntity
                    .ok(ResponseDto.success(FetchRsDto.nonExecutedJob()));

        JobExecution jobExecution = jobExplorer.getLastJobExecution(jobInstance);

        return ResponseEntity
                .ok(ResponseDto.success(fetchJobExecutionToDto(jobExecution)));
    }

    private JobParameters getFetchJobParams(){
        return new JobParametersBuilder()
                .addLocalDateTime("FetchCvsLogJob", LocalDateTime.now())
                .addLocalDate("basedate", LocalDate.now())
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
}
