package com.nickd.sw.command;

public interface Command {

    Context handle(String commandStr, Context context);
}
