package com.nickd.sw;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.rdf.api.RDFSyntax;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StyleTest extends TestCase {

    private static OWLOntologyManager mngr;
    private static OWLOntology ont;
    private static OWLDataFactory df;

    private static long t;

    // One time load and classification
    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(StyleTest.class)) {
            protected void setUp(  ) throws Exception {
                mngr = new OWLManager().get();
                File starwarsOwl = new File("star-wars.owl");
                long start = System.currentTimeMillis();
                ont = mngr.loadOntologyFromOntologyDocument(starwarsOwl);
                t = System.currentTimeMillis() - start;
                System.out.println("Loaded in " + t + "ms");
                df = mngr.getOWLDataFactory();
            }
            protected void tearDown(  ) throws Exception {
                // do your one-time tear down here!
            }
        };
        return setup;
    }


    public void testLoadsInLessThan2s() {
        assertTrue(t < 2000);
    }

    public void testIndividualsHaveReferences() {
        final OWLAnnotationProperty seeAlso = df.getOWLAnnotationProperty(RDFS.SEEALSO.toString());

        final Set<OWLNamedIndividual> inds = ont.getIndividualsInSignature().stream().filter(i ->
            ont.getAnnotationAssertionAxioms(i.getIRI()).stream().noneMatch(ann -> ann.getProperty().equals(seeAlso))
        ).collect(Collectors.toSet());

        assertTrue("Missing references: " + inds, inds.isEmpty());
    }
}