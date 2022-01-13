package com.nickd.sw.util;

import junit.extensions.TestSetup;
import junit.framework.Test;
import openllet.owlapi.OWLGenericTools;
import openllet.owlapi.OWLHelper;
import openllet.owlapi.OWLManagerGroup;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.Collections;

public class TestHelper extends TestSetup {

    public static String BASE = "https://raw.githubusercontent.com/nickdrummond/starwarsontology/master/ontologies";

    public OWLOntologyManager mngr;
    public OWLOntology ont;
    public OWLDataFactory df;
    public OWLReasoner r;

    public long timeToLoad;
    public long timeToClassify;


    public TestHelper(Test test, String iri) throws OWLOntologyCreationException {
        super(test);
        mngr = new OWLManager().get();
        mngr.setIRIMappers(Collections.singleton(new TestIRIMapper()));
        mngr.addOntologyLoaderListener(new LoadLogger());
        df = mngr.getOWLDataFactory();

        long start = System.currentTimeMillis();
        ont = mngr.loadOntology(IRI.create(iri));
        timeToLoad = System.currentTimeMillis() - start;
        System.out.println("Loaded in " + timeToLoad + "ms");
    }

    public OWLNamedIndividual ind(String s) {
        return df.getOWLNamedIndividual(IRI.create(BASE + "#" + s));
    }

    public OWLObjectProperty prop(String s) {
        return df.getOWLObjectProperty(IRI.create(BASE + "#" + s));
    }

    public OWLClass cls(String s) {
        return df.getOWLClass(IRI.create(BASE + "#" + s));
    }

    public void classify() throws OWLOntologyCreationException {
        try (final OWLManagerGroup group = new OWLManagerGroup()) {

        long start = System.currentTimeMillis();
        final OWLHelper owl = new OWLGenericTools(group, ont.getOntologyID(), true);

        r = owl.getReasoner();

        timeToClassify = System.currentTimeMillis() - start;
        System.out.println("Classified in " + timeToClassify + "ms");
    }
    }
}

