package com.nickd.sw.util;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.*;


/**
 * Parse axioms in Manchester OWL Syntax as rendered by ManchesterOWLSyntaxObjectRenderer.
 * The Manchester OWL Syntax is specified as a frame-based rendering, but this gives
 * us axiom approximations.
 *
 * TODO annotations
 * TODO datatypes
 * TODO disjoints/different
 * TODO property chains
 * TODO GCIs
 *
 * TODO Make all ParserExceptions
 * TODO lookahead for hints of the type for Exceptions
 */
public class ManchesterOWLSyntaxAxiomParser {

    private final OWLDataFactory df;
    private final OWLEntityChecker checker;
    private final ManchesterOWLSyntaxClassExpressionParser classExpressionParser;

    public ManchesterOWLSyntaxAxiomParser(OWLDataFactory df,
                                          OWLEntityChecker checker) {
        this.df = df;
        this.checker = checker;
        this.classExpressionParser = new ManchesterOWLSyntaxClassExpressionParser(df, checker);
    }

    public OWLAxiom parse(String s) throws ParserException {
        s = s.trim();

        if (s.isEmpty()) {
            throw new IllegalArgumentException("Input is empty");
        }

        // try keyword leading axioms first
        int keywordDelimPosition = s.indexOf(":");

        if (keywordDelimPosition != -1) {
            String keyword = s.substring(0, keywordDelimPosition);
            String rest = s.substring(keywordDelimPosition + 1);
            return getKeywordAxiom(keyword, rest);
        }

        String first = first(s);
        String rest = rest(s);

        if (first != null && rest != null) {

            boolean negated = false;
            if (NOT.matches(first)) {
                negated = true;
                s = unbracket(rest);

                first = first(s);
                rest = rest(s);

                if (first != null && rest != null) {
                    return getIndividualAxiom(ind(first), rest, negated);
                }
                throw new IllegalArgumentException("Badly formed negation: \"" + s + "\"");
            }

            OWLNamedIndividual ind = ind(first);
            if (ind != null) {
                return getIndividualAxiom(ind, rest, negated);
            }

            OWLClass cls = cls(first);
            if (cls != null) {
                return getClassAxiom(cls, rest);
            }

            OWLObjectProperty objProp = objProp(first);
            if (objProp != null) {
                return getObjectPropertyAxiom(objProp, rest);
            }

            OWLDataProperty dataProp = dataProp(first);
            if (dataProp != null) {
                return getDataPropertyAxiom(dataProp, rest);
            }
        }
        throw new IllegalArgumentException("Unexpected input: \"" + s + "\"");
    }

    private OWLAxiom getKeywordAxiom(String token, String rest) {
        ManchesterOWLSyntax keyword = getSyntax(token);

        if (keyword == null) {
            throw new IllegalArgumentException("Unknown keyword in property characteristic: \"" + token + "\"");
        }

        switch (keyword) {
            case FUNCTIONAL:
                OWLObjectProperty objProp = objProp(rest);
                if (objProp != null) {
                    return df.getOWLFunctionalObjectPropertyAxiom(objProp);
                }
                else {
                    OWLDataProperty dataProp = dataProp(rest);
                    if (dataProp != null) {
                        return df.getOWLFunctionalDataPropertyAxiom(dataProp);
                    }
                }
            case TRANSITIVE:
                return df.getOWLTransitiveObjectPropertyAxiom(objProp(rest));
            case SYMMETRIC:
                return df.getOWLSymmetricObjectPropertyAxiom(objProp(rest));
            case ASYMMETRIC:
                return df.getOWLAsymmetricObjectPropertyAxiom(objProp(rest));
            case REFLEXIVE:
                return df.getOWLReflexiveObjectPropertyAxiom(objProp(rest));
            case IRREFLEXIVE:
                return df.getOWLIrreflexiveObjectPropertyAxiom(objProp(rest));
            default:
                throw new IllegalArgumentException("Unexpected keyword in property characteristic: \"" + token + "\"");
        }
    }

    private OWLAxiom getClassAxiom(OWLClass cls, String s) {

        String firstWord = first(s);
        String rest = rest(s);

        if (firstWord != null && rest != null) {
            ManchesterOWLSyntax keyword = getSyntax(firstWord);

            if (keyword == null) {
                throw new IllegalArgumentException("Unknown keyword in class axiom: \"" + firstWord + "\"");
            }

            switch (keyword) {
                case SUBCLASS_OF:
                    return df.getOWLSubClassOfAxiom(cls, clsExpr(rest));
                case EQUIVALENT_TO:
                    return df.getOWLEquivalentClassesAxiom(cls, clsExpr(rest));
                case DISJOINT_CLASSES:
                    return df.getOWLDisjointClassesAxiom(cls, clsExpr(rest));
                case HAS_KEY:
                    return df.getOWLHasKeyAxiom(cls, objProp(rest));
            }
        }
        throw new IllegalArgumentException("Badly formed class axiom: \"" + s + "\"");
    }

    private OWLAxiom getIndividualAxiom(OWLNamedIndividual ind, String s, boolean negated) {
        String first = first(s);
        String rest = rest(s);

        if (first != null && rest != null) {

            ManchesterOWLSyntax keyword = getSyntax(first);

            if (keyword != null) {
                switch (keyword) {
                    case TYPE:
                        return df.getOWLClassAssertionAxiom(clsExpr(rest), ind);
                }
            }

            OWLObjectProperty objProp = objProp(first);
            if (objProp != null) {
                return negated ?
                        df.getOWLNegativeObjectPropertyAssertionAxiom(objProp, ind, ind(rest)) :
                        df.getOWLObjectPropertyAssertionAxiom(objProp, ind, ind(rest));
            }

            OWLDataProperty dataProp = dataProp(first);
            if (dataProp != null) {
                return negated ?
                        df.getOWLNegativeDataPropertyAssertionAxiom(dataProp, ind, lit(rest)) :
                        df.getOWLDataPropertyAssertionAxiom(dataProp, ind, lit(rest));
            }
        }

        throw new IllegalArgumentException("Badly formed individual axiom: \"" + s + "\"");
    }

    private OWLAxiom getObjectPropertyAxiom (OWLObjectProperty prop, String s){

        // TODO propertyChains

        String firstWord = first(s);
        String rest = rest(s);

        if (firstWord != null && rest != null) {

            ManchesterOWLSyntax keyword = getSyntax(firstWord);

            if (keyword == null) {
                throw new IllegalArgumentException("Unknown keyword in object property axiom: \"" + firstWord + "\"");
            }

            switch (keyword) {
                case DOMAIN:
                    return df.getOWLObjectPropertyDomainAxiom(prop, clsExpr(rest));
                case RANGE:
                    return df.getOWLObjectPropertyRangeAxiom(prop, clsExpr(rest));
                case INVERSE_OF:
                    return df.getOWLInverseObjectPropertiesAxiom(prop, objProp(rest));
                case SUB_PROPERTY_OF:
                    return df.getOWLSubObjectPropertyOfAxiom(prop, objProp(rest));
                case EQUIVALENT_PROPERTIES:
                    return df.getOWLEquivalentObjectPropertiesAxiom(prop, objProp(rest));
                case DISJOINT_PROPERTIES:
                    return df.getOWLDisjointObjectPropertiesAxiom(prop, objProp(rest));
                default:
                    throw new IllegalArgumentException("Unexpected keyword in object property axiom: \"" + firstWord + "\"");
            }
        }
        throw new IllegalArgumentException("Badly formed object property axiom: \"" + s + "\"");
    }

    private OWLAxiom getDataPropertyAxiom (OWLDataProperty prop, String s){

        String firstWord = first(s);
        String rest = rest(s);

        if (firstWord != null && rest != null) {

            ManchesterOWLSyntax keyword = getSyntax(firstWord);

            if (keyword == null) {
                throw new IllegalArgumentException("Unknown keyword in data property axiom: \"" + firstWord + "\"");
            }

            switch (keyword) {
                case DOMAIN:
                    return df.getOWLDataPropertyDomainAxiom(prop, clsExpr(rest));
                case RANGE:
                    return df.getOWLDataPropertyRangeAxiom(prop, dataRange(rest));
                case SUB_PROPERTY_OF:
                    return df.getOWLSubDataPropertyOfAxiom(prop, dataProp(rest));
                case EQUIVALENT_PROPERTIES:
                    return df.getOWLEquivalentDataPropertiesAxiom(prop, dataProp(rest));
                case DISJOINT_PROPERTIES:
                    return df.getOWLDisjointDataPropertiesAxiom(prop, dataProp(rest));
                default:
                    throw new IllegalArgumentException("Unexpected keyword in data property axiom: \"" + firstWord + "\"");
            }
        }
        throw new IllegalArgumentException("Badly formed data property axiom: \"" + s + "\"");
    }

    private OWLClass cls (String s){
        OWLClass cls = checker.getOWLClass(s);
        if (cls == null) {
            throw new ParserException("Expected class", List.of(s), 0, 0, 0, false, true, false, false, false, false, false, false, Collections.emptySet());
        }
        return cls;
    }

    private OWLClassExpression clsExpr (String s){
        return classExpressionParser.parse(s);
    }

    private OWLNamedIndividual ind(String s) {
        OWLNamedIndividual ind = checker.getOWLIndividual(s);
        if (ind == null) {
            throw new ParserException("Expected individual", List.of(s), 0, 0, 0, false, false, false, false, true, false, false, false, Collections.emptySet());
        }
        return ind;
    }

    private OWLObjectProperty objProp (String s){
        OWLObjectProperty prop = checker.getOWLObjectProperty(s);
        if (prop == null) {
            throw new ParserException("Expected object property", List.of(s), 0, 0, 0, false, false, true, false, false, false, false, false, Collections.emptySet());
        }
        return prop;
    }

    private OWLDataProperty dataProp (String s){
        OWLDataProperty prop = checker.getOWLDataProperty(s);
        if (prop == null) {
            throw new ParserException("Expected data property", List.of(s), 0, 0, 0, false, false, false, true, false, false, false, false, Collections.emptySet());
        }
        return prop;
    }

    private OWLLiteral lit(String s) {
        try {
            int i = Integer.parseInt(s);
            return df.getOWLLiteral(i);
        } catch (NumberFormatException e) {
            // not a number
        }
        return df.getOWLLiteral(s); // TODO data types / lang
    }

    private OWLDataRange dataRange(String s) {
        return df.getOWLDatatype(XSDVocabulary.valueOf(s).getIRI());
    }

    private List<? extends OWLObjectPropertyExpression> propChain (String s){
        return Arrays.stream(s.split(CHAIN_CONNECT.keyword()))
                .map(p -> objProp(p.trim()))
                .collect(Collectors.toList());
    }

    private ManchesterOWLSyntax getSyntax (String rendering){
        for (ManchesterOWLSyntax m : ManchesterOWLSyntax.values()) {
            if (m.toString().equals(rendering)) {
                return m;
            }
        }
        return null;
    }

    private String first(String s) {
        int firstSpacePosition = s.indexOf(" ");
        String first = (firstSpacePosition != -1) ? s.substring(0, firstSpacePosition) : null;
        return first;
    }

    private String rest(String s) {
        int firstSpacePosition = s.indexOf(" ");
        String rest =  (firstSpacePosition != -1) ? s.substring(firstSpacePosition + 1) : null;
        return rest;
    }

    private String unbracket(String s) {
        s = s.trim();
        String open = OPEN.keyword();
        String close = CLOSE.keyword();
        if (s.startsWith(open) && s.endsWith(close)) {
            return s.substring(open.length(), s.length()-close.length()).trim();
        }
        throw new IllegalArgumentException("Expected brackets missing: \"" + s + "\"");
    }
}