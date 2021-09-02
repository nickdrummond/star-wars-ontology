package com.nickd.sw;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class SparqlTest extends TestCase {

    private static InfModel rdfsInfModel;

    // One time load and classification
    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(SparqlTest.class)) {
            protected void setUp(  ) throws Exception {
                long t1 = System.currentTimeMillis();

                Model model = RDFDataMgr.loadModel("ontologies/all.owl.ttl", Lang.TURTLE);
                File onts = new File("ontologies/");
                // load all ontologies/ - TODO use imports
                Arrays.stream(Objects.requireNonNull(onts.listFiles())).forEach(f -> {
                    System.out.println("f = " + f);
                    Model m = RDFDataMgr.loadModel(f.getAbsolutePath(), Lang.TURTLE);
                    model.add(m);
                });

                long t2 = System.currentTimeMillis();
                System.out.println("loaded in = " + (t2 - t1));

                // Try full blown OWL reasoning first - too slow
                // Transitive r does not work for instances of subclasses
                // RDFS Reasoner works for inferred instances. No SPARQL1.1 "+" needed
                // https://jena.apache.org/documentation/inference/
                t1 = t2;
                Reasoner rdfsReasoner = ReasonerRegistry.getRDFSReasoner();
                rdfsInfModel = ModelFactory.createInfModel(rdfsReasoner, model);

                t2 = System.currentTimeMillis();
                System.out.println("reasoner loaded = " + (t2 - t1));
            }
            protected void tearDown(  ) throws Exception {
                // do your one-time tear down here!
            }
        };
        return setup;
    }

public void testEvents() throws IOException {

    Query query = loadQuery("sparql/events.sparql");

    try (QueryExecution qexec = QueryExecutionFactory.create(query, rdfsInfModel)) {
        qexec.execSelect().forEachRemaining(soln -> {
            Literal year = soln.getLiteral("year");
            RDFNode event = soln.get("event");
            RDFNode after = soln.get("after");
            RDFNode during = soln.get("during");

            System.out.println(
                    ((year != null) ? year.getInt() : "?") +
                            "\t\t" + event.asResource().getLocalName() +
                            "\t\t" + ((after != null) ? "after: " + after.asResource().getLocalName() : "\t\t\t\t\t\t\t\t") +
                            "\t\t" + ((during != null) ? "during: " + during.asResource().getLocalName() : ""));
        });
    }
}

    public void testPeople() throws IOException {

        Query query = loadQuery("sparql/people.sparql");

        try (QueryExecution qexec = QueryExecutionFactory.create(query, rdfsInfModel)) {
            qexec.execSelect().forEachRemaining(soln -> {
                System.out.println("soln = " + soln);
            });
        }
    }

    public void testPlanets() throws IOException {

        Query query = loadQuery("sparql/planets.sparql");

        try (QueryExecution qexec = QueryExecutionFactory.create(query, rdfsInfModel)) {
            qexec.execSelect().forEachRemaining(soln -> {
                System.out.println("soln = " + soln);
            });
        }
    }

    private Query loadQuery(String s) throws IOException {
        Path path = Paths.get(s);
        String queryString = Files.lines(path).collect(Collectors.joining(System.lineSeparator()));
        return QueryFactory.create(queryString) ;
    }
}
