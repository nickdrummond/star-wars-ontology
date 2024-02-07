package com.nickd.sw;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.io.File;

/**
 * Protege can create odd artifacts.
 * This just loads and saves the ontologies in ttl format.
 */
public class Normalise {

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Helper helper = new Helper(new File("ontologies/all.owl.ttl"));
        helper.save("ontologies");
    }
}
