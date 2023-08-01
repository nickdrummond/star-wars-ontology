package com.nickd.sw.builder.command;

import com.nickd.sw.builder.Context;
import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;
import com.nickd.sw.util.Helper;

import java.util.List;
import java.util.stream.Collectors;

public class BackContextCommand implements Command {

    private final Helper helper;

    public BackContextCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public ContextBase handle(UserInput input, ContextBase context) {
        if (input.params().isEmpty()) {
            Context parent = context.getParent();
            return parent != null ? parent : context;
        }
        else {
            String searchFor = input.paramsAsString();
            return context.stack().stream()
                    .filter(c -> c.toString(helper).equals(searchFor))
                    .findFirst().orElse(context);
        }
    }

    @Override
    public List<String> autocomplete(UserInput commandStr, ContextBase context) {
        return context.stack().stream().map(c->c.toString(helper)).collect(Collectors.toList());
    }
}
