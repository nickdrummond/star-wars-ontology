package com.nickd.sw.util;

import openllet.owlapi.OWLHelper;
import openllet.owlapi.OpenlletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Helper {

    public static String BASE = "https://nickdrummond.github.io/star-wars-ontology/ontologies";

    public OWLOntologyManager mngr;
    public OWLOntology ont;
    public OWLDataFactory df;
    public OWLReasoner r;

    public long timeToLoad;
    public long timeToClassify;

    private Set<OWLOntology> changedOntologies = new HashSet<>();

    public Helper(String iri, OWLOntologyIRIMapper ontologyIRIMapper) throws OWLOntologyCreationException {
        mngr = new OWLManager().get();
        mngr.setIRIMappers(Collections.singleton(ontologyIRIMapper));
        mngr.addOntologyChangeListener(list -> list.forEach(c -> changedOntologies.add(c.getOntology())));

        long start = System.currentTimeMillis();
        ont = mngr.loadOntology(IRI.create(iri));
        df = mngr.getOWLDataFactory();
        timeToLoad = System.currentTimeMillis() - start;
        System.out.println("Loaded in " + timeToLoad + "ms");
    }

    public OWLNamedIndividual ind(String s) {
        return df.getOWLNamedIndividual(IRI.create(BASE + "#" + s));
    }

    public OWLObjectProperty prop(String s) {
        return df.getOWLObjectProperty(IRI.create(BASE + "#" + s));
    }

    public OWLDataProperty dataProp(String s) {
        return df.getOWLDataProperty(IRI.create(BASE + "#" + s));
    }

    public OWLClass cls(String s) {
        return df.getOWLClass(IRI.create(BASE + "#" + s));
    }



    public void classify() {

        final OWLHelper h = OWLHelper.createLightHelper(OpenlletReasonerFactory.getInstance().createReasoner(ont));

        long start = System.currentTimeMillis();

        r = h.getReasoner();

        timeToClassify = System.currentTimeMillis() - start;
        System.out.println("Classified in " + timeToClassify + "ms");
    }

    public void saveChanged() throws OWLOntologyStorageException {
        for (OWLOntology o : changedOntologies) {
            OWLDocumentFormat format = o.getOWLOntologyManager().getOntologyFormat(o);
            if (format instanceof RioTurtleDocumentFormat) {
                TurtleDocumentFormat ttl = new TurtleDocumentFormat();
                ttl.copyPrefixesFrom((RioTurtleDocumentFormat)format);
                o.getOWLOntologyManager().setOntologyFormat(o, ttl);
            }
            mngr.saveOntology(o);
        }
    }
}
