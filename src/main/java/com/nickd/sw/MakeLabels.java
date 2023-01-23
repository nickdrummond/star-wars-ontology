package com.nickd.sw;

import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.StringWriter;
import java.util.*;

public class MakeLabels {

    private final OWLAnnotationProperty targetProperty;
    private final String lang;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {

        Helper helper = new Helper("all.owl.ttl", new StarWarsOntologiesIRIMapper());
        MakeLabels makeLabels = new MakeLabels(helper.df.getOWLAnnotationProperty(IRI.create(RDFS.label.getURI())), "en");
        List<OWLOntologyChange> changes = makeLabels.run(helper.mngr.getOntologies(), helper.df);

        helper.mngr.applyChanges(changes);

        if (changes.isEmpty()) {
            System.out.println("No changes to make");
        }
        else {
            helper.saveChanged();
        }
    }

    public MakeLabels(OWLAnnotationProperty targetProperty, String lang) {
        this.targetProperty = targetProperty;
        this.lang = lang;
    }

    public List<OWLOntologyChange> run(final Set<OWLOntology> onts, OWLDataFactory df) {

        List<OWLOntologyChange> changes = new ArrayList<>();

        OWLEntityVisitor v = new OWLEntityVisitor() {

            private void translate(OWLEntity e) {
                String label = render(e).replaceAll("_", " ");
                OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(targetProperty, e.getIRI(), df.getOWLLiteral(label, lang));
                getDeclarationOntology(e, onts, df).ifPresent(ont -> changes.add(new AddAxiom(ont, ax)));

            }

            @Override
            public void visit(OWLClass ce) {
                translate(ce);
            }

            @Override
            public void visit(OWLNamedIndividual individual) {
                translate(individual);
            }

            @Override
            public void visit(OWLObjectProperty property) {
                translate(property);
            }

            @Override
            public void visit(OWLDataProperty property) {
                translate(property);
            }
        };

        onts.forEach( ont -> ont.getSignature(Imports.EXCLUDED).forEach(ind -> ind.accept(v)));

        return changes;
    }

    public String render(OWLObject o ) {
        final ShortFormProvider sfp = new SimpleShortFormProvider();
        StringWriter w = new StringWriter();
        o.accept(new ManchesterOWLSyntaxObjectRenderer(w, sfp));
        return w.toString();
    }


    private Optional<OWLOntology> getDeclarationOntology(OWLEntity e, Set<OWLOntology> onts, OWLDataFactory df) {
        OWLDeclarationAxiom decl = df.getOWLDeclarationAxiom(e);
        for (OWLOntology o : onts) {
            if (o.containsAxiom(decl)) {
                return Optional.of(o);
            }
        }
        return Optional.empty();
    }
}
