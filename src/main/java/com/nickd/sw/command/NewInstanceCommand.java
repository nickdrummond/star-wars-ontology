package com.nickd.sw.command;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewInstanceCommand implements Command {
    public static final String LANG = "en";
    public static final String UTIL_BASE = "https://nickdrummond.github.io/star-wars-ontology/util";
    private final OWLAnnotationProperty editorLabel;
    private final OWLAnnotationProperty legacyId;
    private final OWLAnnotationProperty rdfsLabel;
    private final OWLAnnotationProperty seeAlso;
    private final OWLDatatype anyURI;
    private final Helper helper;

    public NewInstanceCommand(Helper helper) {
        this.helper = helper;

        editorLabel = helper.annotProp("editorLabel", UTIL_BASE);
        legacyId = helper.annotProp("legacyId", UTIL_BASE);
        rdfsLabel = helper.df.getRDFSLabel();
        seeAlso = helper.df.getRDFSSeeAlso();
        anyURI = helper.df.getOWLDatatype(XSDVocabulary.ANY_URI);
    }

    @Override
    public Context handle(String commandStr, Context context) {
        String[] input = commandStr.split(" ");

        if (input.length >= 3) {
            String type = input[1];
            OWLClass cls = helper.cls(type);

            if (!helper.ont.containsClassInSignature(cls.getIRI(), Imports.INCLUDED)) {
                System.err.println("No class found in ontologies: " + cls);
                return context;
            }

            String label = input[2];
            String id = toId(label);
            OWLNamedIndividual ind = helper.ind(id);

            if (helper.ont.containsEntityInSignature(ind.getIRI(), Imports.INCLUDED)) {
                System.err.println("IRI already used in ontologies: " + ind);
                return context;
            }

            OWLOntology targetOntology = context.getOntology(helper);

            List<OWLOntologyChange> changes = new ArrayList<>();
            changes.add(addDeclaration(ind, targetOntology));
            changes.add(addType(ind, cls, targetOntology));
            changes.add(addLabel(label, ind, targetOntology));
            changes.add(addEditorLabel(id, ind, targetOntology));
            changes.add(addLegacyId(ind, targetOntology));

            if (input.length == 4) {
                changes.add(addSeeAlso(input[3], ind, targetOntology));
            }

            helper.mngr.applyChanges(changes);

            return new Context("", context, ind);
        }
        return context;
    }

    private OWLOntologyChange addType(OWLNamedIndividual ind, OWLClass cls, OWLOntology targetOntology) {
        return new AddAxiom(targetOntology, helper.df.getOWLClassAssertionAxiom(cls, ind));
    }

    private AddAxiom addLegacyId(OWLNamedIndividual ind, OWLOntology targetOntology) {
        return new AddAxiom(targetOntology, getAnnotationAxiom(legacyId, ind, helper.lit(Integer.toString(ind.hashCode()))));
    }

    private AddAxiom addDeclaration(OWLNamedIndividual ind, OWLOntology targetOntology) {
        return new AddAxiom(targetOntology, helper.df.getOWLDeclarationAxiom(ind));
    }

    private AddAxiom addEditorLabel(String id, OWLNamedIndividual ind, OWLOntology targetOntology) {
        return new AddAxiom(targetOntology, getAnnotationAxiom(editorLabel, ind, helper.lit(id)));
    }

    private AddAxiom addLabel(String label, OWLNamedIndividual ind, OWLOntology targetOntology) {
        return new AddAxiom(targetOntology, getAnnotationAxiom(rdfsLabel, ind, helper.lit(label, LANG)));
    }

    private AddAxiom addSeeAlso(String url, OWLNamedIndividual ind, OWLOntology targetOntology) {
        return new AddAxiom(targetOntology, getAnnotationAxiom(seeAlso, ind, helper.lit(url, anyURI)));
    }

    private OWLAnnotationAssertionAxiom getAnnotationAxiom(OWLAnnotationProperty prop, OWLNamedIndividual ind, OWLLiteral value) {
        return helper.df.getOWLAnnotationAssertionAxiom(prop, ind.getIRI(), value);
    }

    private String toId(String label) {
        return label.replaceAll(" ", "_");
    }
}
