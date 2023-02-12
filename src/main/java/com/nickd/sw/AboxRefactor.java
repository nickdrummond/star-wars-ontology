package com.nickd.sw;

import com.github.jsonldjava.shaded.com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.StringWriter;
import java.util.*;

public class AboxRefactor {

    private final OWLObjectProperty includedProperty;
    private final OWLObjectProperty duringProperty;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Helper helper = new Helper("all.owl.ttl", new StarWarsOntologiesIRIMapper());
        AboxRefactor reifyProperty = new AboxRefactor(helper.prop("included"), helper.prop("during"));
        List<OWLOntologyChange> changes = reifyProperty.run(helper.mngr.getOntologies(), helper.df);

        helper.mngr.applyChanges(changes);

        helper.saveChanged();
    }

    public AboxRefactor(final OWLObjectProperty includedProperty,
                        final OWLObjectProperty duringProperty) {
        this.includedProperty = includedProperty;
        this.duringProperty = duringProperty;
    }

    public List<OWLOntologyChange> run(final Set<OWLOntology> onts, OWLDataFactory df) {

        List<OWLOntologyChange> changes = new ArrayList<>();

        OWLAxiomTransformer owlAxiomTransformer = new OWLAxiomTransformer(df, new NameProvider());

        onts.forEach( ont -> ont .getReferencingAxioms(includedProperty, Imports.EXCLUDED).forEach(ax -> {
            Set<OWLAxiom> axTran = ax.accept(owlAxiomTransformer);
            if (!axTran.isEmpty()) {
                changes.add(new RemoveAxiom(ont, ax));
                axTran.forEach(a -> changes.add(new AddAxiom(ont, a)));
            }
            else {
                System.err.println("No transform performed on: " + render(ax));
            }

        }));
        return changes;
    }

    public String render(OWLObject o) {
        final ShortFormProvider sfp = new SimpleShortFormProvider();
        StringWriter w = new StringWriter();
        o.accept(new ManchesterOWLSyntaxObjectRenderer(w, sfp));
        return w.toString();
    }

    private class OWLAxiomTransformer implements OWLAxiomVisitorEx<Set<OWLAxiom>> {

        private final OWLDataFactory df;
        private final NameProvider nameProvider;

        private OWLAxiomTransformer(OWLDataFactory df, final NameProvider nameProvider) {
            this.df = df;
            this.nameProvider = nameProvider;
        }

        @Override
        public Set<OWLAxiom> visit(OWLClassAssertionAxiom axiom) {
            OWLIndividual event = axiom.getIndividual();
            OWLClassToIndividual clsTransformer = new OWLClassToIndividual(df, event, nameProvider);
            axiom.getClassExpression().accept(clsTransformer);
            return clsTransformer.getAxioms();
        }

        @Override
        public <T> Set<OWLAxiom> doDefault(T object) {
            return Collections.emptySet();
        }
    }

    private class NameProvider {

        private final String BASE = "https://nickdrummond.github.io/star-wars-ontology/ontologies#";

        private final Map<OWLIndividual, Integer> eventChildCount = Maps.newHashMap();

        public IRI getName(OWLIndividual parentEvent) {
            int count = eventChildCount.getOrDefault(parentEvent, 0);
            eventChildCount.put(parentEvent, count+1);
            return IRI.create(BASE + "_during_" + render(parentEvent).toLowerCase() + "_" + count);
        }
    }

    private class OWLClassToIndividual implements OWLClassExpressionVisitor {

        private final OWLDataFactory df;
        private final OWLIndividual parentEvent;
        private final NameProvider nameProvider;

        private final Set<OWLAxiom> axioms = Sets.newHashSet();

        private OWLIndividual subEvent = null;

        private OWLClassToIndividual(final OWLDataFactory df,
                                     final OWLIndividual parentEvent,
                                     final NameProvider nameProvider) {
            this.df = df;
            this.parentEvent = parentEvent;
            this.nameProvider = nameProvider;
        }

        @Override
        public void visit(OWLObjectSomeValuesFrom ce) {
            if (ce.getProperty().equals(includedProperty)) {
                if (subEvent == null) {
                    subEvent = df.getOWLNamedIndividual(nameProvider.getName(parentEvent));
                    axioms.add(df.getOWLObjectPropertyAssertionAxiom(duringProperty, subEvent, parentEvent));
                    ce.getFiller().accept(this);
                }
                else {
                    // nesting - see Attack_on_Rishi_Station
                    OWLClassToIndividual nest = new OWLClassToIndividual(df, subEvent, nameProvider);
                    ce.accept(nest);
                    axioms.addAll(nest.getAxioms());
                }
            }
            else {
                if (subEvent != null) {
                    axioms.add(df.getOWLClassAssertionAxiom(ce, subEvent));
                }
                else {
                    System.err.println(render(parentEvent) + " Unexpected some: " + render(ce));
                }
            }
        }

        @Override
        public void visit(OWLObjectIntersectionOf ce) {
            ce.operands().forEach(c -> c.accept(this));
        }

        @Override
        public void visit(OWLClass ce) {
            axioms.add(df.getOWLClassAssertionAxiom(ce, subEvent));
        }

        @Override
        public void visit(OWLObjectHasValue ce) {
            axioms.add(df.getOWLObjectPropertyAssertionAxiom(ce.getProperty(), subEvent, ce.getFiller()));
        }

        @Override
        public void visit(OWLObjectUnionOf ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled Union: " + render(ce));
        }

        @Override
        public void visit(OWLObjectComplementOf ce) {
            if (subEvent != null) {
                axioms.add(df.getOWLClassAssertionAxiom(ce, subEvent));
            }
            else {
                System.err.println(render(parentEvent) + " Unexpected complement: " + render(ce));
            }
        }

        @Override
        public void visit(OWLObjectAllValuesFrom ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled Only: " + render(ce));
        }

        @Override
        public void visit(OWLObjectMinCardinality ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLObjectExactCardinality ce) {
            axioms.add(df.getOWLClassAssertionAxiom(ce, subEvent));
        }

        @Override
        public void visit(OWLObjectMaxCardinality ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLObjectHasSelf ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLObjectOneOf ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLDataSomeValuesFrom ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLDataAllValuesFrom ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLDataHasValue ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLDataMinCardinality ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLDataExactCardinality ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        @Override
        public void visit(OWLDataMaxCardinality ce) {
            throw new RuntimeException(render(parentEvent) + " - Unhandled restriction: " + render(ce));
        }

        public Set<OWLAxiom> getAxioms() {
            return axioms;
        }
    };
}
