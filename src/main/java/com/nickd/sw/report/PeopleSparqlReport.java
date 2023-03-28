package com.nickd.sw.report;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSetFormatter;

import java.io.IOException;

/** Only works effectively on the Abox transformed ontologies */
public class PeopleSparqlReport {

    public static void main(String args[]) throws IOException {

        final SparqlReport report = new SparqlReport("ontologies/events.owl.ttl");

        try (QueryExecution qexec = report.run(report.loadQuery("sparql/nodeaths.sparql"))) {
            ResultSetFormatter.out(qexec.execSelect());
        }

        try (QueryExecution qexec = report.run(report.loadQuery("sparql/deaths.sparql"))) {
            ResultSetFormatter.out(qexec.execSelect());
        }
    }
}
