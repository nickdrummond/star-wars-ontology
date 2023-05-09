package com.nickd.sw.report;

import com.nickd.sw.util.JenaHelper;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;

import java.io.IOException;
import java.util.Map;

/**
 * Transform "x type (locatedIn some XYZ)" into "x locatedIn xyz; xyz type XYZ"
 * Using SPARQL
 * Note - requires that "Event -> locatedIn some Place" be removed before run.
 */
public class LocatedInSparqlReport {

    public static void main(String args[]) throws IOException {

        final SparqlReport report = new SparqlReport("ontologies/events.owl.ttl");

        events(report);

//        update(report);
    }

    private static void events(final SparqlReport report) throws IOException {
        final Query query = report.loadQuery("sparql/locatedIn.sparql");

        try (QueryExecution qexec = report.run(query)) {
            ResultSet qresults = qexec.execSelect();
            ResultSetFormatter.out(qresults);
            System.out.println(qresults.getRowNumber() + " results");
        }
    }

//    private static void update(final SparqlReport report) throws IOException {
//        final Query query = report.loadQuery("sparql/updateAnonLocations.sparql");
//
//        try (QueryExecution qexec = report.run(query)) {
//            Model model = qexec.execConstruct();
//            new JenaHelper().saveModel(model, System.out);
//        }
//    }

}
