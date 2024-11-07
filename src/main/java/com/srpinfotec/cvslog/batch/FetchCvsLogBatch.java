package com.srpinfotec.cvslog.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/*
Fetch 신규 Commit Log
Job 순서
1. Bash Command 실행 : BashCommandTasklet
    - Tasklet으로 Bask 커맨드 실행
2. Log 파일 읽고 Log Entity에 저장 : RevisionLogToEntityConfig
    - ItemReader
    - ItemProcessor
    - ItemWriter
 */

@Configuration
public class FetchCvsLogBatch {

    /**
     * commit message까지 추출하는 fetch
     */
    @Bean
    public Job fetchCvsLogJob(JobRepository jr, PlatformTransactionManager ptm,
                              Step fetchLogCommandStep,
                              Step revisionFileToDBStep,
                              Step deleteFetchLogFileStep
                              ){

        return new JobBuilder("FetchCvsLogJob", jr)
                .incrementer(new RunIdIncrementer())
                .start(fetchLogCommandStep)
                .next(revisionFileToDBStep)
                .next(deleteFetchLogFileStep)
                .build();
    }


    /**
     * commit message는 추출하지 않는 fetch
     */
    @Bean
    public Job fetchCvsLogJobWithoutCommitMsg(JobRepository jr, PlatformTransactionManager ptm,
                                              Step fetchLogCommandStep,
                                              Step revisionFileToDBStepWithoutCommitMsg,
                                              Step deleteFetchLogFileStep
    ){

        return new JobBuilder("FetchCvsLogJobWithoutCommitMsg", jr)
                .incrementer(new RunIdIncrementer())
                .start(fetchLogCommandStep)
                .next(revisionFileToDBStepWithoutCommitMsg)
                .next(deleteFetchLogFileStep)
                .build();
    }

}
