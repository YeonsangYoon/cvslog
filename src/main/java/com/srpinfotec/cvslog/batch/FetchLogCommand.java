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
                        .append(cvsProperties.getLogFilePath());

                commandExecutor.execute(command.toString());

                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    @JobScope
    public Step updateCvsWorkDirectoryStep(JobRepository jr,
                                           PlatformTransactionManager ptm,
                                           Tasklet updateCvsWorkDirectoryTasklet) {
        return new StepBuilder("UpdateCvsWorkDirectoryStep", jr)
                .tasklet(updateCvsWorkDirectoryTasklet, ptm)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet updateCvsWorkDirectoryTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                String command = cvsProperties.getScriptDir() + "/update.sh";

                commandExecutor.execute(command);

                return RepeatStatus.FINISHED;
            }
        };
    }
}
