package com.nickd.sw.util.axioms;

import org.semanticweb.owlapi.model.*;

public class DataPropertyAxiomExtractor extends AxiomExtractor<OWLDataProperty> {

    public DataPropertyAxiomExtractor(OWLDataProperty target, OWLOntology src) {
        super(target, src);
    }

    @Override
    public OWLAxiom visit(OWLSubDataPropertyOfAxiom axiom) {
        return axiom.getSubProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLEquivalentDataPropertiesAxiom axiom) {
        return axiom.containsEntityInSignature(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLDataPropertyDomainAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLDataPropertyRangeAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLFunctionalDataPropertyAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLDisjointDataPropertiesAxiom axiom) {
        return axiom.containsEntityInSignature(target) ? axiom : null;
    }
}
