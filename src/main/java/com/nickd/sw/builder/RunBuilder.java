package com.nickd.sw.builder;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;

public class RunBuilder {

    public static final String DEFAULT_OWL_TO_LOAD = "ontologies/all.owl.ttl";

    public static void main(String[] args) {

        File file = new File(DEFAULT_OWL_TO_LOAD);
        if (args.length == 1) {
            file = new File(args[0]);
        }
        try {
            if (file.exists()) {
                new BuilderController(file, System.out).run(System.in);
            }
            else {
                System.err.println("Cannot find " + file);
            }
        } catch (OWLOntologyCreationException e) {
            System.err.println("Could not load ontologies at " + file);
        }
    }
}
