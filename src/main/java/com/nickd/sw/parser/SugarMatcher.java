package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Set;

public class SugarMatcher extends AbstractParseMatcher {
    private final String syntax;

    public SugarMatcher(String syntax) {
        this.syntax = syntax;
    }

    @Override
    public void check(MyTokenizer tokenizer, OWLEntityChecker checker, OWLDataFactory df) throws ParserException {
        if (!tokenizer.consumeNext().equalsIgnoreCase(syntax)) {
            int pointer = tokenizer.getPointer();
            throw new ParserException(tokenizer.tokens(), pointer, 0, pointer, false, false, false, false, false, false, false, false, Set.of(syntax));
        }
    }
}
