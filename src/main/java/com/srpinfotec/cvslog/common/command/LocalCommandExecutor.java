package com.srpinfotec.cvslog.common.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로컬 테스트용 커멘드 실행기
 */
@Slf4j
@Profile("local")
@Component
public class LocalCommandExecutor implements CommandExecutor {
    @Override
    public void execute(String command) throws IOException, InterruptedException {
        log.info("Execute Bash Command : {}", command);
    }
}
