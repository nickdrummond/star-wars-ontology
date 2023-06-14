package com.nickd.sw;

import com.nickd.sw.util.Helper;
import com.nickd.sw.util.StarWarsOntologiesIRIMapper;
import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.OWLEntityURIConverter;
import org.semanticweb.owlapi.util.OWLEntityURIConverterStrategy;
import org.semanticweb.owlapi.util.ShortFormProvider;
import java.util.*;

public class MakeLabels {

    private final ShortFormProvider sfp;
    private final OWLAnnotationProperty targetProperty;
    private final String lang;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {

        Helper helper = new Helper("all.owl.ttl", new StarWarsOntologiesIRIMapper());
        OWLOntologyManager mngr = helper.mngr;

        OWLAnnotationProperty rdfsLabel = helper.df.getOWLAnnotationProperty(IRI.create(RDFS.label.getURI()));
        OWLAnnotationProperty editorLabel = helper.annotProp("editorLabel", Helper.UTIL_BASE);
        OWLAnnotationProperty legacyId = helper.annotProp("legacyId", Helper.UTIL_BASE);

        // make labels and legacy ID
        ShortFormProvider sfp = owlEntity -> {
            final String s = owlEntity.getIRI().getIRIString();
            return s.substring(s.lastIndexOf("#") + 1);
        };
        ShortFormProvider sfpWithSpaces = owlEntity -> sfp.getShortForm(owlEntity).replaceAll("_", " ");
        ShortFormProvider legacyIdGen = owlEntity -> String.valueOf(owlEntity.getIRI().hashCode());

        Set<OWLOntology> onts = mngr.getOntologies();

        List<OWLOntologyChange> changes = new LinkedList<>();

        changes.addAll(new MakeLabels(editorLabel, "", sfp).run(onts, helper.df));
        changes.addAll(new MakeLabels(rdfsLabel, "en", sfpWithSpaces).run(onts, helper.df));
        changes.addAll(new MakeLabels(legacyId, "", legacyIdGen).run(onts, helper.df));

        if (changes.isEmpty()) {
            System.out.println("No changes to make");
        }
        else {
            // Ensure annotation properties are prefixed
            onts.forEach(o -> {
                OWLDocumentFormat format = mngr.getOntologyFormat(o);
                format.asPrefixOWLDocumentFormat().setPrefix("util", Helper.UTIL_BASE + "#");
            });
            mngr.applyChanges(changes);
            helper.saveChanged();
        }
    }

    public MakeLabels(OWLAnnotationProperty targetProperty, String lang, ShortFormProvider sfp) {
        this.targetProperty = targetProperty;
        this.lang = lang;
        this.sfp = sfp;
    }

    public List<OWLOntologyChange> run(final Set<OWLOntology> onts, OWLDataFactory df) {

        List<OWLOntologyChange> changes = new ArrayList<>();

        OWLEntityVisitor v = new OWLEntityVisitor() {

            private void translate(OWLEntity e) {
                String label = sfp.getShortForm(e);
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

            @Override
            public void visit(OWLAnnotationProperty property) { translate(property); }

            @Override
            public void visit(OWLDatatype node) { translate(node); }
        };

        onts.forEach( ont -> ont.getSignature(Imports.EXCLUDED).forEach(ind -> ind.accept(v)));

        return changes;
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
