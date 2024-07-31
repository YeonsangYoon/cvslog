package com.srpinfotec.cvslog.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShellCommandException.class)
    public void handleShellCommandError(ShellCommandException e){

    }
}
