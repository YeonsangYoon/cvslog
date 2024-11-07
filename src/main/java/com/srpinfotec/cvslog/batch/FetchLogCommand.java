package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.common.CVSProperties;
import com.srpinfotec.cvslog.common.command.CommandExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FetchLogCommand {
    private final CommandExecutor commandExecutor;
    private final CVSProperties cvsProperties;

    /**
     * CVS log Fetch 결과를 cvs_fetch_{jobExecutionId}.log로 기록
     */
    @Bean
    @JobScope
    public Step fetchLogCommandStep(JobRepository jr,
                                    PlatformTransactionManager ptm,
                                    Tasklet fetchLogCommandTasklet){
        return new StepBuilder("FetchLogCommandStep", jr)
                .tasklet(fetchLogCommandTasklet, ptm)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet fetchLogCommandTasklet(@Value("#{jobParameters['basedate']}") LocalDate baseDate){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();

                StringBuilder command = new StringBuilder()
                        .append("cvs -d ")
                        .append(cvsProperties.getRoot())
                        .append(" history -a -x AMR");

                if(baseDate != null){
                    command
                            .append(" -D ")
                            .append(baseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }

                command
                        .append(" > ")
                        .append(cvsProperties.getLogFilePath(jobExecutionId));

                commandExecutor.execute(command.toString());

                return RepeatStatus.FINISHED;
            }
        };
    }

    /**
     * Fetch 결과 log file 삭제 Step
     *  - Tasklet 기반
     */
    @Bean
    @JobScope
    public Step deleteFetchLogFileStep(JobRepository jr,
                                       PlatformTransactionManager ptm,
                                       Tasklet deleteFetchLogFIleTasklet) {
        return new StepBuilder("DeleteFetchLogFileStep", jr)
                .tasklet(deleteFetchLogFIleTasklet, ptm)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet deleteFetchLogFIleTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();

                String rmCommand = "rm -f " + cvsProperties.getLogFilePath(jobExecutionId);

                commandExecutor.execute(rmCommand);

                return RepeatStatus.FINISHED;
            }
        };
    }
}
