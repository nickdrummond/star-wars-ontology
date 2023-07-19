package com.nickd.sw.command;

import com.nickd.sw.util.FinderUtils;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class FindCommand implements Command {
    private Helper helper;

    public FindCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context context) {
        String[] input = commandStr.split(" ");

        if (input.length > 1) {
            String param = input[1];

            List<OWLEntity> results = FinderUtils.findByAnnotation(param, helper.df.getRDFSLabel(), helper);

            if (results.isEmpty()) {
                return context;
            }

            return new Context(commandStr, context, results);
        }
        return context;
    }
}
