package com.nickd.sw;

import com.nickd.sw.util.AxiomSearch;
import com.nickd.sw.util.Helper;
import com.nickd.sw.util.MOS;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;

public class FindAxioms {

    public static void main(String[] args) throws OWLOntologyCreationException {
        Helper helper = new Helper(new File("ontologies/events.owl.ttl"));

        var r = helper.loadReasoner();

        MOS.setToString();

        // Place and at some Thing
        var expr = helper.df.getOWLObjectIntersectionOf(
                helper.cls("Place"),
                helper.df.getOWLObjectSomeValuesFrom(helper.prop("at"), helper.df.getOWLThing())
        );

        AxiomSearch.axiomSearch(r, expr);

        helper.clearReasoner();
    }
}
