package com.nickd.sw.util;

import openllet.owlapi.OWLHelper;
import openllet.owlapi.OpenlletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.nickd.sw.util.MyStringUtils.singleLine;

public class Helper {

    @FunctionalInterface
    public interface LoadsOntology {
        OWLOntology apply(OWLOntologyManager mngr) throws OWLOntologyCreationException;
    }

    public static String BASE = "https://nickdrummond.github.io/star-wars-ontology/ontologies";
    public static String UTIL_BASE = "https://nickdrummond.github.io/star-wars-ontology/util";

    public OWLOntologyManager mngr;
    public OWLOntology ont;
    public OWLDataFactory df;
    public OWLReasoner r;
    public OWLReasoner told;

    private final SimpleShortFormProvider sfp;
    private final ShortFormEntityChecker checker;
    private final ManchesterOWLSyntaxClassExpressionParser mos;

    public long timeToLoad;
    public long timeToClassify;

    private final Set<OWLOntology> changedOntologies = new HashSet<>();

    public Helper(final File ontFile) throws OWLOntologyCreationException {
        this(ontFile, new SameDirectoryIRIMapper(ontFile));
    }

    public Helper(final File ontFile, final OWLOntologyIRIMapper ontologyIRIMapper) throws OWLOntologyCreationException {
        this(mngr -> mngr.loadOntologyFromOntologyDocument(ontFile), ontologyIRIMapper);
    }

    public Helper(final String iri, final OWLOntologyIRIMapper ontologyIRIMapper) throws OWLOntologyCreationException {
        this(mngr -> mngr.loadOntology(IRI.create(iri)), ontologyIRIMapper);
    }

    public Helper(LoadsOntology loadsOntology, OWLOntologyIRIMapper ontologyIRIMapper) throws OWLOntologyCreationException {
        mngr = new OWLManager().get();
        mngr.setIRIMappers(Collections.singleton(ontologyIRIMapper));
        mngr.addOntologyChangeListener(list -> list.forEach(c -> changedOntologies.add(c.getOntology())));

        long start = System.currentTimeMillis();
        ont = loadsOntology.apply(mngr);
        timeToLoad = System.currentTimeMillis() - start;

        df = mngr.getOWLDataFactory();
        sfp = new SimpleShortFormProvider();
        BidirectionalShortFormProviderAdapter cache = new BidirectionalShortFormProviderAdapter(sfp);
        ont.getSignature(Imports.INCLUDED).forEach(cache::add);
        checker = new ShortFormEntityChecker(cache);
        mos = new ManchesterOWLSyntaxClassExpressionParser(df, checker);
        told = new StructuralReasonerFactory().createNonBufferingReasoner(ont, new SimpleConfiguration());

        System.out.println("Loaded in " + timeToLoad + "ms");
    }

    private static IRI makeIRI(String s) {
        return IRI.create(BASE + "#" + s);
    }

    public OWLNamedIndividual ind(String s) {
        return df.getOWLNamedIndividual(makeIRI(s));
    }

    public OWLObjectProperty prop(String s) {
        return df.getOWLObjectProperty(makeIRI(s));
    }

    public OWLDataProperty dataProp(String s) {
        return df.getOWLDataProperty(makeIRI(s));
    }

    public OWLClass cls(String s) {
        return df.getOWLClass(makeIRI(s));
    }

    public OWLAnnotationProperty annotProp(String s, String base) {return df.getOWLAnnotationProperty(IRI.create(base + "#" + s)); }

    public OWLOntology ont(String s) { return mngr.getOntology(IRI.create(BASE + "/" + s)); }

    public String render (OWLEntity entity) {
        return sfp.getShortForm(entity);
    }

    public OWLEntity check(String name) {
        return checker.getOWLObjectProperty(name);
    }

    public OWLClassExpression mos(String s) {
        return mos.parse(s);
    }

    public String render(OWLObject o) {
        return render(o, true);
    }

    public String render(OWLObject o, boolean singleLine) {
        StringWriter w = new StringWriter();
        o.accept(new MyMOSObjectRenderer(w, sfp));
        return singleLine ? singleLine(w.toString()) : w.toString();
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
        FileUtils.save(mngr, location);
    }

    public OWLEntity entityForIRI(IRI iri) {
        Set<OWLEntity> matches = ont.getEntitiesInSignature(iri, Imports.INCLUDED);
        if (matches.isEmpty()) {
            throw new RuntimeException("Cannot find entity for " + iri);
        }
        return matches.iterator().next();
    }
}
