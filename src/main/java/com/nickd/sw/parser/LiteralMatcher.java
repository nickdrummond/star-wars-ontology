package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import java.util.Collections;

public class LiteralMatcher extends AbstractParseMatcher {

    private OWLLiteral lit;

    @Override
    public OWLLiteral getLiteral() {
        return lit;
    }

    @Override
    public void check(MyTokenizer tokenizer, OWLEntityChecker checker, OWLDataFactory df) throws ParserException {
        OWLLiteral lit = new OWLLiteralImpl(tokenizer.consumeNext(), null, null);
        if (lit == null) {
            int pointer = tokenizer.getPointer();
            throw new ParserException(tokenizer.tokens(), pointer, 0, pointer, false, false, false, false, false, false, false, true, Collections.emptySet());
        }
        this.lit = lit;
    }
}
