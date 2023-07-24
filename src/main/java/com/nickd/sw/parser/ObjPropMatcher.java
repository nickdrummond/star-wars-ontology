package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Collections;

public class ObjPropMatcher extends AbstractParseMatcher {
    private OWLObjectProperty prop;

    @Override
    public OWLObjectProperty getObjectProperty() {
        return prop;
    }

    @Override
    public void check(MyTokenizer tokenizer, OWLEntityChecker checker, OWLDataFactory df) throws ParserException {
        OWLObjectProperty prop = checker.getOWLObjectProperty(tokenizer.consumeNext());
        if (prop == null) {
            int pointer = tokenizer.getPointer();
            throw new ParserException(tokenizer.tokens(), pointer, 0, pointer, false, false, true, false, false, false, false, false, Collections.emptySet());
        }
        this.prop = prop;
    }
}
