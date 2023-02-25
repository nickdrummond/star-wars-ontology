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
        OntModelSpec spec = OntModelSpec.OWL_MEM;
        spec.setDocumentManager(dm);
        Model model = ModelFactory.createOntologyModel(spec);
        model.read(location);

        long t2 = System.currentTimeMillis();
        System.out.println("loaded in " + (t2 - t1) + "ms");

        return model;
    }
}
