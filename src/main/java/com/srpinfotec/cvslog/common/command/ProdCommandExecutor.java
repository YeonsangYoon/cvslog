package com.srpinfotec.cvslog.common.command;

import com.srpinfotec.cvslog.common.OsType;
import com.srpinfotec.cvslog.error.ShellCommandException;
import com.srpinfotec.cvslog.util.SystemUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 운영 커멘드 실행기
 */
@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class ProdCommandExecutor implements CommandExecutor {
    private static final Long COMMAND_TIMEOUT_MINUTE = 10L;

    @Override
    public void execute(String command) throws IOException, InterruptedException {
        log.info("Execute Bash Command : {}", command);

        ProcessBuilder processBuilder = new ProcessBuilder()
                .redirectErrorStream(true);

        if(SystemUtil.currentOs() == OsType.LINUX){
            processBuilder.command("/bin/bash", "-c", command);
        } else {
            processBuilder.command("cmd.exe", "/c", command);
        }

        Process process = processBuilder.start();

        process.getErrorStream().close();
        process.getInputStream().close();
        process.getOutputStream().close();

        boolean finished = process.waitFor(COMMAND_TIMEOUT_MINUTE, TimeUnit.MINUTES);
        if (!finished ) {
            process.destroy();
            throw new ShellCommandException("Bash command failed");
        }
    }

    @Override
    public List<String> executeWithOutput(String command) throws IOException, InterruptedException {
        log.info("Execute Bash Command : {}", command);

        List<String> logs = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder()
                .redirectErrorStream(true);

        if(SystemUtil.currentOs() == OsType.LINUX){
            processBuilder.command("/bin/bash", "-c", command);
        } else {
            processBuilder.command("cmd.exe", "/c", command);
        }

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        }

        boolean finished = process.waitFor(COMMAND_TIMEOUT_MINUTE, TimeUnit.MINUTES);
        if (!finished ) {
            process.destroy();
            throw new ShellCommandException("Bash command failed");
        }

        return logs;
    }
}
