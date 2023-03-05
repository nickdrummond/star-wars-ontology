package com.nickd.sw.util;

import openllet.owlapi.PelletReasonerFactory;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.LocationMapper;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class JenaHelper {

    public Model getModelFor(final String location) {
        long t1 = System.currentTimeMillis();

        OntDocumentManager dm = new OntDocumentManager("ont-policy.rdf");
        dm.setReadFailureHandler((url, m, e) -> System.err.println("Failed to load " + url + e.getMessage()));
        LocationMapper locManager = dm.getFileManager().getLocationMapper();
        locManager.addAltPrefix("https://nickdrummond.github.io/star-wars-ontology/ontologies/", "file:ontologies/");

        OntModelSpec spec = OntModelSpec.OWL_MEM;
        spec.setDocumentManager(dm);
        OntModel model = ModelFactory.createOntologyModel(spec);
        model.read(location);

        long t2 = System.currentTimeMillis();

        listImports(model);
        System.out.println("loaded in " + (t2 - t1) + "ms");

        return model;
    }

    private void listImports(OntModel model) {
        model.getImportModelMaker().listModels().forEachRemaining(mURI -> {
            OntDocumentManager dm = model.getDocumentManager();
            String alt = dm.getFileManager().getLocationMapper().altMapping(mURI);

            OntModel m = dm.getOntology(mURI, OntModelSpec.OWL_MEM);
            if (m != null) {
                System.out.println("import = " + mURI + " from " + alt);
            }
            else {
                System.err.println("Cannot find model for " + mURI);
            }
        });
    }
}
