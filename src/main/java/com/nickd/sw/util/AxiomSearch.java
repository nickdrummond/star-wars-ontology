package com.nickd.sw.util;


import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLObjectWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Find axioms with a class component that satisfies the given expression<br>
 * eg axiomSearch(q some Thing) will return the axiom
 * A -> p some (B and q some C)
 */
public class AxiomSearch {

    private static final Logger log = LoggerFactory.getLogger(AxiomSearch.class);

    public record AxiomMatch(OWLAxiom axiom, OWLClassExpression matchingClassExpression){}

    public static Set<AxiomMatch> axiomSearch(OWLReasoner r, OWLClassExpression clsExpr) {
        var ont = r.getRootOntology();
        var df = ont.getOWLOntologyManager().getOWLDataFactory();
        Set<OWLLogicalAxiom> axioms = ont.getLogicalAxioms(Imports.INCLUDED);
        log.info("{} axioms", axioms.size());
        var walker = new OWLObjectWalker<>(axioms);
        var matchingAxioms = new HashSet<AxiomMatch>();
        walker.walkStructure(new OWLObjectVisitor() {
            int processed = 0;
            OWLAxiom currentAxiom;

            @Override
            public void doDefault(Object object) {
                if (object instanceof OWLAxiom axiom) {
                    processed++;
                    if (processed % 100 == 0) {
                        log.info("... {}", processed);
                    }
                    currentAxiom = axiom;
                }
                else if (object instanceof OWLClassExpression clsExpression
                        && !clsExpression.isNamed()
                        && r.isEntailed(df.getOWLSubClassOfAxiom(clsExpression, clsExpr))) {
                    AxiomMatch match = new AxiomMatch(currentAxiom, clsExpression);
                    log.info("match {}", match);
                    matchingAxioms.add(match);
                }
            }
        });
        return matchingAxioms;
    }
}

