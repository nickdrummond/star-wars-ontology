package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.*;

public abstract class AbstractParseMatcher {
    public OWLNamedIndividual getIndividual() {
        throw new RuntimeException("Not an individual matcher");
    }

    public OWLClass getOWLClass() {
        throw new RuntimeException("Not a class matcher");
    }

    public OWLClassExpression getOWLClassExpression() {
        throw new RuntimeException("Not a class expression matcher");
    }

    public OWLObjectProperty getObjectProperty() {
        throw new RuntimeException("Not an object property matcher");
    }

    public OWLDataProperty getDataProperty() {
        throw new RuntimeException("Not a data property matcher");
    }

    public OWLLiteral getLiteral() {
        throw new RuntimeException("Not a literal matcher");
    }

    public void check(MyTokenizer tokenizer, OWLEntityChecker checker, OWLDataFactory df) throws ParserException {
    }
}
