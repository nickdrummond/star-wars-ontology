package com.nickd.sw.report;

import org.apache.jena.query.*;

import java.io.IOException;

public class EventsSparqlReport {

    public static void main(String args[]) throws IOException {

        final SparqlReport report = new SparqlReport("ontologies/events.owl.ttl");

        final Query query = report.loadQuery("sparql/events.sparql");

        try (QueryExecution qexec = report.run(query)) {
            ResultSetFormatter.out(qexec.execSelect());
        }
    }
}
