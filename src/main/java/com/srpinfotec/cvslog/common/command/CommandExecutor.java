package com.srpinfotec.cvslog.common.command;

import java.io.IOException;
import java.util.List;

public interface CommandExecutor {
    void execute(String command) throws IOException, InterruptedException;

    List<String> executeWithOutput(String command) throws IOException, InterruptedException;
}
