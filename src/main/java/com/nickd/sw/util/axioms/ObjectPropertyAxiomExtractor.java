package com.nickd.sw.util.axioms;

import org.semanticweb.owlapi.model.*;

public class ObjectPropertyAxiomExtractor extends AxiomExtractor<OWLObjectProperty> {

    public ObjectPropertyAxiomExtractor(OWLObjectProperty target, OWLOntology src) {
        super(target, src);
    }

    @Override
    public OWLAxiom visit(OWLSubObjectPropertyOfAxiom axiom) {
        return axiom.getSubProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        return axiom.containsEntityInSignature(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLObjectPropertyDomainAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLObjectPropertyRangeAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLInverseObjectPropertiesAxiom axiom) {
        return axiom.getFirstProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLSubPropertyChainOfAxiom axiom) {
        return axiom.getSuperProperty().equals(target) ? axiom : null;
    }

    @Override
    public OWLAxiom visit(OWLFunctionalObjectPropertyAxiom axiom) {
        return characteristic(axiom);
    }

    @Override
    public OWLAxiom visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        return characteristic(axiom);
    }

    @Override
    public OWLAxiom visit(OWLTransitiveObjectPropertyAxiom axiom) {
        return characteristic(axiom);
    }

    @Override
    public OWLAxiom visit(OWLSymmetricObjectPropertyAxiom axiom) {
        return characteristic(axiom);
    }

    @Override
    public OWLAxiom visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        return characteristic(axiom);
    }

    @Override
    public OWLAxiom visit(OWLReflexiveObjectPropertyAxiom axiom) {
        return characteristic(axiom);
    }

    @Override
    public OWLAxiom visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        return characteristic(axiom);
    }

    @Override
    public OWLAxiom visit(OWLDisjointObjectPropertiesAxiom axiom) {
        return axiom.containsEntityInSignature(target) ? axiom : null;
    }

    private OWLAxiom characteristic(OWLObjectPropertyCharacteristicAxiom axiom) {
        return axiom.getProperty().equals(target) ? axiom : null;
    }
}
