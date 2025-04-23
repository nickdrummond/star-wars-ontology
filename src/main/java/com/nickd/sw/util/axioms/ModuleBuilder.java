package com.nickd.sw.util.axioms;

import com.nickd.sw.util.FileUtils;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class ModuleBuilder<T extends OWLEntity> {

    private final Set<T> entities;
    private OWLOntologyManager output;
    private OWLDocumentFormat format;
    private Set<IRI> visited = new HashSet<>();
    private Function<T, AxiomExtractor<T>> extractor;
    private IRI propertiesOnt;

    public ModuleBuilder(Set<T> entities, OWLOntologyManager output) {
        this.entities = entities;
        this.output = output;
    }

    public ModuleBuilder<T> withFormat(OWLDocumentFormat format) {
        this.format = format;
        return this;
    }

    public ModuleBuilder<T> withExtractor(Function<T, AxiomExtractor<T>> extractor) {
        this.extractor = extractor;
        return this;
    }

    public ModuleBuilder<T> withPropertiesImport(IRI propertiesOnt) {
        this.propertiesOnt = propertiesOnt;
        return this;
    }

    public void build(File base) throws OWLOntologyCreationException, OWLOntologyStorageException {
        for (T entity : entities) {
            OWLOntology ont = extractToOntology(entity);
            if (ont != null) {
                FileUtils.save(ont, base);
                output.removeOntology(ont);
            }
        }
    }

    private OWLOntology extractToOntology(T entity) throws OWLOntologyCreationException {
        IRI iri = entity.getIRI();
        if (visited.contains(iri)) {
            System.out.println("already processed cls = " + entity);
            return null;
        }
        visited.add(iri);

        OWLOntology clsOnt = output.createOntology(makeOntIRI(entity));

        if (propertiesOnt != null) {
            output.applyChange(new AddImport(clsOnt, output.getOWLDataFactory().getOWLImportsDeclaration(propertiesOnt)));
        }

        if (format != null) {
            output.setOntologyFormat(clsOnt, format);
        }


        extractor.apply(entity).findAxioms()
                .forEach(ax -> {
                    clsOnt.add(ax);
                    // Only import classes and individuals
                    ax.getIndividualsInSignature().forEach(ref -> {
                        // TODO still getting loops - eg Job
                        if (!ref.equals(entity)) {
                            addImport(clsOnt, ref);
                        }
                    });
                    ax.getClassesInSignature().forEach(ref -> {
                        // TODO still getting loops - eg Job
                        if (!ref.equals(entity)) {
                            addImport(clsOnt, ref);
                        }
                    });
                });
        return clsOnt;
    }

    private IRI makeOntIRI(OWLEntity entity) {
        return IRI.create(entity.getIRI().toString().replace("#", "/") + ".ttl");
    }

    private void addImport(OWLOntology ont, OWLEntity owlEntity) {
        ont.applyChange(new AddImport(ont, output.getOWLDataFactory().getOWLImportsDeclaration(makeOntIRI(owlEntity))));
    }
}
