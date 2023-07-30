package com.nickd.sw.builder.command;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.util.List;

public class OntologiesCommand implements Command {
    private final Helper helper;

    public OntologiesCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public List<String> autocomplete(UserInput commandStr, Context context) {
        return List.of("List ontologies");
    }

    @Override
    public Context handle(UserInput input, Context parentContext) {
        List<String> params = input.params();
        if (params.size() == 1) {
            OWLOntology ont = helper.ont(params.get(0));
            if (ont != null) {
                return new Context("", parentContext, ont);
            }
        }
        return new Context("ontologies", parentContext, List.copyOf(helper.mngr.getOntologies()));
    }
}
