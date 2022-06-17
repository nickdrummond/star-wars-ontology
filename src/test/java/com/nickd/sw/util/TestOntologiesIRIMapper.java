package com.nickd.sw.util;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;

import javax.annotation.Nullable;
import java.util.Objects;

public class TestOntologiesIRIMapper implements OWLOntologyIRIMapper {
    @Nullable
    @Override
    public IRI getDocumentIRI(IRI iri) {
        return iri.getRemainder().
                map(rem -> IRI.create(Objects.requireNonNull(getClass().getClassLoader().getResource(rem))))
                .orElseThrow(IllegalArgumentException::new);
    }
}
