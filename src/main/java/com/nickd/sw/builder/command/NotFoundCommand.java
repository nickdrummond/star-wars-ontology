package com.nickd.sw.builder.command;

import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotFoundCommand implements Command {

    private Logger logger = LoggerFactory.getLogger(NotFoundCommand.class);

    private final Map<String, Command> commands;

    public NotFoundCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public ContextBase handle(UserInput input, ContextBase context) {
        logger.warn("Command not found: " + input);
        return context;
    }

    @Override
    public List<String> autocomplete(UserInput commandStr, ContextBase context) {
        return new ArrayList<>(commands.keySet());
    }
}
