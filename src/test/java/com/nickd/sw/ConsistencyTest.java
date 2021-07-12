package com.nickd.sw;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.File;

public class ConsistencyTest extends TestCase {

    private String BASE = "http://null.com/star-wars#";
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

    public void testMurderedSpeed() {
        OWLClass murder = r.getDataFactory().getOWLClass(BASE+"Murder");
        OWLObjectProperty killedIn = r.getDataFactory().getOWLObjectProperty(BASE+"killedIn");

        OWLClassExpression murdered = r.getDataFactory().getOWLObjectSomeValuesFrom(killedIn, murder);
        long start = System.currentTimeMillis();
        NodeSet<OWLNamedIndividual> results = r.getInstances(murdered);
        long d = System.currentTimeMillis() - start;

        assertTrue(results.getFlattened().size() > 0);
        assertTrue("Murder inference too slow: " + d + "ms", d < 10000); // 10s
    }
}