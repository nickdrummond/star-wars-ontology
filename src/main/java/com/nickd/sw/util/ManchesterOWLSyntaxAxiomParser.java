package com.nickd.sw.util;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax.*;


/**
 * Parse axioms in Manchester OWL Syntax.
 * The Manchester OWL Syntax is specified as a frame-based rendering, but this gives
 * us axiom approximations based on rendering by ManchesterOWLSyntaxObjectRenderer.
 * TODO annotations
 * TODO datatypes
 * TODO disjoints/different
 * TODO property chains
 * TODO GCIs
 * TODO Make all ParserExceptions - correct indexing
 * TODO lookahead for hints of the type for Exceptions
 */
public class ManchesterOWLSyntaxAxiomParser {

    private final OWLDataFactory df;
    private final OWLEntityChecker checker;
    private final ManchesterOWLSyntaxClassExpressionParser classExpressionParser;
    private Set<String> startKeywords = Set.of(FUNCTIONAL.keyword());

    public ManchesterOWLSyntaxAxiomParser(OWLDataFactory df,
                                          OWLEntityChecker checker) {
        this.df = df;
        this.checker = checker;
        this.classExpressionParser = new ManchesterOWLSyntaxClassExpressionParser(df, checker);
    }

    public OWLAxiom parse(String s) throws ParserException {
        s = s.trim();

        if (s.isEmpty()) {
            throw new ParserException("Empty input", Collections.singletonList(""), 0, 0, 0, true, true, true, true, true, true, true, true, startKeywords);
        }

        OWLAxiom axiom = parsePropertyCharacteristicAxiom(s);
        if (axiom != null) {
            return axiom;
        }

        axiom = parseNegatedPropertyAssertion(s);
        if (axiom != null) {
            return axiom;
        }

        // TODO don't throw if indiv not found - might be another type of axiom

        // Either it is a well formed ind axiom (return axiom), a badly formed Ind axiom (throw), or not an individual axiom (return null)

        Optional<OWLAxiom> optAxiom = parseIndividualAxiom(s);
        if (optAxiom.isPresent()) {
            return optAxiom.get();
        }

        axiom = parseClassAxiom(s);
        if (axiom != null) {
            return axiom;
        }

        axiom = parseObjectPropertyAxiom(s);
        if (axiom != null) {
            return axiom;
        }

        axiom = parseDataPropertyAxiom(s);
        if (axiom != null) {
            return axiom;
        }

        throw new IllegalArgumentException("Unexpected input: \"" + s + "\"");
    }

    public OWLAxiom parseNegatedPropertyAssertion(String s) throws ParserException {
        String first = first(s);
        String rest = rest(s);

        if (first != null && rest != null && NOT.matches(first)) {

            // then this MUST be a NOT
            String inBrackets = unbracket(rest);

            first = first(inBrackets);
            rest = rest(inBrackets);

            if (first != null && rest != null) {

                OWLNamedIndividual ind = indOrThrow(first, null); // TODO

                first = first(rest);
                rest = rest(rest);

                if (first != null && rest != null) {

                    OWLObjectProperty objProp = objProp(first);
                    if (objProp != null) {
                        return df.getOWLNegativeObjectPropertyAssertionAxiom(objProp, ind, indOrThrow(rest, null)); // TODO
                    }

                    OWLDataProperty dataProp = dataProp(first);
                    if (dataProp != null) {
                        return df.getOWLNegativeDataPropertyAssertionAxiom(dataProp, ind, litOrThrow(rest));
                    }
                }
            }
            throw new IllegalArgumentException("Badly formed negated individual axiom: \"" + s + "\"");
        }
        return null;
    }

    public Optional<OWLAxiom> parseIndividualAxiom(final String s) throws ParserException {
        List<String> tokens = new ArrayList<>();
        String indName = first(s);
        tokens.add(indName);

        Optional<OWLNamedIndividual> ind = ind(indName);

        if (ind.isEmpty()) { // not an individual axiom
            return Optional.empty();
        }

        String rest = rest(s);
        if (rest.isEmpty()) {
            throw new ParserException("Badly formed individual axiom", tokens, indName.length()+1, 0, 0, false, false, true, true, false, false, false, false, Set.of(TYPE.toString()));
        }

        String keyWordOrProp = first(rest);
        tokens.add(keyWordOrProp);

        ManchesterOWLSyntax keyword = getSyntax(keyWordOrProp);
        if (keyword != null) {
            switch (keyword) {
                case TYPE:
                    String obj = rest(rest);
                    tokens.add(rest);
                    return Optional.of(df.getOWLClassAssertionAxiom(clsExpr(obj), ind.get()));
                default:
                    throw new ParserException("Found unexpected keyword: " + keyword, tokens, 0, 0, 0, false, false, true, true, false, false, false, false, Set.of(TYPE.toString()));
            }
        }

        OWLObjectProperty objProp = objProp(keyWordOrProp);
        if (objProp != null) {
            String objText = rest(rest);
            tokens.add(objText);
            OWLNamedIndividual obj = indOrThrow(objText, () -> new ParserException(tokens, 0, 0, 0, false, false, false, false, true, false, false, false, Collections.emptySet()));
            return Optional.of(df.getOWLObjectPropertyAssertionAxiom(objProp, ind.get(), obj));
        }

        OWLDataProperty dataProp = dataProp(keyWordOrProp);
        if (dataProp != null) {
            String obj = rest(rest);
            tokens.add(rest);
            return Optional.of(df.getOWLDataPropertyAssertionAxiom(dataProp, ind.get(), litOrThrow(obj)));
        }
        return Optional.empty();
    }

    public OWLAxiom parsePropertyCharacteristicAxiom(String s) {
        // try keyword leading axioms first
        String first = first(s, ":");
        String rest = rest(s, ":");

        if (first != null && rest != null) {

            ManchesterOWLSyntax keyword = getSyntax(first);

            if (keyword == null) {
                throw new IllegalArgumentException("Unknown keyword in property characteristic: \"" + first + "\"");
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
                    throw new IllegalArgumentException("Unexpected keyword in property characteristic: \"" + first + "\"");
            }
        }
        return null;
    }

    public OWLAxiom parseClassAxiom(final String s) {
        String first = first(s);
        String rest = rest(s);

        if (first != null && rest != null) {

            OWLClass cls = cls(first);

            first = first(rest);
            rest = rest(rest);

            if (first != null && rest != null) {
                ManchesterOWLSyntax keyword = getSyntax(first);

                if (keyword == null) {
                    throw new IllegalArgumentException("Unknown keyword in class axiom: \"" + first + "\"");
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
        }
        throw new IllegalArgumentException("Badly formed class axiom: \"" + s + "\"");
    }


    public OWLAxiom parseObjectPropertyAxiom (final String s){

        // TODO propertyChains
        String first = first(s);
        String rest = rest(s);

        if (first != null && rest != null) {

            OWLObjectProperty prop = objProp(first);

            first = first(rest);
            rest = rest(rest);

            if (first != null && rest != null) {

                ManchesterOWLSyntax keyword = getSyntax(first);

                if (keyword == null) {
                    throw new IllegalArgumentException("Unknown keyword in object property axiom: \"" + first + "\"");
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
                        throw new IllegalArgumentException("Unexpected keyword in object property axiom: \"" + first + "\"");
                }
            }
        }
        throw new IllegalArgumentException("Badly formed object property axiom: \"" + s + "\"");
    }

    public OWLAxiom parseDataPropertyAxiom(String s){
        String first = first(s);
        String rest = rest(s);

        if (first != null && rest != null) {

            OWLDataProperty prop = dataProp(first);

            first = first(rest);
            rest = rest(rest);


            if (first != null && rest != null) {

                ManchesterOWLSyntax keyword = getSyntax(first);

                if (keyword == null) {
                    throw new IllegalArgumentException("Unknown keyword in data property axiom: \"" + first + "\"");
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
                        throw new IllegalArgumentException("Unexpected keyword in data property axiom: \"" + first + "\"");
                }
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

    private Optional<OWLNamedIndividual> ind(String s) {
        return Optional.ofNullable(checker.getOWLIndividual(s));
    }

    private OWLNamedIndividual indOrThrow(String s, Supplier<ParserException> exceptionGen) throws ParserException {
        return ind(s).orElseThrow(exceptionGen);
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

    private OWLLiteral litOrThrow(String s) {
        return lit(s).orElseThrow(() -> new ParserException("Expected literal", List.of(s), 0, 0, 0, false, false, false, false, false, false, false, true, Collections.emptySet()));
    }

    private Optional<OWLLiteral> lit(String s) {
        try {
            int i = Integer.parseInt(s);
            return Optional.of(df.getOWLLiteral(i));
        } catch (NumberFormatException e) {
            // not a number
        }
        return Optional.of(df.getOWLLiteral(s)); // TODO data types / lang
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
            if (m.toString().equalsIgnoreCase(rendering)) {
                return m;
            }
        }
        return null;
    }


    private @Nonnull String first(@Nonnull String s, @Nonnull String delim) {
        int firstSpacePosition = s.indexOf(delim);
        return (firstSpacePosition != -1) ? s.substring(0, firstSpacePosition).trim() : s;
    }

    private @Nonnull String rest(@Nonnull String s, @Nonnull String delim) {
        int firstSpacePosition = s.indexOf(delim);
        return (firstSpacePosition != -1) ? s.substring(firstSpacePosition + 1).trim() : "";
    }

    private @Nonnull String first(@Nonnull String s) {
        return first(s, " ");
    }

    private @Nonnull String rest(@Nonnull String s) {
        return rest(s, " ");
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