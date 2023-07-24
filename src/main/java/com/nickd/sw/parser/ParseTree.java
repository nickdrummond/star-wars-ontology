package com.nickd.sw.parser;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParseTree implements OWLObjectProvider {

    public static final String PREFIX_SUGAR = ":";
    private OWLDataFactory df;
    private MyTokenizer tokenizer;
    private OWLEntityChecker checker;
    private final LinkedHashMap<String, AbstractParseMatcher> matchers = new LinkedHashMap<>();
    private OWLAxiom axiom;

    private Function<OWLObjectProvider, OWLAxiom> creator;
    private ParseTree parent = null;

    private ParseTree() {
    }

    public ParseTree(final String s, final OWLEntityChecker checker, OWLDataFactory df) {
        this.tokenizer = new MyTokenizer(s);
        this.checker = checker;
        this.df = df;
    }

    public static ParseTree branch() {
        return new ParseTree();
    }

    public ParseTree expect(String key, AbstractParseMatcher m) throws ParserException {
        matchers.put(key, m);
        return this;
    }

    public ParseTree expectEither(ParseTree... branches) throws ParserException {
        Arrays.stream(branches).forEach(branch -> branch.parent = ParseTree.this);
        return expect("", new BranchingMatcher(branches));
    }

    public ParseTree create(Function<OWLObjectProvider, OWLAxiom> o) {
        this.creator = o;
        return this;
    }

    public ParseTree expectIndividual(String a) {
        return expect(a, new IndividualMatcher());
    }

    public ParseTree expectClass(String a) {
        return expect(a, new ClassMatcher());
    }

    public ParseTree expectClassExpression(String a) {
        return expect(a, new ClassExpressionMatcher());
    }

    public ParseTree expectObjectProperty(String p) {
        return expect(p, new ObjPropMatcher());
    }

    public ParseTree expectDataProperty(String p) {
        return expect(p, new DataPropMatcher());
    }

    public ParseTree expectKeyword(ManchesterOWLSyntax manchesterOWLSyntax) {
        return expect(manchesterOWLSyntax.toString(), new KeywordMatcher(manchesterOWLSyntax));
    }

    public ParseTree expectPrefixKeyword(ManchesterOWLSyntax manchesterOWLSyntax) {
        return expect(manchesterOWLSyntax.toString() + PREFIX_SUGAR, new KeywordMatcher(manchesterOWLSyntax, PREFIX_SUGAR));
    }

    public ParseTree expectLiteral(String key) {
        return expect(key, new LiteralMatcher());
    }

    public ParseTree expectSugar(String s) {
        return expect(s, new SugarMatcher(s));
    }

    @Override
    public OWLObjectPropertyExpression objProp(String key) {
        return matchers.get(key).getObjectProperty();
    }

    @Override
    public OWLDataPropertyExpression dataProp(String key) {
        return matchers.get(key).getDataProperty();
    }

    @Override
    public OWLIndividual ind(String key) {
        return matchers.get(key).getIndividual();
    }

    @Override
    public OWLClassExpression cls(String key) {
        return matchers.get(key).getOWLClass();
    }

    @Override
    public OWLClassExpression clsExpr(String key) {
        return matchers.get(key).getOWLClassExpression();
    }

    @Override
    public OWLLiteral lit(String key) {
        return matchers.get(key).getLiteral();
    }


    public OWLAxiom getAxiom() {
        if (axiom == null) {
            // make a copy as branches may modify the content
            new LinkedHashMap<>(matchers).forEach( (k, m) -> m.check(tokenizer, checker, df));
        }
        axiom = creator.apply(this);
        return axiom;
    }

    private class BranchingMatcher extends AbstractParseMatcher {
        private final List<ParseTree> branches;

        public BranchingMatcher(ParseTree... branches) {
            this.branches = List.of(branches);
        }

        @Override
        public void check(MyTokenizer tokenizer, OWLEntityChecker checker, OWLDataFactory df) throws ParserException {
            List<ParserException> fails = new ArrayList<>();
            for (ParseTree branch : branches) {
                try {
                    branch.tokenizer = new MyTokenizer(branch.parent.tokenizer); // clone state of parent tokenizer for each branch
                    branch.checker = branch.parent.checker;
                    branch.df = branch.parent.df;

                    // run all matchers in the branch (make a copy first because of nesting)
                    new LinkedHashMap<>(branch.matchers).forEach( (k, m) -> m.check(branch.tokenizer, branch.checker, branch.df));

                    // if no parse errors

                    // copy the successful matchers to the parent
                    branch.parent.matchers.putAll(branch.matchers);

                    // along with the builder which will eventually call this creator
                    branch.parent.creator = branch.creator;

                    return; // No need to try other branches if successful
                }
                catch(ParserException e) {
                    fails.add(e);
                }
            }
            // if all branches below this failed, fail this branch
            if (fails.size() == branches.size()) {
                throw merge(getBestFails(fails));
            }
        }
    }

    private List<ParserException> getBestFails(List<ParserException> fails) {
        int maxFailDepth = maxDepth(fails);
        return fails.stream().filter(f -> f.getTokenSequence().size() >= maxFailDepth).collect(Collectors.toList());
    }

    private ParserException merge(List<ParserException> fails) {

        ParserException representativeException = fails.get(0);
        if (fails.size() == 1) {
            return representativeException;
        }

        List<String> tokens = representativeException.getTokenSequence();
        int startPos = representativeException.getStartPos();
        int line = representativeException.getLineNumber();
        int column = representativeException.getColumnNumber();

        boolean ont = false;
        boolean cls = false;
        boolean oProp = false;
        boolean dProp = false;
        boolean ind = false;
        boolean dt = false;
        boolean aProp = false;
        boolean inte = false;
        Set<String> keys = new HashSet<>();

        for (ParserException f : fails) {
            if (f.isOntologyNameExpected()) ont = true;
            if (f.isClassNameExpected()) cls = true;
            if (f.isObjectPropertyNameExpected()) oProp = true;
            if (f.isDataPropertyNameExpected()) dProp = true;
            if (f.isIndividualNameExpected()) ind = true;
            if (f.isDatatypeNameExpected()) dt = true;
            if (f.isAnnotationPropertyNameExpected()) aProp = true;
            if (f.isIntegerExpected()) inte = true;
            keys.addAll(f.getExpectedKeywords());
        }

        return new ParserException(tokens, startPos, line, column, ont, cls, oProp, dProp, ind, dt, aProp, inte, keys);
    }

    private int maxDepth(List<ParserException> fails) {
        int max = 0;
        for (ParserException f : fails) {
            int size = f.getTokenSequence().size();
            if (size > max) {
                max = size;
            }
        }
        return max;
    }
}
