package com.nickd.sw;

import com.nickd.sw.util.TestHelper;
import com.nickd.sw.util.TestOntologiesIRIMapper;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.owlapi.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

public class MakeDifferentIndividualsTest extends TestCase {

    private static TestHelper helper;

    private static List<OWLOntologyChange> changes;
    private static OWLOntology targetOntology;

    // One time load
    public static junit.framework.Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(
                new TestSuite(MakeDifferentIndividualsTest.class),
                Helper.BASE + "/test-make-different.owl.ttl",
                new TestOntologiesIRIMapper());

        helper.classify();

        targetOntology = helper.ont();

        return helper;
    }

    public void testCreateAllDifferentAxiomWithInferredInstances() {

        MakeDifferentIndividuals action = new MakeDifferentIndividuals(helper.r(), helper.df());

        changes = action.run(helper.cls("Root"), targetOntology);

        Set<OWLNamedIndividual> inds = inds("a1", "a2", "aa1", "ab1", "b1", "c1", "c2", "r1");
        AddAxiom expected = new AddAxiom(targetOntology, helper.df().getOWLDifferentIndividualsAxiom(inds));

        assertTrue(changes.contains(expected));
    }

    public void testRemovesExistingAllDifferentReferencingAffectedIndividual() {

        MakeDifferentIndividuals action = new MakeDifferentIndividuals(helper.r(), helper.df());

        changes = action.run(helper.cls("Root"), targetOntology);

        RemoveAxiom expected1 = new RemoveAxiom(targetOntology, helper.df().getOWLDifferentIndividualsAxiom(inds("a1", "a2")));
        RemoveAxiom expected2 = new RemoveAxiom(targetOntology, helper.df().getOWLDifferentIndividualsAxiom(inds("c1", "c2")));

        assertTrue(changes.contains(expected1));
        assertTrue(changes.contains(expected2));
    }

    public void testOnlyAddOneIndividualWhereSameAsIsInferred() {
        MakeDifferentIndividuals action = new MakeDifferentIndividuals(helper.r(), helper.df());

        changes = action.run(helper.cls("Other"), targetOntology);

        // Not sure how arbitrary the choice of representative element is
        Set<OWLNamedIndividual> inds = inds("o2", "ob1", "oa1", "oa2");
        AddAxiom expected = new AddAxiom(targetOntology, helper.df().getOWLDifferentIndividualsAxiom(inds));

        System.out.println("changes.get(0).getAxiom() = " + changes.get(0).getAxiom());
        assertTrue(changes.contains(expected));
    }

    private Set<OWLNamedIndividual> inds(String... names) {
        return Arrays.stream(names).map(helper::ind).collect(Collectors.toSet());
    }
}