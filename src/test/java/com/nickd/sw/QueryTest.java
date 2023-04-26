package com.nickd.sw;

import com.nickd.sw.util.Helper;
import com.nickd.sw.util.StarWarsOntologiesIRIMapper;
import com.nickd.sw.util.TestHelper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.util.concurrent.TimeUnit;

public class QueryTest extends TestCase {

    private static TestHelper helper;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(
                new TestSuite(QueryTest.class),
                Helper.BASE + "/events.owl.ttl",
                new StarWarsOntologiesIRIMapper());
        return helper;
    }

    public void testEventLocationQueries() {
        OWLClass event = helper.cls("Event");
        OWLObjectProperty locatedIn = helper.prop("locatedIn");
        OWLNamedIndividual place = helper.ind("Outer_Rim");
        long fullStart = System.nanoTime();

        helper.classify();
        NodeSet<OWLClass> events = helper.r().getSubClasses(event);
        helper.clearReasoner();
        events.forEach(e -> {
            helper.classify();
            OWLClassExpression svp = helper.df().getOWLObjectHasValue(locatedIn, place);
            OWLClass representativeEvent = e.getRepresentativeElement();
            OWLObjectIntersectionOf query = helper.df().getOWLObjectIntersectionOf(representativeEvent, svp);
            System.out.print(helper.render(event) + " = ");
            long start = System.nanoTime();
            NodeSet<OWLNamedIndividual> results = helper.r().getInstances(query);
            long d = System.nanoTime() - start;
            System.out.println(results.getFlattened().size()  + " in " + TimeUnit.NANOSECONDS.toMillis(d) + "ms");
            helper.clearReasoner();
        });

        long fullEnd = System.nanoTime() - fullStart;
        System.out.println("Completed in " + TimeUnit.NANOSECONDS.toMillis(fullEnd) + "ms");

    }
}