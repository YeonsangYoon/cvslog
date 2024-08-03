package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.error.ShellCommandException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class BashCommandTasklet implements Tasklet {
    private final String logFilePath = "/home/cvslogProject/cvslog/logs/cvslog.log";
    private final ProcessBuilder processBuilder;

    public BashCommandTasklet(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String command = chunkContext.getStepContext().getJobParameters().get("command").toString() + " > " + logFilePath;

        processBuilder.command("bash", "-c", command);

        Process process = processBuilder.start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new ShellCommandException("Bash command failed with exit code: " + exitCode);
        }

        return RepeatStatus.FINISHED;
    }
}
