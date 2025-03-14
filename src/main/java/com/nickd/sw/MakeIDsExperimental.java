package com.nickd.sw;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityURIConverter;
import org.semanticweb.owlapi.util.OWLEntityURIConverterStrategy;

import java.io.File;
import java.util.*;

public class MakeIDsExperimental {

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {

        Helper helper = new Helper(new File("ontologies/all.owl.ttl"));
        OWLOntologyManager mngr = helper.mngr;

        Set<OWLOntology> onts = mngr.getOntologies();

        mngr.applyChanges(new OWLEntityURIConverter(mngr, onts, new UUIDStrategy(Helper.BASE)).getChanges());

        helper.saveChanged();
    }

    private static class UUIDStrategy implements OWLEntityURIConverterStrategy {
        private final String base;

        public UUIDStrategy(String base) {
            this.base = base;
        }

        @Override
        public IRI getConvertedIRI(OWLEntity e) {
            IRI iri = e.getIRI();
            if (iri.getIRIString().startsWith(base)) {
                String id = "id_" + UUID.randomUUID().toString().replaceAll("-", "_");
                return IRI.create(base + "#" + id);
            }
            return null;
        }
    }
}
