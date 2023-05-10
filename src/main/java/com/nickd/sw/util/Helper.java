package com.nickd.sw.util;

import openllet.owlapi.OWLHelper;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public void clearReasoner() {
        r.dispose();
        r = null;
    }

    public void classify() {

        final OWLHelper h = OWLHelper.createLightHelper(OpenlletReasonerFactory.getInstance().createReasoner(ont));

//        long start = System.nanoTime();

        r = h.getReasoner();
        // analogue to Protege "Classify"
        r.precomputeInferences(InferenceType.CLASS_HIERARCHY);
        r.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
        r.precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
        r.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
        r.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        r.precomputeInferences(InferenceType.SAME_INDIVIDUAL);

//        timeToClassify = System.nanoTime() - start;
//        System.out.println("Classified in " + TimeUnit.NANOSECONDS.toMillis(timeToClassify) + "ms");
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

    public void save(String location) throws OWLOntologyStorageException {

        File base = new File(location);
        System.out.println("Saving ontologies to " + base.getAbsolutePath());
        if (!base.exists()) {
            if (!base.mkdir()) {
                throw new OWLOntologyStorageException("Could not create compilation directory: " + base);
            }
        }

        for (OWLOntology o : mngr.getOntologies()) {
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
}
