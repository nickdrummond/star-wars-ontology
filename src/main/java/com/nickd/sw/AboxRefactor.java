package com.nickd.sw;

import com.github.jsonldjava.shaded.com.google.common.collect.Sets;
import com.nickd.sw.util.Helper;
import com.nickd.sw.util.NameProvider;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.io.File;
import java.util.*;

/**
 * Transforms anonymous sub-events to a named individual.
 * An included some (...) is transformed to a new individual with a during relationship to the parent event.
 *
 * This is a more accurate representation, but is not as easy to author.
 *
 * If the switch "-anon" is used, then anonymous individuals are created, and the included stays - this is NOT
 * usable in Protege, but is rendered inline in the browser
 */
public class AboxRefactor {

    private final String BASE = "https://nickdrummond.github.io/star-wars-ontology/ontologies#";

    private final OWLObjectProperty includedProperty;
    private final OWLObjectProperty duringProperty;
    private final OWLClass eventClass;
    private final NameProvider nameProvider;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        NameProvider nameProvider;

        if (args.length > 0 && args[0].equals("-anon")) {
            nameProvider = null;
        }
        else {
            nameProvider = new NameProvider();
        }

        Helper helper = new Helper(new File("ontologies/all.owl.ttl"));


        AboxRefactor reifyProperty = new AboxRefactor(
                helper.prop("included"),
                helper.prop("during"),
                helper.cls("Event"),
                nameProvider);

        List<OWLOntologyChange> changes = reifyProperty.run(helper.mngr.getOntologies(), helper.df);

        helper.mngr.applyChanges(changes);

        helper.save("compiled/abox" + (nameProvider == null ? "-anon" : ""));
    }

    public AboxRefactor(final OWLObjectProperty includedProperty,
                        final OWLObjectProperty duringProperty,
                        final OWLClass eventClass,
                        final NameProvider nameProvider) {
        this.includedProperty = includedProperty;
        this.duringProperty = duringProperty;
        this.eventClass = eventClass;
        this.nameProvider = nameProvider;
    }

    public List<OWLOntologyChange> run(final Set<OWLOntology> onts,
                                       final OWLDataFactory df) {

        List<OWLOntologyChange> changes = new ArrayList<>();

        OWLAxiomTransformer owlAxiomTransformer = new OWLAxiomTransformer(df, nameProvider);
        
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

    private String render(OWLObject owlObject) {
        if (nameProvider != null) {
            return nameProvider.render(owlObject);
        }
        return owlObject.toString();
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
                    if (nameProvider == null) { // anonymous
                        subEvent = df.getOWLAnonymousIndividual();
                        // retain the includes direction - named events -> include their anon sub-events
                        axioms.add(df.getOWLObjectPropertyAssertionAxiom(includedProperty, parentEvent, subEvent));
                    }
                    else { // named
                        String name = nameProvider.getName(ce.getFiller(), parentEvent);
                        subEvent = df.getOWLNamedIndividual(IRI.create(BASE + "_" + name));
                        // make the sub-event its own first class citizen - using during, referencing the parent event
                        // This is a more consistent hierarchy
                        axioms.add(df.getOWLObjectPropertyAssertionAxiom(duringProperty, subEvent, parentEvent));
                    }
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
            hasNamedType = true;
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

        private void addDefaultType() {
            axioms.add(df.getOWLClassAssertionAxiom(eventClass, subEvent));
        }
    };
}
