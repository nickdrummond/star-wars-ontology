package com.nickd.sw.report;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSetFormatter;

import java.io.IOException;

public class ParticipantsSparqlReport {

    public static void main(String args[]) throws IOException {

        final SparqlReport report = new SparqlReport("ontologies/events.owl.ttl");

        final Query query = report.loadQuery("sparql/participants.sparql");

        try (QueryExecution qexec = report.run(query)) {
            ResultSetFormatter.outputAsCSV(qexec.execSelect());
        }
    }
}
