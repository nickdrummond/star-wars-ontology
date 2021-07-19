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
    private static OWLDataFactory df;

    // One time load and classification
    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(ClassificationTest.class)) {
            protected void setUp(  ) throws Exception {
                OWLOntologyManager mngr = new OWLManager().get();
                File starwarsOwl = new File("star-wars.owl.ttl");
                OWLOntology ont = mngr.loadOntologyFromOntologyDocument(starwarsOwl);
                Configuration conf = new Configuration();
                long start = System.currentTimeMillis();
                r = new Reasoner(conf, ont);
                df = r.getDataFactory();
                long t = System.currentTimeMillis() - start;
                System.out.println("Classified in " + t + "ms");

            }
            protected void tearDown(  ) throws Exception {
                // do your one-time tear down here!
            }
        };
        return setup;
    }

    private OWLNamedIndividual ind(String s) {
        return df.getOWLNamedIndividual(BASE + s);
    }

    private OWLObjectProperty prop(String s) {
        return df.getOWLObjectProperty(BASE + s);
    }

    private OWLClass cls(String s) {
        return df.getOWLClass(BASE + s);
    }

    // Cody is a Leader inOrganisation 7th Sky Corps, in the Grand Army of the Republ., in the Republic
    // So, cody should be a member of the Galactic Republic
    public void testMembershipTransitivity() {

        OWLClassExpression expr = df.getOWLObjectHasValue(
                prop("memberOf"), ind("Galactic_Republic"));

        NodeSet<OWLNamedIndividual> results = r.getInstances(expr);

        assertTrue(results.getFlattened().contains(ind("Cody")));
    }

    // We assert that Hunter is a Trooper in the Bad Batch, which is in the Grand Army
    // therefore he is a Trooper in the Grand Army
    public void testInOrganisationTransitivity() {
        OWLClassExpression expr =
                df.getOWLObjectSomeValuesFrom(prop("hasRole"),
                    df.getOWLObjectIntersectionOf(cls("Trooper"),
                        df.getOWLObjectHasValue(prop("inOrganisation"), ind("Grand_Army_of_the_Republic"))
                    )
                );

        NodeSet<OWLNamedIndividual> results = r.getInstances(expr);

        assertTrue(results.getFlattened().contains(ind("Hunter")));
    }

    // Luke is from Mos Eisely,
    // therefore he is from the Outer Rim
    public void testFromTransitivity() {
        NodeSet<OWLNamedIndividual> results = r.getInstances(
                df.getOWLObjectHasValue(prop("from"), ind("Outer_Rim"))
        );

        assertTrue(results.getFlattened().contains(ind("Luke_Skywalker")));
    }
}