package com.nickd.sw.report;

import com.nickd.sw.util.JenaHelper;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class SparqlReport {

    private static InfModel rdfsInfModel;

    public SparqlReport(final String ontology) {
        Model model = new JenaHelper().getModelWithImportsFor(ontology);

        // Try full blown OWL reasoning first - too slow
        // Transitive r does not work for instances of subclasses
        // RDFS Reasoner works for inferred instances. No SPARQL1.1 "+" needed
        // https://jena.apache.org/documentation/inference/
        Reasoner rdfsReasoner = ReasonerRegistry.getRDFSReasoner();
        rdfsInfModel = ModelFactory.createInfModel(rdfsReasoner, model);
    }

    public Query loadQuery(String s) throws IOException {
        Path path = Paths.get(s);
        String queryString = Files.lines(path).collect(Collectors.joining(System.lineSeparator()));
        return QueryFactory.create(queryString) ;
    }

    public QueryExecution run(Query query) {
        return QueryExecutionFactory.create(query, rdfsInfModel);
    }

    public QueryExecution runWithBindings(Query query, Map<String, String> bindings) {
        QuerySolutionMap initialBinding = new QuerySolutionMap();
        bindings.forEach((key, value) -> initialBinding.add(key, getResource(query, value)));
        return QueryExecutionFactory.create(query, rdfsInfModel, initialBinding);
    }

    private Resource getResource(Query query, String prefixedName) {
        String[] pre = prefixedName.split(":");
        String uri = query.getPrefix(pre[0]);
        return rdfsInfModel.getResource(uri + pre[1]);
    }
}
