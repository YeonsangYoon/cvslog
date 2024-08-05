package com.srpinfotec.cvslog.common.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<String> executeWithOutput(String command) {
        log.info("Execute Bash Command : {}", command);
        return new ArrayList<>();
    }
}
