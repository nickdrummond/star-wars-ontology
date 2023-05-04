package com.nickd.sw;

import com.github.jsonldjava.shaded.com.google.common.collect.Sets;
import com.nickd.sw.util.Helper;
import com.nickd.sw.util.NameProvider;
import com.nickd.sw.util.StarWarsOntologiesIRIMapper;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.util.*;

public class AboxRefactor {

    private final String BASE = "https://nickdrummond.github.io/star-wars-ontology/ontologies#";

    private final OWLObjectProperty includedProperty;
    private final OWLObjectProperty duringProperty;
    private final OWLClass eventClass;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Helper helper = new Helper("all.owl.ttl", new StarWarsOntologiesIRIMapper());
        AboxRefactor reifyProperty = new AboxRefactor(
                helper.prop("included"),
                helper.prop("during"),
                helper.cls("Event"));

        NameProvider nameProvider = new NameProvider();

        List<OWLOntologyChange> changes = reifyProperty.run(helper.mngr.getOntologies(), helper.df, nameProvider);

        helper.mngr.applyChanges(changes);

        helper.saveChanged();
    }

    public AboxRefactor(final OWLObjectProperty includedProperty,
                        final OWLObjectProperty duringProperty,
                        final OWLClass eventClass) {
        this.includedProperty = includedProperty;
        this.duringProperty = duringProperty;
        this.eventClass = eventClass;
    }

    public List<OWLOntologyChange> run(final Set<OWLOntology> onts,
                                       final OWLDataFactory df,
                                       final NameProvider nameProvider) {

        List<OWLOntologyChange> changes = new ArrayList<>();

        OWLAxiomTransformer owlAxiomTransformer = new OWLAxiomTransformer(df, nameProvider);
        
        onts.forEach( ont -> ont .getReferencingAxioms(includedProperty, Imports.EXCLUDED).forEach(ax -> {
            Set<OWLAxiom> axTran = ax.accept(owlAxiomTransformer);
            if (!axTran.isEmpty()) {
                changes.add(new RemoveAxiom(ont, ax));
                axTran.forEach(a -> changes.add(new AddAxiom(ont, a)));
            }
            else {
                System.err.println("No transform performed on: " + nameProvider.render(ax));
            }

        }));
        return changes;
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


    private class OWLClassToIndividual implements OWLClassExpressionVisitor {

        private final OWLDataFactory df;
        private final OWLIndividual parentEvent;
        private final NameProvider nameProvider;

        private final Set<OWLAxiom> axioms = Sets.newHashSet();

        private OWLIndividual subEvent = null;
        private boolean hasNamedType = false;

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
                    String name = nameProvider.getName(ce.getFiller(), parentEvent);
                    subEvent = df.getOWLNamedIndividual(IRI.create(BASE + "_" + name));
                    axioms.add(df.getOWLObjectPropertyAssertionAxiom(duringProperty, subEvent, parentEvent));
                    ce.getFiller().accept(this);
                    if (!hasNamedType) {
                        addDefaultType();
                    }
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
                    System.err.println(nameProvider.render(parentEvent) + " Unexpected some: " + nameProvider.render(ce));
                }
            }
        }

        @Override
        public void visit(OWLObjectIntersectionOf ce) {
            ce.operands().forEach(c -> c.accept(this));
        }

        @Override
        public void visit(OWLClass ce) {
            hasNamedType = true;
            axioms.add(df.getOWLClassAssertionAxiom(ce, subEvent));
        }

        @Override
        public void visit(OWLObjectHasValue ce) {
            axioms.add(df.getOWLObjectPropertyAssertionAxiom(ce.getProperty(), subEvent, ce.getFiller()));
        }

        @Override
        public void visit(OWLObjectUnionOf ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled Union: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLObjectComplementOf ce) {
            if (subEvent != null) {
                axioms.add(df.getOWLClassAssertionAxiom(ce, subEvent));
            }
            else {
                System.err.println(nameProvider.render(parentEvent) + " Unexpected complement: " + nameProvider.render(ce));
            }
        }

        @Override
        public void visit(OWLObjectAllValuesFrom ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled Only: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLObjectMinCardinality ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLObjectExactCardinality ce) {
            axioms.add(df.getOWLClassAssertionAxiom(ce, subEvent));
        }

        @Override
        public void visit(OWLObjectMaxCardinality ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLObjectHasSelf ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLObjectOneOf ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLDataSomeValuesFrom ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLDataAllValuesFrom ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLDataHasValue ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLDataMinCardinality ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLDataExactCardinality ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        @Override
        public void visit(OWLDataMaxCardinality ce) {
            throw new RuntimeException(nameProvider.render(parentEvent) + " - Unhandled restriction: " + nameProvider.render(ce));
        }

        public Set<OWLAxiom> getAxioms() {
            return axioms;
        }

        private void addDefaultType() {
            axioms.add(df.getOWLClassAssertionAxiom(eventClass, subEvent));
        }
    };
}
