package com.nickd.sw.builder.command;

import java.util.List;

public class RootContextCommand implements Command {

    public RootContextCommand() {
    }

    @Override
    public List<String> autocomplete(UserInput input, Context context) {
        return List.of("Step back to the root");
    }

    @Override
    public Context handle(UserInput input, Context context) {
        while (!context.isRoot()) {
            context = context.getParent();
        }
        return context;
    }
}
