package com.nickd.sw.util;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.TYPE;

@RunWith(MockitoJUnitRunner.class)
public class ManchesterOWLSyntaxAxiomParserTest extends TestCase {

    @Mock
    private OWLEntityChecker checker;

    private ManchesterOWLSyntaxAxiomParser parser;
    private OWLDataFactoryImpl df;

    @Before
    public void createParser() {
        df = new OWLDataFactoryImpl();
        parser = new ManchesterOWLSyntaxAxiomParser(df, checker);
    }

    @Test(expected = ParserException.class)
    public void testParseEmptyString() {
        parser.parse("");
    }

    @Test
    public void wellFormedPropertyCharacteristic() {
        String s = "Transitive: p";
        OWLAxiom expected = df.getOWLTransitiveObjectPropertyAxiom(objP("p"));

        assertEquals(expected, parser.parsePropertyCharacteristicAxiom(s));
        assertEquals(expected, parser.parse(s));
    }

    @Test
    public void wellFormedClassAssertionAxiomNamedClass() {
        String s = "a Type A";
        OWLAxiom expected = df.getOWLClassAssertionAxiom(cls("A"), ind("a"));

        assertEquals(expected, parser.parseIndividualAxiom(s).get());
        assertEquals(expected, parser.parse(s));
    }

    @Test
    public void wellFormedClassAssertionAxiomAnonClass() {
        String s = "a Type (p some A)";
        OWLAxiom expected = df.getOWLClassAssertionAxiom(
                df.getOWLObjectSomeValuesFrom(objP("p"), cls("A")),
                ind("a"));

        assertEquals(expected, parser.parseIndividualAxiom(s).get());
        assertEquals(expected, parser.parse(s));
    }

    @Test
    public void wellFormedPropertyAssertion() {
        String s = "a p b";
        OWLAxiom expected = df.getOWLObjectPropertyAssertionAxiom(objP("p"), ind("a"), ind("b"));

        assertEquals(expected, parser.parseIndividualAxiom(s).get());
        assertEquals(expected, parser.parse(s));
    }

    @Test
    public void missingPropertyOrKeywordInIndividualAxiom() {
        try {
            ind("a");
            parser.parseIndividualAxiom("a");
            fail("Expected exception not thrown");
        }
        catch (ParserException e) {
            assertTrue(e.isObjectPropertyNameExpected());
            assertTrue(e.isDataPropertyNameExpected());
            assertTrue(e.getExpectedKeywords().contains(TYPE.toString()));
            assertEquals(1, e.getStartPos());
        }
    }

    @Test
    public void incorrectKeywordInIndividualAxiom() {
        try {
            ind("a");
            parser.parseIndividualAxiom("a Transitive");
            fail("Expected exception not thrown");
        }
        catch (ParserException e) {
            assertTrue(e.getExpectedKeywords().contains(TYPE.toString()));
            assertEquals(2, e.getStartPos());
        }
    }

    @Test
    public void unknownPropertyInIndividualAxiom() {
        try {
            ind("a");
            parser.parseIndividualAxiom("a p");
            fail("Expected exception not thrown");
        }
        catch (ParserException e) {
            assertTrue(e.isObjectPropertyNameExpected());
            assertTrue(e.isDataPropertyNameExpected());
            assertTrue(e.getExpectedKeywords().isEmpty());
            assertEquals(2, e.getStartPos());
        }
    }

    @Test
    public void missingObjectInIndividualAxiom() {
        try {
            ind("a");
            objP("p");
            parser.parseIndividualAxiom("a p");
            fail("Expected exception not thrown");
        }
        catch (ParserException e) {
            System.out.println(e.getMessage());
            assertTrue(e.isIndividualNameExpected());
            assertEquals(3, e.getStartPos());
        }
    }

    @Test
    public void wellFormedNegativePropertyAssertion() {
        String s = "not (a p b)";
        OWLAxiom expected = df.getOWLNegativeObjectPropertyAssertionAxiom(objP("p"), ind("a"), ind("b"));

        assertEquals(expected, parser.parseNegatedPropertyAssertion(s));
        assertEquals(expected, parser.parse(s));
    }

    @Test
    public void wellFormedSubclassOfAssertion() {
        String s = "A SubClassOf B";
        OWLAxiom expected = df.getOWLSubClassOfAxiom(cls("A"), cls("B"));

        assertTrue(parser.parseIndividualAxiom(s).isEmpty()); // not an individual assertion
        assertEquals(expected, parser.parseClassAxiom(s));
        assertEquals(expected, parser.parse(s));
    }

    @Test
    public void wellFormedObjectPropertyDomainAssertion() {
        String s = "p Domain A";
        OWLAxiom expected = df.getOWLObjectPropertyDomainAxiom(objP("p"), cls("A"));

        assertEquals(expected, parser.parseObjectPropertyAxiom(s));
        assertEquals(expected, parser.parse(s));
    }

    // TODO badly formed axioms

    private OWLClass cls(String name) {
        OWLClass cls = df.getOWLClass(name);
        when(checker.getOWLClass(name)).thenReturn(cls);
        return cls;
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