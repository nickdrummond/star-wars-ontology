package com.nickd.sw.report;

import org.apache.jena.query.*;

import java.io.IOException;
import java.util.Map;

public class EventsSparqlReport {

    public static void main(String args[]) throws IOException {

        final SparqlReport report = new SparqlReport("ontologies/events.owl.ttl");

        eventsByLocation(report, "sw:Outer_Rim");

        events(report);
    }

    private static void events(final SparqlReport report) throws IOException {
        final Query query = report.loadQuery("sparql/events.sparql");

        try (QueryExecution qexec = report.run(query)) {
            ResultSet qresults = qexec.execSelect();
            ResultSetFormatter.out(qresults);
            System.out.println(qresults.getRowNumber() + " results");
        }
    }

    private static void eventsByLocation(final SparqlReport report, final String generalLocation) throws IOException {

        final Query query = report.loadQuery("sparql/eventsByLocation.sparql");

        Map<String, String> bindings = Map.of("generalLocation", generalLocation);

        try (QueryExecution qexec = report.runWithBindings(query, bindings)) {
            ResultSet qresults = qexec.execSelect();

            System.out.println("Location: " + generalLocation);
            ResultSetFormatter.out(qresults);
            System.out.println(qresults.getRowNumber() + " results");
        }
    }
}
