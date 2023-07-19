package com.nickd.sw.command;

import java.util.Map;

public class CommandController {

    private final Map<String, Command> commands;

    public CommandController(Map<String, Command> commands) {
        this.commands = commands;
    }

    public Command getCommand(String s, Context currentContext) {
        String firstWord = s.split(" ")[0];
        return commands.getOrDefault(firstWord, new NotFoundCommand());
    }
}
