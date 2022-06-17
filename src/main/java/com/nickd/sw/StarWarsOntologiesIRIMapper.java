package com.nickd.sw;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;

import javax.annotation.Nullable;
import java.io.File;

public class StarWarsOntologiesIRIMapper implements OWLOntologyIRIMapper {
    public static final String BASE = "ontologies/";

    @Nullable
    @Override
    public IRI getDocumentIRI(IRI iri) {
        return iri.getRemainder().
                map(rem -> IRI.create(new File(BASE + rem)))
                .orElseThrow(IllegalArgumentException::new);
    }
}
