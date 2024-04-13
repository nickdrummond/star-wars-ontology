package com.nickd.sw.util.axioms;

import org.semanticweb.owlapi.model.*;

public class IndividualAxiomExtractor extends AxiomExtractor<OWLNamedIndividual> {

    public IndividualAxiomExtractor(OWLNamedIndividual target, OWLOntology src) {
        super(target, src);
    }

    @Override
    public OWLAxiom visit(OWLAnnotationAssertionAxiom axiom) {
        return axiom.getSubject().equals(target.getIRI()) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLClassAssertionAxiom axiom) {
        return axiom.getIndividual().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLObjectPropertyAssertionAxiom axiom) {
        return axiom.getSubject().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        return axiom.getSubject().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLDataPropertyAssertionAxiom axiom) {
        return axiom.getSubject().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        return axiom.getSubject().equals(target) ? axiom : null;
    }

    // TODO
//        @Override
//        public OWLAxiom visit(OWLSameIndividualAxiom axiom) {
//            return axiom.containsEntityInSignature(target) ? axiom : null;
//        }
//
//        @Override
//        public OWLAxiom visit(OWLDifferentIndividualsAxiom axiom) {
//            return axiom.containsEntityInSignature(target) ? axiom : null;
//        }
}
