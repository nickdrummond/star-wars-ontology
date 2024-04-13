package com.nickd.sw.util.axioms;

import com.google.common.collect.Streams;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.util.Objects;
import java.util.stream.Stream;

public class AxiomExtractor<T extends OWLEntity> implements OWLAxiomVisitorEx<OWLAxiom> {

    protected final T target;

    protected final OWLOntology src;

    public AxiomExtractor(T target, OWLOntology src) {
        this.target = target;
        this.src = src;
    }

    public Stream<OWLAxiom> findAxioms() {
        return Streams.concat(
                src.annotationAssertionAxioms(target.getIRI(), Imports.INCLUDED),
                src.referencingAxioms(target, Imports.INCLUDED)
        ).map(ax -> ax.accept(this))
                .filter(Objects::nonNull);
    }
}
