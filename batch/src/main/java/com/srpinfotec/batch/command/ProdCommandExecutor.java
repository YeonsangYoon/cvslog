package com.srpinfotec.batch.command;

import com.srpinfotec.batch.exception.ShellCommandException;
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
        log.debug("Execute Bash Command : {}", command);

        Process process = createProcess(command);

        try {
            boolean finished = process.waitFor(COMMAND_TIMEOUT_MINUTE, TimeUnit.MINUTES);

            if (!finished) {
                process.destroyForcibly();
                throw new ShellCommandException("Command timeout after " + COMMAND_TIMEOUT_MINUTE + " minutes");
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new ShellCommandException("Command failed with exit code: " + exitCode);
            }
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    @Override
    public List<String> executeWithOutput(String command) throws IOException, InterruptedException {
        log.debug("Execute Bash Command : {}", command);

        List<String> logs = new ArrayList<>();
        Process process = createProcess(command);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
                log.trace("Command output: {}", line);
            }

            boolean finished = process.waitFor(COMMAND_TIMEOUT_MINUTE, TimeUnit.MINUTES);

            if (!finished) {
                process.destroyForcibly();
                throw new ShellCommandException("Command timeout after " + COMMAND_TIMEOUT_MINUTE + " minutes");
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new ShellCommandException("Command failed with exit code: " + exitCode + ", output: " + logs);
            }

        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }

        return logs;
    }

    /**
     * 프로세스 생성 공통 메서드
     */
    private Process createProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder()
                .redirectErrorStream(true);

        if (currentOs() == OsType.LINUX) {
            processBuilder.command("/bin/bash", "-c", command);
        } else {
            processBuilder.command("cmd.exe", "/c", command);
        }

        return processBuilder.start();
    }

    private OsType currentOs() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return OsType.WINDOW;
        } else {
            return OsType.LINUX;
        }
    }
}
