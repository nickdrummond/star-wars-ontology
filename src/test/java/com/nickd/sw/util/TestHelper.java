package com.nickd.sw.util;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.File;
import java.util.Set;
import java.util.stream.Stream;

public class TestHelper extends TestSetup {

    private final Helper helper;

    public TestHelper(Test test, String iri, OWLOntologyIRIMapper ontologyIRIMapper) throws OWLOntologyCreationException {
        super(test);
        helper = new Helper(iri, ontologyIRIMapper);
    }

    public TestHelper(TestSuite test, File file) throws OWLOntologyCreationException {
        super(test);
        helper = new Helper(file);
    }

    public String render (OWLEntity entity) {
        return helper.render(entity);
    }

    public OWLEntity check(String name) {
        return helper.check(name);
    }

    public OWLClassExpression mos(String s) {
        return helper.mos(s);
    }

    public OWLNamedIndividual ind(String s) {
        return helper.ind(s);
    }

    public OWLObjectProperty prop(String s) {
        return helper.prop(s);
    }

    public OWLClass cls(String s) { return helper.cls(s);}

    public void clearReasoner() { helper.clearReasoner(); }

    public void classify() { helper.classify(); }

    public OWLDataFactory df() { return helper.df ;}

    public OWLReasoner r() {return helper.r;}

    public long timeToClassify() {
        return helper.timeToClassify;
    }

    public long timeToLoad() {
        return helper.timeToLoad;
    }

    public OWLOntology ont() {
        return helper.ont;
    }

    public Set<OWLOntology> onts() {
        return helper.mngr.getOntologies();
    }

    public <T extends OWLAxiom> Stream<T> getAxioms(AxiomType<T> axiomType) {
        return helper.ont.getAxioms(axiomType, true).stream();
    }

    public String render(OWLObject o) {
        return helper.render(o);
    }
}

