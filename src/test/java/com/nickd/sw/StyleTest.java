package com.nickd.sw;

import com.nickd.sw.util.TestHelper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

public class StyleTest extends TestCase {

    private static TestHelper helper;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(
                new TestSuite(StyleTest.class),
                new File("ontologies/all.owl.ttl"));
        return helper;
    }

    public void testLoadsInLessThan2s() {
        assertTrue(helper.timeToLoad() < 20000);
    }

    public void testIndividualsHaveReferences() {
        final OWLAnnotationProperty seeAlso = helper.df().getOWLAnnotationProperty(RDFS.SEEALSO.toString());

        final Set<OWLNamedIndividual> inds = helper.ont().getIndividualsInSignature().stream().filter(i ->
                helper.ont().getAnnotationAssertionAxioms(i.getIRI()).stream().noneMatch(ann -> ann.getProperty().equals(seeAlso))
        ).collect(Collectors.toSet());

        assertTrue("Missing references: " + inds, inds.isEmpty());
    }

    public void testNoHasValueRestrictionsOnIndividuals() {

        OWLClassExpressionVisitorEx<Boolean> filter = new OWLClassExpressionVisitorEx<>() {
            @Override
            public Boolean visit(OWLObjectHasValue ce) {
                return true;
            }

            @Override
            public <T> Boolean doDefault(T object) {
                return false;
            }
        };

        assertEquals (0, helper
                .getAxioms(AxiomType.CLASS_ASSERTION)
                .filter(ax -> ax.getClassExpression().accept(filter))
                .peek(ax -> System.out.println("hasValue should be property assertion: " + helper.render(ax)))
                .count());
    }
}