package com.nickd.sw.report;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSetFormatter;

import java.io.IOException;

public class PlanetsSparqlReport {

    public static void main(String args[]) throws IOException {

        final SparqlReport report = new SparqlReport("ontologies/star-wars.owl.ttl");

        final Query query = report.loadQuery("sparql/planets.sparql");

        try (QueryExecution qexec = report.run(query)) {
            ResultSetFormatter.out(qexec.execSelect());
        }
    }
}
