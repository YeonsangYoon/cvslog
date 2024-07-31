package com.srpinfotec.cvslog.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShellCommandException.class)
    public void handleShellCommandError(ShellCommandException e){
        log.error("Shell Command Error [{}] : {}", e.getCommand(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public void allException(RuntimeException e){
        log.error(e.getMessage());
    }
}
