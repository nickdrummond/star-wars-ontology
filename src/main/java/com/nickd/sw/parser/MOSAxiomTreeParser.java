package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Set;

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
                        ParseTree.child() // Property Characteristics
                                .expectEither(
                                        ParseTree.child().expectPrefixKeyword(FUNCTIONAL).expectEither(
                                                ParseTree.child().expectObjectProperty("p").create(e -> df.getOWLFunctionalObjectPropertyAxiom(e.objProp("p"))),
                                                ParseTree.child().expectDataProperty("p").create(e -> df.getOWLFunctionalDataPropertyAxiom(e.dataProp("p")))
                                        ),
                                        ParseTree.child().expectPrefixKeyword(TRANSITIVE)
                                                .expectObjectProperty("p").create(e -> df.getOWLTransitiveObjectPropertyAxiom(e.objProp("p"))),
                                        ParseTree.child().expectPrefixKeyword(SYMMETRIC)
                                                .expectObjectProperty("p").create(e -> df.getOWLSymmetricObjectPropertyAxiom(e.objProp("p"))),
                                        ParseTree.child().expectPrefixKeyword(ASYMMETRIC)
                                                .expectObjectProperty("p").create(e -> df.getOWLAsymmetricObjectPropertyAxiom(e.objProp("p"))),
                                        ParseTree.child().expectPrefixKeyword(REFLEXIVE)
                                                .expectObjectProperty("p").create(e -> df.getOWLReflexiveObjectPropertyAxiom(e.objProp("p"))),
                                        ParseTree.child().expectPrefixKeyword(IRREFLEXIVE)
                                                .expectObjectProperty("p").create(e -> df.getOWLIrreflexiveObjectPropertyAxiom(e.objProp("p")))
                                        ),
                        ParseTree.child() // Individual axioms
                                .expectIndividual("a")
                                .expectEither(
                                        ParseTree.child()
                                                .expectKeyword(TYPE)
                                                .expectClassExpression("A")
                                                .create(e -> df.getOWLClassAssertionAxiom(e.clsExpr("A"), e.ind("a"))),
                                        ParseTree.child() // expected path
                                                .expectObjectProperty("p")
                                                .expectIndividual("b")
                                                .create(e -> df.getOWLObjectPropertyAssertionAxiom(e.objProp("p"), e.ind("a"), e.ind("b"))),
                                        ParseTree.child()
                                                .expectDataProperty("p")
                                                .expectLiteral("v")
                                                .create(e -> df.getOWLDataPropertyAssertionAxiom(e.dataProp("p"), e.ind("a"), e.lit("v")))
                                )
                );
        return builder.getAxiom();
    }
}
