package com.srpinfotec.cvslog.common.command;

import java.io.IOException;

public interface CommandExecutor {
    void execute(String command) throws IOException, InterruptedException;
}
