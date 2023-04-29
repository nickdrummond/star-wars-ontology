package com.nickd.sw.util;

import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class MosParser {

    private final ManchesterOWLSyntaxClassExpressionParser mos;
    private final BidirectionalShortFormProviderAdapter cache;

    public MosParser(final Helper helper) {
        SimpleShortFormProvider sfp = new SimpleShortFormProvider();
        cache = new BidirectionalShortFormProviderAdapter(sfp);
        helper.ont.getSignature(Imports.INCLUDED).forEach(cache::add);
        ShortFormEntityChecker checker = new ShortFormEntityChecker(cache);
        mos = new ManchesterOWLSyntaxClassExpressionParser(helper.df, checker);
    }

    public OWLClassExpression parseCls(String s) {
        return mos.parse(s);
    }

    public String renderEntity(OWLEntity e) { return cache.getShortForm(e); }
}
