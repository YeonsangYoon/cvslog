package com.srpinfotec.cvslog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobLauncher jobLauncher;

    private final Job fetchCvsLogJob;
    private final Job fetchCvsLogJobWithoutCommitMsg;

    @Bean
    public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
        return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(
            JobLauncher jobLauncher,
            JobExplorer jobExplorer,
            JobRepository jobRepository,
            BatchProperties properties
    ) {
        JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
        String jobNames = properties.getJob().getName();
        if (StringUtils.hasText(jobNames)) {
            runner.setJobName(jobNames);
        }
        return runner;
    }

    public JobExecution runDailyFetchCvsLog() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("FetchCvsLogJob", LocalDateTime.now())
                .addLocalDate("basedate", LocalDate.now())
                .addLong("chunkSize", 100L)
                .toJobParameters();

        return jobLauncher.run(fetchCvsLogJob, jobParameters);
    }

    @Async
    public void runFetchCvsLogWithoutCommitMsg() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("FetchCvsLogJobWithoutCommitMsg", LocalDateTime.now())
                .addLong("chunkSize", 1000L)
                .toJobParameters();

        jobLauncher.run(fetchCvsLogJobWithoutCommitMsg, jobParameters);
    }

    /********************************************************************************
     * Batch Job Scheduler
     ********************************************************************************/

    @Scheduled(cron = "0 0/10 * * * ?")
    public void dailyCvsLogScheduler() throws Exception{
        runDailyFetchCvsLog();
    }

}
