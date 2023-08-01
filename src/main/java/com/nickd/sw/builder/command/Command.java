package com.nickd.sw.builder.command;

import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;

import java.util.List;

public interface Command {

    ContextBase handle(UserInput commandStr, ContextBase context);

    /**
     * Gets called if the current command ends with a string "?"
     * Could be used as a command help or to complete names in paramsAsString
     * @param commandStr
     * @param context
     * @return
     */
    List<String> autocomplete(UserInput commandStr, ContextBase context);
}
