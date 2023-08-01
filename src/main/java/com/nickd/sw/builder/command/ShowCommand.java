package com.nickd.sw.builder.command;

import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;
import com.nickd.sw.util.DescriptionVisitorEx;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Optional;

public class ShowCommand implements Command {
    private Helper helper;

    public ShowCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public ContextBase handle(UserInput input, ContextBase context) {
        Optional<OWLEntity> sel = Optional.empty();

        if (input.params().size() == 1) {
            sel = helper.entity(input.paramsAsString());
        } else if (context.isSingleSelection()) {
            sel = context.getOWLEntity();
        }

        return sel.map(e -> new ContextBase("axioms", context, e.accept(new DescriptionVisitorEx(context.getOntology(helper))))).orElse(context);
    }


    @Override
    public List<String> autocomplete(UserInput commandStr, ContextBase context) {
        return List.of("Describes the context");
    }
}
