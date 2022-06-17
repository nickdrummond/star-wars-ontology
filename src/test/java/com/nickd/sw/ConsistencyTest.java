package com.nickd.sw;

import com.nickd.sw.util.TestHelper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import openllet.core.utils.SetUtils;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.util.HashSet;
import java.util.Set;

public class ConsistencyTest extends TestCase {

    private static TestHelper helper;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(
                new TestSuite(ConsistencyTest.class),
                Helper.BASE + "/all.owl.ttl",
                new StarWarsOntologiesIRIMapper());
        helper.classify();
        return helper;
    }

    public void testOntologyConsistent() {
        assertTrue(helper.r().isConsistent());
    }

    public void testClassificationInLessThan1s() {
        assertTrue(helper.timeToClassify() < 700);
    }

    public void testMurderedSpeed() {
        OWLClass murder = helper.cls("Murder");
        OWLObjectProperty killedIn = helper.prop("killedIn");

        OWLClassExpression murdered = helper.df().getOWLObjectSomeValuesFrom(killedIn, murder);
        long start = System.currentTimeMillis();
        NodeSet<OWLNamedIndividual> results = helper.r().getInstances(murdered);
        long d = System.currentTimeMillis() - start;

        System.out.println("Murder inference = " + d + "ms");
        assertTrue(results.getFlattened().size() > 0);
        assertTrue("Murder inference too slow", d < 15000); // 15s
    }

    public void testDisjointLivingThings() {
        OWLClass livingThing = helper.cls("Living_thing");
        Set<OWLNamedIndividual> allLivingThingInstances = new HashSet<>(helper.r().getInstances(livingThing).getFlattened());

        assertTrue(allLivingThingInstances.size() > 0);

        // Living_thing and not{Luke_Skywalker}
        OWLIndividual lukeSkywalker = helper.ind("#Luke_Skywalker");
        Set<OWLNamedIndividual> notLuke = helper.r().getInstances(
                helper.df().getOWLObjectIntersectionOf(
                        livingThing,
                        helper.df().getOWLObjectComplementOf(
                                helper.df().getOWLObjectOneOf(lukeSkywalker)))
            ).getFlattened();


        // TODO generate an allDisjoint!
        assertEquals(new HashSet<>(), SetUtils.difference(allLivingThingInstances, notLuke));
    }
}
