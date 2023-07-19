package com.nickd.sw.command;

import com.nickd.sw.util.Helper;
import org.apache.jena.base.Sys;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class OntologiesCommand implements Command {
    private Helper helper;

    public OntologiesCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context parentContext) {
        String[] input = commandStr.split(" ");

        if (input.length == 2) {
            OWLOntology ont = helper.ont(input[1]);
            if (ont != null) {
                return new Context("", parentContext, ont);
            }
        }
        return new Context("ontologies", parentContext, List.copyOf(helper.mngr.getOntologies()));
    }
}
