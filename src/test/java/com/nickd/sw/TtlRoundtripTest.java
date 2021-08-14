package com.nickd.sw;

import junit.framework.TestCase;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.net.URISyntaxException;

public class TtlRoundtripTest extends TestCase {

    public void testRioCorruptsOutput () throws OWLOntologyCreationException, OWLOntologyStorageException, URISyntaxException {
        OWLOntologyManager mngr = new OWLManager().get();
        File a = new File(getClass().getClassLoader().getResource("a.owl.ttl").toURI());
        OWLDocumentFormat format = new RioTurtleDocumentFormat();
        OWLOntologyDocumentSource docSource = new FileDocumentSource(a, format);
        OWLOntology ontA = mngr.loadOntologyFromOntologyDocument(docSource);
        ontA.saveOntology(format, System.out);
    }
}
