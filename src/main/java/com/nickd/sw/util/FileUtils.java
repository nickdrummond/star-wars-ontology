package com.nickd.sw.util;

import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileUtils {

    public static void save(OWLOntologyManager mngr, String location) throws OWLOntologyStorageException {

        File base = ensureDirectoryExists(location);

        for (OWLOntology o : mngr.getOntologies()) {
            save(mngr, o, base);
        }
    }

    public static File ensureDirectoryExists(String location) throws OWLOntologyStorageException {
        File base = new File(location);
        System.out.println("Saving ontologies to " + base.getAbsolutePath());
        if (!base.exists()) {
            if (!base.mkdir()) {
                throw new OWLOntologyStorageException("Could not create compilation directory: " + base);
            }
        }
        return base;
    }

    public static void save(OWLOntologyManager mngr, OWLOntology o, File base) throws OWLOntologyStorageException {
        OWLDocumentFormat format = o.getOWLOntologyManager().getOntologyFormat(o);
        if (format instanceof RioTurtleDocumentFormat) {
            TurtleDocumentFormat ttl = new TurtleDocumentFormat();
            ttl.copyPrefixesFrom((RioTurtleDocumentFormat)format);
            o.getOWLOntologyManager().setOntologyFormat(o, ttl);
        }
        try {
            IRI iri = o.getOntologyID().getOntologyIRI().orElseThrow();
            File f = new File(base, iri.getShortForm());
            System.out.println("saving..." + f.getAbsolutePath());
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            mngr.saveOntology(o, fileOutputStream);
        } catch (FileNotFoundException e) {
            throw new OWLOntologyStorageException(e);
        }
    }
}
