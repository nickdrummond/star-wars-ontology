package com.nickd.sw.util;

import org.semanticweb.owlapi.model.*;

/**
 * Is the given entity the subject of the axiom?
 */
class IsSubjectOfAxiomVisitorEx implements OWLAxiomVisitorEx<Boolean> {
    private final OWLEntity entity;

    public IsSubjectOfAxiomVisitorEx(OWLEntity entity) {
        this.entity = entity;
    }

    @Override
    public Boolean visit(OWLSubClassOfAxiom axiom) {
        return axiom.getSubClass().equals(entity);
    }

    @Override
    public Boolean visit(OWLEquivalentClassesAxiom axiom) {
        return axiom.namedClasses().anyMatch(c -> c.equals(entity));
    }

    @Override
    public Boolean visit(OWLClassAssertionAxiom axiom) {
        return axiom.getIndividual().equals(entity);
    }

    @Override
    public Boolean visit(OWLSameIndividualAxiom axiom) {
        return axiom.individuals().anyMatch(i -> i.equals(entity));
    }

    @Override
    public Boolean visit(OWLObjectPropertyAssertionAxiom axiom) {
        return axiom.getSubject().equals(entity);
    }

    @Override
    public Boolean visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        return axiom.getSubject().equals(entity);
    }

    @Override
    public Boolean visit(OWLDataPropertyAssertionAxiom axiom) {
        return axiom.getSubject().equals(entity);
    }

    @Override
    public Boolean visit(OWLAnnotationAssertionAxiom axiom) {
        return axiom.getSubject().asIRI().stream().anyMatch(iri -> iri.equals(entity.getIRI()));
    }

    @Override
    public Boolean visit(OWLObjectPropertyDomainAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLObjectPropertyRangeAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLFunctionalObjectPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLFunctionalDataPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLSymmetricObjectPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLTransitiveObjectPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLSubObjectPropertyOfAxiom axiom) {
        return axiom.getSubProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLInverseObjectPropertiesAxiom axiom) {
        return axiom.getFirstProperty().equals(entity) || axiom.getSecondProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        return axiom.properties().anyMatch(c -> c.equals(entity));
    }

    @Override
    public Boolean visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLReflexiveObjectPropertyAxiom axiom) {
        return axiom.getProperty().equals(entity);
    }

    @Override
    public Boolean visit(OWLSubPropertyChainOfAxiom axiom) {
        return axiom.getSuperProperty().equals(entity);
    }



    @Override
    public <T> Boolean doDefault(T object) {
        return false;
    }
}
