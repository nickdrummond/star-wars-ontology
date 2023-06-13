package com.nickd.sw;

import com.nickd.sw.util.Helper;
import com.nickd.sw.util.StarWarsOntologiesIRIMapper;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.semanticweb.owlapi.util.ShortFormProvider;
import java.util.*;
import java.util.function.Function;

public class MakeLabels {

    private final ShortFormProvider sfp;
    private final OWLAnnotationProperty targetProperty;
    private final String lang;

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {

        Helper helper = new Helper("all.owl.ttl", new StarWarsOntologiesIRIMapper());
        OWLOntologyManager mngr = helper.mngr;

        OWLAnnotationProperty editorLabel = helper.df.getOWLAnnotationProperty(IRI.create(Helper.BASE + "#editorLabel"));
        OWLAnnotationProperty rdfsLabel = helper.df.getOWLAnnotationProperty(IRI.create(RDFS.label.getURI()));

        // make labels
        ShortFormProvider sfp = owlEntity -> {
            final String s = owlEntity.getIRI().getIRIString();
            return s.substring(s.lastIndexOf("#") + 1);
        };

        ShortFormProvider sfpWithSpaces = owlEntity -> sfp.getShortForm(owlEntity).replaceAll("_", " ");

        mngr.applyChanges(new MakeLabels(editorLabel, "", sfp).run(mngr.getOntologies(), helper.df));
        mngr.applyChanges(new MakeLabels(rdfsLabel, "en", sfpWithSpaces).run(mngr.getOntologies(), helper.df));

        // then regenerate IRIs
        OWLEntityRenamer renamer = new OWLEntityRenamer(mngr, mngr.getOntologies());

        Set<OWLEntity> entities = helper.ont.getSignature(Imports.INCLUDED);
        for (OWLEntity e : entities) {
            IRI iri = e.getIRI();
            if (iri.getIRIString().startsWith(Helper.BASE)) {
                String s = iri.getIRIString();
                int hashPos = s.lastIndexOf("#");
                String prefix = s.substring(0, hashPos);
                String original = s.substring(hashPos + 1);
                int id = original.hashCode();
                String s2 = prefix + "#id" + id;
                mngr.applyChanges(renamer.changeIRI(iri, IRI.create(s2)));
            }
        }

        helper.save("compiled");
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
