package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;

public class ClassExpressionMatcher extends AbstractParseMatcher {
    private OWLClassExpression expr;

    @Override
    public OWLClassExpression getOWLClassExpression() {
        return expr; // TODO
    }

    @Override
    public void check(MyTokenizer tokenizer, OWLEntityChecker checker, OWLDataFactory df) throws ParserException {
        ManchesterOWLSyntaxClassExpressionParser mos = new ManchesterOWLSyntaxClassExpressionParser(df, checker);
        try {
            this.expr = mos.parse(tokenizer.remainder());
        } catch (ParserException e) {
            throw new ParserException(
                    e.getTokenSequence(),
                    tokenizer.getPointer() + e.getStartPos(),
                    e.getLineNumber(),
                    tokenizer.getPointer() + e.getColumnNumber(),
                    e.isOntologyNameExpected(),
                    e.isClassNameExpected(),
                    e.isObjectPropertyNameExpected(),
                    e.isDataPropertyNameExpected(),
                    e.isIndividualNameExpected(),
                    e.isDatatypeNameExpected(),
                    e.isAnnotationPropertyNameExpected(),
                    e.isIntegerExpected(),
                    e.getExpectedKeywords());
        }
    }
}
