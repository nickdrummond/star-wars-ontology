package com.nickd.sw;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReifyBirth {

    private OWLClass replacementClass;
    private OWLDataProperty targetProperty;
    private OWLObjectProperty ofProperty;
    private OWLDataProperty yearProperty;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Helper helper = new Helper(new File("ontologies/all.owl.ttl"));
        ReifyBirth reifyProperty = new ReifyBirth(
                helper.dataProp("born"),
                helper.cls("Birth"),
                helper.prop("of"),
                helper.dataProp("year"));
        List<OWLOntologyChange> changes = reifyProperty.run(helper.mngr.getOntologies(), helper.df);

        helper.mngr.applyChanges(changes);

        if (changes.isEmpty()) {
            System.out.println("No changes to make");
        }
        else {
            helper.saveChanged();
        }
    }

    public ReifyBirth(OWLDataProperty targetProperty,
                      OWLClass replacementClass,
                      OWLObjectProperty ofProperty,
                      OWLDataProperty yearProperty) {
        this.targetProperty = targetProperty;
        this.replacementClass = replacementClass;
        this.ofProperty = ofProperty;
        this.yearProperty = yearProperty;
    }

    public List<OWLOntologyChange> run(final Set<OWLOntology> onts, OWLDataFactory df) {

        List<OWLOntologyChange> changes = new ArrayList<>();

        onts.forEach( ont -> ont .getReferencingAxioms(targetProperty, Imports.EXCLUDED).forEach(ax -> {

            OWLAxiomTransformer owlAxiomTransformer = new OWLAxiomTransformer(df);
            ax.accept(owlAxiomTransformer);
            Set<OWLAxiom> axTran = owlAxiomTransformer.getNewAxioms();

            if (!axTran.isEmpty()) {
                changes.add(new RemoveAxiom(ont, ax));
                axTran.forEach(a->changes.add(new AddAxiom(ont, a)));
            }
            else {
                System.out.println("Failed to transform " + ax);
            }
        }));
        return changes;
    }

    public String render(OWLObject o ) {
        final ShortFormProvider sfp = new SimpleShortFormProvider();
        StringWriter w = new StringWriter();
        o.accept(new ManchesterOWLSyntaxObjectRenderer(w, sfp));
        return w.toString();
    }

    private class OWLAxiomTransformer implements OWLAxiomVisitor {

        private final OWLDataFactory df;
        private final Set<OWLAxiom> newAxioms = new HashSet<>();

        private OWLAxiomTransformer(OWLDataFactory df) {
            this.df = df;
        }

        @Override
        public void visit(OWLDataPropertyAssertionAxiom axiom) {
            OWLLiteral year = axiom.getObject();
            OWLNamedIndividual subject = axiom.getSubject().asOWLNamedIndividual();

            String name = subject.getIRI().getFragment();
            String namespace = subject.getIRI().getNamespace();

            OWLNamedIndividual birth = df.getOWLNamedIndividual(IRI.create(namespace + "Birth_of_" + name));

            newAxioms.add(df.getOWLDeclarationAxiom(birth));
            newAxioms.add(df.getOWLClassAssertionAxiom(replacementClass, birth));
            newAxioms.add(df.getOWLObjectPropertyAssertionAxiom(ofProperty, birth, subject));
            newAxioms.add(df.getOWLDataPropertyAssertionAxiom(yearProperty, birth, year));
        }

        @Override
        public void visit(OWLClassAssertionAxiom axiom) {
            OWLClassExpressionTransformer transformer = new OWLClassExpressionTransformer(df);
            axiom.getClassExpression().accept(transformer);
            OWLClassExpression yearRestriction = transformer.getTransformed();

            OWLNamedIndividual subject = axiom.getIndividual().asOWLNamedIndividual();

            String name = subject.getIRI().getFragment();
            String namespace = subject.getIRI().getNamespace();

            OWLNamedIndividual birth = df.getOWLNamedIndividual(IRI.create(namespace + "Birth_of_" + name));

            newAxioms.add(df.getOWLClassAssertionAxiom(replacementClass, birth));
            newAxioms.add(df.getOWLObjectPropertyAssertionAxiom(ofProperty, birth, subject));
            newAxioms.add(df.getOWLClassAssertionAxiom(yearRestriction, birth));
        }

        public Set<OWLAxiom> getNewAxioms() {
            return newAxioms;
        }
    }


    private class OWLClassExpressionTransformer implements OWLClassExpressionVisitor {

        private OWLDataFactory df;
        private OWLDataSomeValuesFrom ceTrans;

        private OWLClassExpressionTransformer(OWLDataFactory df) {
            this.df = df;
        }

        @Override
        public void visit(OWLDataSomeValuesFrom ce) {
            if (ce.getProperty().equals(targetProperty)) {
                ceTrans = df.getOWLDataSomeValuesFrom(yearProperty, ce.getFiller());
            }
        }

        public OWLDataSomeValuesFrom getTransformed() {
            return ceTrans;
        }
    };
}
