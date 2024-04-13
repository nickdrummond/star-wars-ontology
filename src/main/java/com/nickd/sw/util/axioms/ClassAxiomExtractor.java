package com.nickd.sw.util.axioms;

import org.semanticweb.owlapi.model.*;

public class ClassAxiomExtractor extends AxiomExtractor<OWLClass> {

    public ClassAxiomExtractor(OWLClass target, OWLOntology src) {
        super(target, src);
    }

    @Override
    public OWLAxiom visit(OWLAnnotationAssertionAxiom axiom) {
        return axiom.getSubject().equals(target.getIRI()) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLSubClassOfAxiom axiom) {
        return axiom.getSubClass().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLEquivalentClassesAxiom axiom) {
        return axiom.containsEntityInSignature(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLHasKeyAxiom axiom) {
        return axiom.getClassExpression().containsEntityInSignature(target) ? axiom : null;
    }
//
//    @Override
//    public OWLAxiom visit(OWLClassAssertionAxiom axiom) {
//        return axiom.getClassExpression().equals(target) ? axiom : null;
//    }

    // TODO disjoints
//
//        @Override
//        public OWLAxiom visit(OWLDisjointClassesAxiom axiom) {
//            return axiom.containsEntityInSignature(target) ? axiom : null;
//        }
//
//        @Override
//        public OWLAxiom visit(OWLDisjointUnionAxiom axiom) {
//            return axiom.containsEntityInSignature(target) ? axiom : null;
//        }
}
