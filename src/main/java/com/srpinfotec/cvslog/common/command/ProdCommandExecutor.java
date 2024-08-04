package com.srpinfotec.cvslog.common.command;

import com.srpinfotec.cvslog.common.OsType;
import com.srpinfotec.cvslog.error.ShellCommandException;
import com.srpinfotec.cvslog.util.SystemUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 운영 커멘드 실행기
 */
@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class ProdCommandExecutor implements CommandExecutor {

    @Override
    public void execute(String command) throws IOException, InterruptedException {
        log.info("Execute Bash Command : {}", command);

        ProcessBuilder processBuilder = new ProcessBuilder();

        if(SystemUtil.currentOs() == OsType.LINUX){
            processBuilder.command("bash", "-c", command);
        } else {
            processBuilder.command("cmd.exe", "/c", command);
        }

        Process process = processBuilder.start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new ShellCommandException("Bash command failed with exit code: " + exitCode);
        }
    }
}
