package com.nickd.sw.builder.command;

import com.nickd.sw.builder.Constants;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NewInstanceCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(NewInstanceCommand.class);

    private final OWLAnnotationProperty editorLabel;
    private final OWLAnnotationProperty legacyId;
    private final OWLAnnotationProperty rdfsLabel;
    private final OWLAnnotationProperty seeAlso;
    private final OWLDatatype anyURI;
    private final Helper helper;

    public NewInstanceCommand(Helper helper, OWLAnnotationProperty editorLabel) {
        this.helper = helper;

        this.editorLabel = editorLabel;
        legacyId = helper.annotProp(Constants.LEGACY_ID, Constants.UTIL_BASE);
        rdfsLabel = helper.df.getRDFSLabel();
        seeAlso = helper.df.getRDFSSeeAlso();
        anyURI = helper.df.getOWLDatatype(XSDVocabulary.ANY_URI);
    }


    @Override
    public List<String> autocomplete(UserInput commandStr, Context context) {
        return List.of("Create a new individual with some paramsAsString");
    }

    @Override
    public Context handle(UserInput input, Context context) {

        List<String> params = input.params();

        if (params.size() >= 3) {
            String type = params.get(0);
            OWLClass cls = helper.cls(type);

            if (!helper.ont.containsClassInSignature(cls.getIRI(), Imports.INCLUDED)) {
                logger.warn("No class found in ontologies: " + cls);
                return context;
            }

            String label = params.get(1);
            String id = toId(label);
            OWLNamedIndividual ind = helper.ind(id);

            if (helper.ont.containsEntityInSignature(ind.getIRI(), Imports.INCLUDED)) {
                logger.warn("IRI already used in ontologies: " + ind);
                return context;
            }

            OWLOntology targetOntology = context.getOntology(helper);

            List<OWLOntologyChange> changes = new ArrayList<>();
            changes.add(addDeclaration(ind, targetOntology));
            changes.add(addType(ind, cls, targetOntology));
            changes.add(addLabel(label, ind, targetOntology));
            changes.add(addEditorLabel(id, ind, targetOntology));
            changes.add(addLegacyId(ind, targetOntology));

            if (params.size() == 3) {
                changes.add(addSeeAlso(params.get(2), ind, targetOntology));
            }
            // TODO else check if there is a ref at WOOKIEEPEDIA_BASE/label

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
        return new AddAxiom(targetOntology, getAnnotationAxiom(rdfsLabel, ind, helper.lit(label, Constants.DEFAULT_LANG)));
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
