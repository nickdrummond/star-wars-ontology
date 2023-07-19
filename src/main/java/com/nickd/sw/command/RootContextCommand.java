package com.nickd.sw.command;

import com.nickd.sw.util.Helper;

public class RootContextCommand implements Command {
    private Helper helper;

    public RootContextCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context context) {
        while (!context.isRoot()) {
            context = context.getParent();
        }
        return context;
    }
}
