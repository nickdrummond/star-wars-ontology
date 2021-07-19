package com.nickd.sw;

import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SparqlTest extends TestCase {

    private static InfModel infModel;

    // One time load and classification
    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(SparqlTest.class)) {
            protected void setUp(  ) throws Exception {
                long t1 = System.currentTimeMillis();
                Model model = RDFDataMgr.loadModel("star-wars.owl.ttl", Lang.TURTLE);

                long t2 = System.currentTimeMillis();
                System.out.println("loaded in = " + (t2 - t1));

                // Try full blown OWL reasoning first - too slow
                // Transitive r does not work for instances of subclasses
                // RDFS Reasoner works for inferred instances. No SPARQL1.1 "+" needed
                // https://jena.apache.org/documentation/inference/
                t1 = t2;
                Reasoner owlReasoner = ReasonerRegistry.getRDFSReasoner();
                infModel = ModelFactory.createInfModel(owlReasoner, model);

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

    try (QueryExecution qexec = QueryExecutionFactory.create(query, infModel)) {
        qexec.execSelect().forEachRemaining(soln -> {
            Literal year = soln.getLiteral("year");
            RDFNode event = soln.get("event");
            RDFNode location = soln.get("loc");

            System.out.println(
                    ((year != null) ? year.getInt() : "?") +
                            "\t\t" + event.asResource().getLocalName() +
                            "\t\t" + ((location != null) ? location.asResource().getLocalName() : ""));
        });
    }
}

    public void testPeople() throws IOException {

        Query query = loadQuery("sparql/people.sparql");

        try (QueryExecution qexec = QueryExecutionFactory.create(query, infModel)) {
            qexec.execSelect().forEachRemaining(soln -> {
                RDFNode x = soln.get("varName") ;       // Get a result variable by name.
                Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
                Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
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
