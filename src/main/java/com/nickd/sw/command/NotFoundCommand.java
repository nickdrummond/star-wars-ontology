package com.nickd.sw.command;

public class NotFoundCommand implements Command {
    @Override
    public Context handle(String commandStr, Context context) {
        System.err.println("Command not found: " + commandStr);
        return context;
    }
}
