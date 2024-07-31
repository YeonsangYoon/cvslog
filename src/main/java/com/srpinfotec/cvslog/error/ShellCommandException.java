package com.srpinfotec.cvslog.error;

public class ShellCommandException extends CustomException{
    private String executeCommand;

    public ShellCommandException() {
        super();
    }

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
