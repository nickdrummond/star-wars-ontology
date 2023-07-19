package com.nickd.sw.command;

import com.nickd.sw.util.Helper;

public class BackContextCommand implements Command {
    private Helper helper;

    public BackContextCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context context) {
        Context parent = context.getParent();
        return parent != null ? parent : context;
    }
}
