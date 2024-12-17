package com.srpinfotec.batch.exception;

public class ShellCommandException extends BatchException{
    private String executeCommand;

    public ShellCommandException(String message) {
        super(message);
    }

    public ShellCommandException(String message, String executeCommand){
        super(message);
        this.executeCommand = executeCommand;
    }

    public String getCommand(){
        return this.executeCommand;
    }
}
