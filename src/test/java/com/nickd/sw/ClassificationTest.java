package com.nickd.sw;

import com.nickd.sw.util.TestHelper;
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

    private static TestHelper helper;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(new TestSuite(ClassificationTest.class), TestHelper.BASE + "/all.owl.ttl");
        helper.classify();
        return helper;
    }

    // Cody is a Leader inOrganisation 7th Sky Corps, in the Grand Army of the Republ., in the Republic
    // So, cody should be a member of the Galactic Republic
    public void testMembershipTransitivity() {

        OWLClassExpression expr = helper.df.getOWLObjectHasValue(
                helper.prop("memberOf"), helper.ind("Galactic_Republic"));

        NodeSet<OWLNamedIndividual> results = helper.r.getInstances(expr);

        assertTrue(results.getFlattened().contains(helper.ind("Cody")));
    }

    // We assert that Hunter is a Trooper in the Bad Batch, which is in the Grand Army
    // therefore he is a Trooper in the Grand Army
    public void testInOrganisationTransitivity() {
        OWLClassExpression expr =
                helper.df.getOWLObjectSomeValuesFrom(helper.prop("hasRole"),
                        helper.df.getOWLObjectIntersectionOf(helper.cls("Trooper"),
                                helper.df.getOWLObjectHasValue(helper.prop("inOrganisation"), helper.ind("Grand_Army_of_the_Republic"))
                    )
                );

        NodeSet<OWLNamedIndividual> results = helper.r.getInstances(expr);

        assertTrue(results.getFlattened().contains(helper.ind("Hunter")));
    }

    // Luke is from Mos Eisely,
    // therefore he is from the Outer Rim
    public void testFromTransitivity() {
        NodeSet<OWLNamedIndividual> results = helper.r.getInstances(
                helper.df.getOWLObjectHasValue(helper.prop("from"), helper.ind("Outer_Rim"))
        );

        assertTrue(results.getFlattened().contains(helper.ind("Luke_Skywalker")));
    }
}