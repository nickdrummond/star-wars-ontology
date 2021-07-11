package com.nickd.sw;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.File;

public class ConsistencyTest extends TestCase {

    private static Reasoner r;
    private static long t;

    // One time load and classification
    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(ConsistencyTest.class)) {
            protected void setUp(  ) throws Exception {
                OWLOntologyManager mngr = new OWLManager().get();
                File starwarsOwl = new File("star-wars.owl");
                OWLOntology ont = mngr.loadOntologyFromOntologyDocument(starwarsOwl);
                Configuration conf = new Configuration();
                long start = System.currentTimeMillis();
                r = new Reasoner(conf, ont);
                t = System.currentTimeMillis() - start;
                System.out.println("Classified in " + t + "ms");

            }
            protected void tearDown(  ) throws Exception {
                // do your one-time tear down here!
            }
        };
        return setup;
    }

    public void testOntologyConsistent() {
        assertTrue(r.isConsistent());
    }

    public void testClassificationInLessThan2s() {
        assertTrue(t < 2000);
    }

    public void testInference() {
        OWLClass fight = r.getDataFactory().getOWLClass("http://null.com/star-wars#Fight");
        NodeSet<OWLNamedIndividual> results = r.getInstances(fight);
        assertEquals(39, results.getFlattened().size());
    }
}