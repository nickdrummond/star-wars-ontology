package com.nickd.sw.command;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class IndividualsCommand implements Command {
    private Helper helper;

    public IndividualsCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context context) {
        OWLOntology ont = context.getOntology(helper);
        Optional<OWLClass> cls = context.getOWLClass();
        List<OWLNamedIndividual> results = cls.isPresent() ? getInstances(cls.get()) : getAllIndividualsInSig(ont);
        return new Context("individuals", context, results);
    }

    private List<OWLNamedIndividual> getInstances(OWLClass cls) {
        return helper.told.instances(cls).sorted().collect(Collectors.toList());
    }

    private List<OWLNamedIndividual> getAllIndividualsInSig(OWLOntology ont) {
        return ont.individualsInSignature().sorted().collect(Collectors.toList());
    }
}
