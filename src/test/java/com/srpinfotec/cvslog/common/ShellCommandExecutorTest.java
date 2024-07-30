package com.srpinfotec.cvslog.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShellCommandExecutorTest {
    @Autowired ShellCommand shellCommand;

    @Test
    public void 커맨드실행(){
        ShellCommandExecutor.execute("type .\\src\\main\\resources\\commitLog.txt");
    }


    @Test
    public void 미리정의된커맨드실행(){
        ShellCommandExecutor.execute(shellCommand.getRecentHistory());
    }
}