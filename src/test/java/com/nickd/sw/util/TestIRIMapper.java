package com.nickd.sw.util;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;

import javax.annotation.Nullable;
import java.io.File;

public class TestIRIMapper implements OWLOntologyIRIMapper {
    public static final String BASE = "ontologies/";

    @Nullable
    @Override
    public IRI getDocumentIRI(IRI iri) {
        return iri.getRemainder().
                map(rem -> IRI.create(new File(BASE + rem)))
                .orElseThrow(IllegalArgumentException::new);
    }
}
