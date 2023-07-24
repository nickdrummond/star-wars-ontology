package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import static com.nickd.sw.parser.ParseTree.branch;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.*;

public class MOSAxiomTreeParser {

    private final OWLDataFactory df;
    private final OWLEntityChecker checker;

    public MOSAxiomTreeParser(OWLDataFactory df,
                              OWLEntityChecker checker) {
        this.df = df;
        this.checker = checker;
    }

    public OWLAxiom parse(String s) {
        ParseTree builder = new ParseTree(s, checker, df)
                .expectEither(
                        branch() // Property Characteristics
                                .expectEither(
                                        branch().expectPrefixKeyword(FUNCTIONAL).expectEither(
                                                branch()
                                                        .expectObjectProperty("p")
                                                        .create(e -> df.getOWLFunctionalObjectPropertyAxiom(e.objProp("p"))),
                                                branch()
                                                        .expectDataProperty("p")
                                                        .create(e -> df.getOWLFunctionalDataPropertyAxiom(e.dataProp("p")))
                                        ),
                                        branch()
                                                .expectPrefixKeyword(TRANSITIVE)
                                                .expectObjectProperty("p")
                                                .create(e -> df.getOWLTransitiveObjectPropertyAxiom(e.objProp("p"))),
                                        branch()
                                                .expectPrefixKeyword(SYMMETRIC)
                                                .expectObjectProperty("p")
                                                .create(e -> df.getOWLSymmetricObjectPropertyAxiom(e.objProp("p"))),
                                        branch()
                                                .expectPrefixKeyword(ASYMMETRIC)
                                                .expectObjectProperty("p")
                                                .create(e -> df.getOWLAsymmetricObjectPropertyAxiom(e.objProp("p"))),
                                        branch()
                                                .expectPrefixKeyword(REFLEXIVE)
                                                .expectObjectProperty("p")
                                                .create(e -> df.getOWLReflexiveObjectPropertyAxiom(e.objProp("p"))),
                                        branch()
                                                .expectPrefixKeyword(IRREFLEXIVE)
                                                .expectObjectProperty("p")
                                                .create(e -> df.getOWLIrreflexiveObjectPropertyAxiom(e.objProp("p")))
                                ),
                        branch() // Individual axioms
                                .expectIndividual("a")
                                .expectEither(
                                        branch()
                                                .expectKeyword(TYPE)
                                                .expectClassExpression("A")
                                                .create(e -> df.getOWLClassAssertionAxiom(e.clsExpr("A"), e.ind("a"))),
                                        branch() // expected path
                                                .expectObjectProperty("p")
                                                .expectIndividual("b")
                                                .create(e -> df.getOWLObjectPropertyAssertionAxiom(e.objProp("p"), e.ind("a"), e.ind("b"))),
                                        branch()
                                                .expectDataProperty("p").expectLiteral("v")
                                                .create(e -> df.getOWLDataPropertyAssertionAxiom(e.dataProp("p"), e.ind("a"), e.lit("v")))
                                ),
                        branch() // Negative individual axioms
                                .expectKeyword(NOT).expectKeyword(OPEN).expectIndividual("a")
                                .expectEither(
                                        branch() // expected path
                                                .expectObjectProperty("p").expectIndividual("b").expectKeyword(CLOSE)
                                                .create(e -> df.getOWLNegativeObjectPropertyAssertionAxiom(e.objProp("p"), e.ind("a"), e.ind("b"))),
                                        branch()
                                                .expectDataProperty("p").expectLiteral("v").expectKeyword(CLOSE)
                                                .create(e -> df.getOWLNegativeDataPropertyAssertionAxiom(e.dataProp("p"), e.ind("a"), e.lit("v")))
                                )
                );
        return builder.getAxiom();
    }
}
