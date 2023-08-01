package com.nickd.sw.builder.command;

import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;

import java.util.List;

public class RootContextCommand implements Command {

    public RootContextCommand() {
    }

    @Override
    public List<String> autocomplete(UserInput input, ContextBase context) {
        return List.of("Step back to the root");
    }

    @Override
    public ContextBase handle(UserInput input, ContextBase context) {
        while (!context.isRoot()) {
            context = context.getParent();
        }
        return context;
    }
}
