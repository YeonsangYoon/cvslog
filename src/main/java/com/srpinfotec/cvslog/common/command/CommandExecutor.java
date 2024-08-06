package com.srpinfotec.cvslog.common.command;

import java.io.IOException;
import java.util.List;

public interface CommandExecutor {
    /**
     * 표준 입출력이 필요 없는 경우
     * @param command 실행할 명령어
     */
    void execute(String command) throws IOException, InterruptedException;

    List<String> executeWithOutput(String command) throws IOException, InterruptedException;
}
