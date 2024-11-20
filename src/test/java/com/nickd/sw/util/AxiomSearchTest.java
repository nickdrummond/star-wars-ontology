package com.nickd.sw.util;

import openllet.owlapi.PelletReasoner;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import static org.hamcrest.MatcherAssert.assertThat;

public class AxiomSearchTest {
    private OWLReasoner r;
    private OWLOntology ont;
    private OWLDataFactory df;
    private OWLOntologyManager mngr;

    @Before
    public void setUp() throws Exception {
        mngr = OWLManager.createOWLOntologyManager();
        df = mngr.getOWLDataFactory();
        ont = mngr.createOntology();
    }

    @After
    public void tearDown() throws Exception {
        r.dispose();
    }

    @Test
    public void axiomSearch() {
        // GIVEN an ontology with a few axioms
        var a = df.getOWLClass(IRI.create("http://null.org/A"));
        var asub = df.getOWLClass(IRI.create("http://null.org/Asub"));
        var b = df.getOWLClass(IRI.create("http://null.org/B"));
        var c = df.getOWLClass(IRI.create("http://null.org/C"));
        var d = df.getOWLClass(IRI.create("http://null.org/D"));
        var p = df.getOWLObjectProperty("http://null.org/p");

        // asub -> a
        ont.addAxiom(df.getOWLSubClassOfAxiom(asub, a));

        // b -> p some asub
        var bSubPSomeASub = df.getOWLSubClassOfAxiom(b, df.getOWLObjectSomeValuesFrom(p, asub));
        ont.addAxiom(bSubPSomeASub);

        // c -> p some d
        var cSubPSomeD = df.getOWLSubClassOfAxiom(c, df.getOWLObjectSomeValuesFrom(p, d));
        ont.addAxiom(cSubPSomeD);

        r = new PelletReasoner(ont, BufferingMode.BUFFERING);

        // WHEN we try to find axioms with a component satisfying "p some a"
        var matchingAxioms = AxiomSearch.axiomSearch(r, df.getOWLObjectSomeValuesFrom(p, a));

        matchingAxioms.forEach(System.out::println);

        // THEN we match b -> p some ASub on the "p some Asub"
        var match = new AxiomSearch.AxiomMatch(bSubPSomeASub, df.getOWLObjectSomeValuesFrom(p, asub));
        assertThat(matchingAxioms, CoreMatchers.hasItem(match));
        // BUT not trivially because of the B
        Assert.assertEquals(1, matchingAxioms.size());
    }
}