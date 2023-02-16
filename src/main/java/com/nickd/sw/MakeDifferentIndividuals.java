package com.nickd.sw;

import com.github.jsonldjava.shaded.com.google.common.collect.Sets;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MakeDifferentIndividuals {

    private final OWLReasoner r;
    private final OWLDataFactory df;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {

        Helper helper = new Helper("events.owl.ttl", new StarWarsOntologiesIRIMapper());

        helper.classify();

        OWLClass query = helper.cls("Living_thing");
        OWLOntology starwarsOnt = helper.mngr.getOntology(IRI.create("https://nickdrummond.github.io/star-wars-ontology/ontologies/star-wars.owl.ttl"));

        MakeDifferentIndividuals action = new MakeDifferentIndividuals(helper.r, helper.df);

        List<OWLOntologyChange> changes = action.run(query, Objects.requireNonNull(starwarsOnt));

        if (changes.isEmpty()) {
            System.out.println("No changes to make");
        }
        else {
            helper.mngr.applyChanges(changes);
            helper.classify();
            if (helper.r.isConsistent()) {
                helper.saveChanged();
            }
            else {
                throw new RuntimeException("Ontology inconsistent with changes");
            }
        }
    }

    public MakeDifferentIndividuals(final OWLReasoner r, final OWLDataFactory df) {
        this.r = r;
        this.df = df;
    }

    public List<OWLOntologyChange> run(final OWLClassExpression query, final OWLOntology targetOntology) {
        List<OWLOntologyChange> changes = new ArrayList<>();
        Set<OWLNamedIndividual> instances = getRepresentativeInstances(query);
        OWLAxiom allDifferent = df.getOWLDifferentIndividualsAxiom(instances);
        if (!targetOntology.containsAxiom(allDifferent)) {
            changes.add(new AddAxiom(targetOntology, allDifferent));
            Set<OWLDifferentIndividualsAxiom> oldAxioms = Sets.newHashSet();
            for (OWLNamedIndividual i : instances) {
                oldAxioms.addAll(targetOntology.getDifferentIndividualAxioms(i));
            }
            for (OWLDifferentIndividualsAxiom oldAx : oldAxioms) {
                changes.add(new RemoveAxiom(targetOntology, oldAx));
            }
        }
        return changes;
    }

    private Set<OWLNamedIndividual> getRepresentativeInstances(final OWLClassExpression query) {
        return r.getInstances(query, false)
                .nodes()
                .map(Node::getRepresentativeElement)
                .collect(Collectors.toSet());
    }
}
