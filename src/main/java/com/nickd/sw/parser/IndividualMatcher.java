package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Collections;

public class IndividualMatcher extends AbstractParseMatcher {

    private OWLNamedIndividual ind;

    @Override
    public OWLNamedIndividual getIndividual() {
        return ind;
    }

    @Override
    public void check(MyTokenizer tokenizer, OWLEntityChecker checker, OWLDataFactory df) throws ParserException {
        OWLNamedIndividual ind = checker.getOWLIndividual(tokenizer.consumeNext());
        if (ind == null) {
            int pointer = tokenizer.getPointer();

            throw new ParserException(tokenizer.tokens(), pointer, 0, pointer, false, false, false, false, true, false, false, false, Collections.emptySet());
        }
        this.ind = ind;
    }
}
