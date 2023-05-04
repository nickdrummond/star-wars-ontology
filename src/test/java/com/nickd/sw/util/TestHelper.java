package com.nickd.sw.util;

import junit.extensions.TestSetup;
import junit.framework.Test;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxInlineAxiomParser;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.*;
import java.io.StringWriter;
import java.util.Set;
import java.util.stream.Stream;

public class TestHelper extends TestSetup {

    private final Helper helper;

    private final ManchesterOWLSyntaxClassExpressionParser mos;
    private final SimpleShortFormProvider sfp;
    private final ShortFormEntityChecker checker;

    public TestHelper(Test test, String iri, OWLOntologyIRIMapper ontologyIRIMapper) throws OWLOntologyCreationException {
        super(test);
        helper = new Helper(iri, ontologyIRIMapper);
        sfp = new SimpleShortFormProvider();
        BidirectionalShortFormProviderAdapter cache = new BidirectionalShortFormProviderAdapter(sfp);
        helper.ont.getSignature(Imports.INCLUDED).forEach(cache::add);
        checker = new ShortFormEntityChecker(cache);
        mos = new ManchesterOWLSyntaxClassExpressionParser(df(), checker);
    }

    public String render (OWLEntity entity) {
        return sfp.getShortForm(entity);
    }

    public OWLEntity check(String name) {
        return checker.getOWLObjectProperty(name);
    }

    public OWLClassExpression mos(String s) {
        return mos.parse(s);
    }

    public OWLNamedIndividual ind(String s) {
        return helper.ind(s);
    }

    public OWLObjectProperty prop(String s) {
        return helper.prop(s);
    }

    public OWLClass cls(String s) { return helper.cls(s);}

    public void clearReasoner() { helper.clearReasoner(); }

    public void classify() { helper.classify(); }

    public OWLDataFactory df() { return helper.df ;}

    public OWLReasoner r() {return helper.r;}

    public long timeToClassify() {
        return helper.timeToClassify;
    }

    public long timeToLoad() {
        return helper.timeToLoad;
    }

    public OWLOntology ont() {
        return helper.ont;
    }

    public Set<OWLOntology> onts() {
        return helper.mngr.getOntologies();
    }

    public <T extends OWLAxiom> Stream<T> getAxioms(AxiomType<T> axiomType) {
        return helper.ont.getAxioms(axiomType, true).stream();
    }

    public String render(OWLObject o ) {
        StringWriter w = new StringWriter();
        o.accept(new ManchesterOWLSyntaxObjectRenderer(w, sfp));
        return w.toString();
    }
}

