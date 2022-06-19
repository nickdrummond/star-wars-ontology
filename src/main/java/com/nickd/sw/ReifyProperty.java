package com.nickd.sw;

import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReifyProperty {

    private OWLClass replacementClass;
    private OWLObjectProperty targetProperty;
    private OWLObjectProperty ofProperty;
    private OWLObjectProperty includedProperty;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Helper helper = new Helper("all.owl.ttl", new StarWarsOntologiesIRIMapper());
        ReifyProperty reifyProperty = new ReifyProperty(
                helper.prop("stole"),
                helper.cls("Stealing"),
                helper.prop("of"),
                helper.prop("included"));
        List<OWLOntologyChange> changes = reifyProperty.run(helper.mngr.getOntologies(), helper.df);

        helper.mngr.applyChanges(changes);

        helper.saveAll();
    }

    public ReifyProperty(OWLObjectProperty targetProperty,
                         OWLClass replacementClass,
                         OWLObjectProperty ofProperty,
                         OWLObjectProperty includedProperty) {
        this.targetProperty = targetProperty;
        this.replacementClass = replacementClass;
        this.ofProperty = ofProperty;
        this.includedProperty = includedProperty;
    }

    public List<OWLOntologyChange> run(final Set<OWLOntology> onts, OWLDataFactory df) {

        List<OWLOntologyChange> changes = new ArrayList<>();

        OWLClassExpressionTransformer clsTransformer = new OWLClassExpressionTransformer(df);
        OWLAxiomTransformer owlAxiomTransformer = new OWLAxiomTransformer(df, clsTransformer);

        onts.forEach( ont -> ont .getReferencingAxioms(targetProperty, Imports.EXCLUDED).forEach(ax -> {

            OWLAxiom axTran = ax.accept(owlAxiomTransformer);

            if (axTran != null && ax != axTran) {
                changes.add(new RemoveAxiom(ont, ax));
                changes.add(new AddAxiom(ont, axTran));
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

    private class OWLAxiomTransformer implements OWLAxiomVisitorEx<OWLAxiom> {

        private OWLDataFactory df;
        private OWLClassExpressionTransformer clsTransformer;

        private OWLAxiomTransformer(OWLDataFactory df, OWLClassExpressionTransformer clsTransformer) {
            this.df = df;
            this.clsTransformer = clsTransformer;
        }

        @Override
        public OWLAxiom visit(OWLClassAssertionAxiom axiom) {
            OWLClassExpression typeTran = axiom.getClassExpression().accept(clsTransformer);
            if (typeTran == null) {
                throw new RuntimeException("Failed to transform: " + render(axiom));
            }
            return df.getOWLClassAssertionAxiom(typeTran, axiom.getIndividual(), axiom.annotationsAsList());
        }

        @Override
        public OWLAxiom visit(OWLObjectPropertyAssertionAxiom axiom) {
            return df.getOWLClassAssertionAxiom(df.getOWLObjectSomeValuesFrom(includedProperty,
                    df.getOWLObjectIntersectionOf(replacementClass,
                            df.getOWLObjectHasValue(ofProperty, axiom.getObject()))), axiom.getSubject());
        }
    }

    private class OWLClassExpressionTransformer implements OWLClassExpressionVisitorEx<OWLClassExpression> {

        private OWLDataFactory df;

        private OWLClassExpressionTransformer(OWLDataFactory df) {
            this.df = df;
        }

        @Override
        public OWLClassExpression visit(OWLObjectHasValue ce) {
            if (ce.getProperty().equals(targetProperty)) {
                return df.getOWLObjectSomeValuesFrom(includedProperty,
                        df.getOWLObjectIntersectionOf(replacementClass,
                                df.getOWLObjectHasValue(ofProperty, ce.getFiller())));
            }
            return ce;
        }

        @Override
        public OWLClassExpression visit(OWLObjectSomeValuesFrom ce) {
            if (ce.getProperty().equals(targetProperty)) {
                return df.getOWLObjectSomeValuesFrom(includedProperty,
                        df.getOWLObjectIntersectionOf(replacementClass,
                                df.getOWLObjectSomeValuesFrom(ofProperty, ce.getFiller().accept(this))));
            }
            else if (ce.containsEntityInSignature(targetProperty)) {
                return df.getOWLObjectSomeValuesFrom(ce.getProperty(), ce.getFiller().accept(this));
            }
            return ce;
        }

        @Override
        public OWLClassExpression visit(OWLObjectExactCardinality ce) {
            if (ce.getProperty().equals(targetProperty)) {
                return df.getOWLObjectSomeValuesFrom(includedProperty,
                        df.getOWLObjectIntersectionOf(replacementClass,
                                df.getOWLObjectExactCardinality(ce.getCardinality(), ofProperty, ce.getFiller().accept(this))));
            }
            else if (ce.containsEntityInSignature(targetProperty)) {
                return df.getOWLObjectExactCardinality(ce.getCardinality(), ce.getProperty(), ce.getFiller().accept(this));
            }
            return ce;
        }

        @Override
        public OWLClassExpression visit(OWLObjectIntersectionOf ce) {
            if (ce.containsEntityInSignature(targetProperty)) {
                return df.getOWLObjectIntersectionOf(ce.operands().map(o -> o.accept(this)));
            }
            return ce;
        }

        @Override
        public <T> OWLClassExpression doDefault(T object) {
            return (OWLClassExpression) object;
        }
    };
}
