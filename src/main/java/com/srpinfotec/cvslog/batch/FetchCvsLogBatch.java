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

    @Bean
    public Job FetchCvsLogJob(JobRepository jr, PlatformTransactionManager ptm,
                              Step fetchLogCommandStep,
                              Step revisionFileToDBStep
                              ){

        return new JobBuilder("FetchCvsLogJob", jr)
                .incrementer(new RunIdIncrementer())
                .start(fetchLogCommandStep)
                .next(revisionFileToDBStep)
                .build();
    }



}
