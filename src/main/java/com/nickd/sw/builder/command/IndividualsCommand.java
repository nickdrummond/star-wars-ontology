package com.nickd.sw.builder.command;

import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IndividualsCommand implements Command {
    private Helper helper;

    public IndividualsCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public ContextBase handle(UserInput commandStr, ContextBase context) {
        OWLOntology ont = context.getOntology(helper);
        Optional<OWLClass> cls = context.getOWLClass();
        List<OWLNamedIndividual> results = cls.isPresent() ? getInstances(cls.get()) : getAllIndividualsInSig(ont);
        return new ContextBase("individuals", context, results);
    }

    private List<OWLNamedIndividual> getInstances(OWLClass cls) {
        return helper.told.instances(cls).sorted().collect(Collectors.toList());
    }

    private List<OWLNamedIndividual> getAllIndividualsInSig(OWLOntology ont) {
        return ont.individualsInSignature().sorted().collect(Collectors.toList());
    }

    @Override
    public List<String> autocomplete(UserInput commandStr, ContextBase context) {
        return List.of("Get instances of a class in the context or all individuals");
    }
}
