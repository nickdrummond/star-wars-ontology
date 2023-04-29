package com.nickd.sw.report;

import com.nickd.sw.util.Helper;
import com.nickd.sw.util.MosParser;
import com.nickd.sw.util.StarWarsOntologiesIRIMapper;
import org.apache.jena.base.Sys;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DLQuerySetSubtraction {

    public static void main(String[] args) throws OWLOntologyCreationException {
        String q1 = "Event and (locatedIn some Thing)";
        String q2 = "Event and (locatedIn value Galaxy)";

        Helper helper = new Helper("events.owl.ttl", new StarWarsOntologiesIRIMapper());
        helper.classify();
        MosParser mos = new MosParser(helper);

        long t = System.nanoTime();
        Set<OWLNamedIndividual> r1 = helper.r.instances(mos.parseCls(q1)).collect(Collectors.toSet());
        System.out.println(r1.size() + " results in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t) + "ms");

        t = System.nanoTime();
        Set<OWLNamedIndividual> r2 = helper.r.instances(mos.parseCls(q2)).collect(Collectors.toSet());
        System.out.println(r2.size() + " results in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t) + "ms");

        Set<OWLNamedIndividual> resultsCopy = new HashSet<>(r1);
        resultsCopy.removeAll(r2);
        System.out.println("r1 - r2 = " + resultsCopy.size());

        resultsCopy.forEach(i -> System.out.println(mos.renderEntity(i)));
    }
}
