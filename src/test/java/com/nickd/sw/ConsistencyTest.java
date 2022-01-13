package com.nickd.sw;

import com.nickd.sw.util.TestHelper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

public class ConsistencyTest extends TestCase {

    private static TestHelper helper;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(new TestSuite(ConsistencyTest.class), TestHelper.BASE + "/all.owl.ttl");
        helper.classify();
        return helper;
    }

    public void testOntologyConsistent() {
        assertTrue(helper.r.isConsistent());
    }

    public void testClassificationInLessThan1s() {
        assertTrue(helper.timeToClassify < 700);
    }

    public void testMurderedSpeed() {
        OWLClass murder = helper.df.getOWLClass(TestHelper.BASE + "#Murder");
        OWLObjectProperty killedIn = helper.df.getOWLObjectProperty(TestHelper.BASE + "#killedIn");

        OWLClassExpression murdered = helper.df.getOWLObjectSomeValuesFrom(killedIn, murder);
        long start = System.currentTimeMillis();
        NodeSet<OWLNamedIndividual> results = helper.r.getInstances(murdered);
        long d = System.currentTimeMillis() - start;

        System.out.println("Murder inference = " + d + "ms");
        assertTrue(results.getFlattened().size() > 0);
        assertTrue("Murder inference too slow", d < 15000); // 15s
    }
}