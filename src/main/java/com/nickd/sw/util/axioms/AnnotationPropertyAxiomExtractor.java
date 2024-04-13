package com.nickd.sw.util.axioms;

import org.semanticweb.owlapi.model.*;

public class AnnotationPropertyAxiomExtractor extends AxiomExtractor<OWLAnnotationProperty> {

    public AnnotationPropertyAxiomExtractor(OWLAnnotationProperty target, OWLOntology src) {
        super(target, src);
    }

    @Override
    public OWLAxiom visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        return axiom.getSubProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLAnnotationPropertyDomainAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLAnnotationPropertyRangeAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }
}
