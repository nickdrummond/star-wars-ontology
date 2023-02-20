package com.nickd.sw.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class JenaHelper {

    public Model getModelFor(final String location) {
        long t1 = System.currentTimeMillis();

        Model model = RDFDataMgr.loadModel(location, Lang.TURTLE);
        File onts = new File("ontologies/");
        // hack load all ontologies/ - TODO use imports
        Arrays.stream(Objects.requireNonNull(onts.listFiles())).filter(f -> f.toString().endsWith(".ttl")).forEach(f -> {
            Model m = RDFDataMgr.loadModel(f.getAbsolutePath(), Lang.TURTLE);
            model.add(m);
        });

        long t2 = System.currentTimeMillis();
        System.out.println("loaded in = " + (t2 - t1));

        return model;
    }
}
