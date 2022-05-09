package com.nickd.sw;

import com.nickd.sw.util.TestHelper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.File;

public class ClassificationTest extends TestCase {

    private static TestHelper helper;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(new TestSuite(ClassificationTest.class), TestHelper.BASE + "/star-wars.owl.ttl");
        helper.classify();
        return helper;
    }

    // Cody is a Commander inOrganisation 7th Sky Corps,
    // 7th Sky Corps is in the Grand Army of the Republ., in the Republic
    // So, cody SHOULD be a member of the Galactic Republic
    //
    // With pellet this does not work - you have to assert Cody is a member of 7thSC.
    // Even though pellet correctly infers he is a member of 7thSC, but does not follow the transitivity
    public void testMembershipTransitivity() {

        OWLClassExpression expr = helper.df.getOWLObjectHasValue(
                helper.prop("memberOf"), helper.ind("Galactic_Republic"));

        NodeSet<OWLNamedIndividual> results = helper.r.getInstances(expr);

        assertTrue(results.getFlattened().contains(helper.ind("Cody")));
    }

    // Hunter is a Sergeant in the Bad Batch, which is in the Grand Army
    // therefore he is an Officer in some part of in the Grand Army.
    //
    // This query works well as some roles may not be transitive:
    // Leader of Red Squadron is not Leader of the Rebellion
    //
    // hadRole some (Officer and inOrganisation some (memberOf value Grand_Army_of_the_Republic))
    public void testRolesInOrganisation() {
        OWLClassExpression expr =
                helper.df.getOWLObjectSomeValuesFrom(helper.prop("hadRole"),
                        helper.df.getOWLObjectIntersectionOf(helper.cls("Officer"),
                                helper.df.getOWLObjectSomeValuesFrom(helper.prop("inOrganisation"),
                                        helper.df.getOWLObjectHasValue(helper.prop("memberOf"),
                                            helper.ind("Grand_Army_of_the_Republic"))
                    )));

        NodeSet<OWLNamedIndividual> results = helper.r.getInstances(expr);

        assertTrue(results.getFlattened().contains(helper.ind("Hunter")));
    }

    // Luke is from Mos Eisely, therefore he is from a place in the Outer Rim
    // "originallyFrom some (locatedIn value Outer_Rim)"
    public void testLocationTransitivity() {
        NodeSet<OWLNamedIndividual> results = helper.r.getInstances(
                helper.df.getOWLObjectSomeValuesFrom(helper.prop("originallyFrom"),
                    helper.df.getOWLObjectHasValue(helper.prop("locatedIn"),
                            helper.ind("Outer_Rim"))
        ));

        assertTrue(results.getFlattened().contains(helper.ind("Luke_Skywalker")));
    }
}