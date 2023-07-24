package com.nickd.sw.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.*;

@RunWith(MockitoJUnitRunner.class)
public class ParseTreeTest {

    @Mock
    private OWLEntityChecker checker;

    private OWLDataFactoryImpl df;

    @Before
    public void createParser() {
        df = new OWLDataFactoryImpl();
    }

    @Test
    public void testSingleTrunk() {
        String s = "a p b";

        OWLAxiom expected = df.getOWLObjectPropertyAssertionAxiom(objP("p"), ind("a"), ind("b"));

        ParseTree builder = new ParseTree(s, checker, df)
                .expectIndividual("a")
                .expectObjectProperty("p")
                .expectIndividual("b")
                .create(e -> df.getOWLObjectPropertyAssertionAxiom(e.objProp("p"), e.ind("a"), e.ind("b")));

        assertEquals(expected, builder.getAxiom());
    }

    @Test
    public void testBranches() {
        ind("a");
        objP("p");
        ind("b");
        String s = "a p b";

        OWLAxiom expected = df.getOWLObjectPropertyAssertionAxiom(objP("p"), ind("a"), ind("b"));

        ParseTree builder = new ParseTree(s, checker, df)
                .expectIndividual("a")
                .expectEither(
                        ParseTree.child()
                                .expectKeyword(TYPE)
                                .expectClass("A")
                                .create(e -> df.getOWLClassAssertionAxiom(e.cls("A"), e.ind("a"))),
                        ParseTree.child() // expected path
                                .expectObjectProperty("p")
                                .expectIndividual("b")
                                .create(e -> df.getOWLObjectPropertyAssertionAxiom(e.objProp("p"), e.ind("a"), e.ind("b")))
                );

        assertEquals(expected, builder.getAxiom());
    }

    @Test
    public void testNestedBranches() {
        ind("a");
        objP("p");
        ind("b");
        String s = "a p b";

        OWLAxiom expected = df.getOWLObjectPropertyAssertionAxiom(objP("p"), ind("a"), ind("b"));

        ParseTree builder = new ParseTree(s, checker, df)
                .expectEither(
                        ParseTree.child() // Property Characteristics
                                .expectEither(
                                        ParseTree.child().expectKeyword(FUNCTIONAL).expectEither(
                                                ParseTree.child().expectObjectProperty("p").create(e -> df.getOWLFunctionalObjectPropertyAxiom(e.objProp("p"))),
                                                ParseTree.child().expectDataProperty("p").create(e -> df.getOWLFunctionalDataPropertyAxiom(e.dataProp("p")))
                                        ),
                                        ParseTree.child().expectKeyword(TRANSITIVE)
                                                .expectObjectProperty("p").create(e -> df.getOWLTransitiveObjectPropertyAxiom(e.objProp("p")))
                                ),
                        ParseTree.child() // Individual axioms
                                .expectIndividual("a")
                                .expectEither(
                                        ParseTree.child()
                                                .expectKeyword(TYPE)
                                                .expectClass("A")
                                                .create(e -> df.getOWLClassAssertionAxiom(e.cls("A"), e.ind("a"))),
                                        ParseTree.child() // expected path
                                                .expectObjectProperty("p")
                                                .expectIndividual("b")
                                                .create(e -> df.getOWLObjectPropertyAssertionAxiom(e.objProp("p"), e.ind("a"), e.ind("b")))
                                )
                );
        assertEquals(expected, builder.getAxiom());
    }

    private OWLNamedIndividual ind(String name) {
        OWLNamedIndividual ind = df.getOWLNamedIndividual(name);
        when(checker.getOWLIndividual(name)).thenReturn(ind);
        return ind;
    }

    private OWLObjectProperty objP(String name) {
        OWLObjectProperty prop = df.getOWLObjectProperty(name);
        when(checker.getOWLObjectProperty(name)).thenReturn(prop);
        return prop;
    }
}