package com.srpinfotec.cvslog.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
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
3. Log Entity에서 User, Project, Time을 기준으로 Grouping해서 Commit 엔티티 생성 : GroupingLogConfig
    - ItemReader
    - ItemProcessor
    - ItemWriter
4. Log Entity를 Revision Entity로 이관
    - ItemReader
    - ItemProcessor
    - ItemWriter
5. Log Entity 전체 삭제
 */

@Configuration
public class FetchCvsLogBatch {

    @Bean
    public ProcessBuilder processBuilder(){
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        return builder;
    }

    @Bean
    public Job FetchCvsLogJob(JobRepository jr, PlatformTransactionManager ptm,
                              Tasklet bashCommandTasklet,
                              Step revisionFileToDBStep
                              ){
        return new JobBuilder("FetchCvsLogJob", jr)
                .incrementer(new RunIdIncrementer())
                .start(revisionFileToDBStep)
                .build();
    }

}
