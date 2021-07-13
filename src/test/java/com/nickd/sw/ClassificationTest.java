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

public class ClassificationTest extends TestCase {

    private String BASE = "http://null.com/star-wars#";
    private static Reasoner r;
    private static long t;

    // One time load and classification
    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(ClassificationTest.class)) {
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

    // We assert that Cody is a Leader inOrganisation 7th Sky Corps, which is a member of the Grand Army of the Republ.
    // So, cody should be a member of the Army and Galactic Republic
    public void testMembershipTransitivity() {
        OWLNamedIndividual cody = r.getDataFactory().getOWLNamedIndividual(BASE+"Cody");

        OWLNamedIndividual republic = r.getDataFactory().getOWLNamedIndividual(BASE+"Galactic_Republic");
        OWLObjectProperty memberOf = r.getDataFactory().getOWLObjectProperty(BASE+"memberOf");

        OWLClassExpression expr = r.getDataFactory().getOWLObjectHasValue(memberOf, republic);

        NodeSet<OWLNamedIndividual> results = r.getInstances(expr);

        assertTrue(results.getFlattened().contains(cody));
    }

    // We assert that Hunter is a soldier in the Bad Batch, which is in the Grand Army
    // Clone Troopers are soldiers in the Grand Army
    public void testInOrganisationTransitivity() {
        OWLClass cloneTrooper = r.getDataFactory().getOWLClass(BASE+"Clone_Trooper");
        OWLNamedIndividual hunter = r.getDataFactory().getOWLNamedIndividual(BASE+"Hunter");

        NodeSet<OWLNamedIndividual> results = r.getInstances(cloneTrooper);

        assertTrue(results.getFlattened().contains(hunter));
    }
}