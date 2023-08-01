package com.nickd.sw.builder.command;

import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.util.List;

public class OntologiesCommand implements Command {
    private final Helper helper;

    public OntologiesCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public List<String> autocomplete(UserInput commandStr, ContextBase context) {
        return List.of("List ontologies");
    }

    @Override
    public ContextBase handle(UserInput input, ContextBase parentContext) {
        List<String> params = input.params();
        if (params.size() == 1) {
            OWLOntology ont = helper.ont(params.get(0));
            if (ont != null) {
                return new ContextBase("", parentContext, ont);
            }
        }
        return new ContextBase("ontologies", parentContext, List.copyOf(helper.mngr.getOntologies()));
    }
}
