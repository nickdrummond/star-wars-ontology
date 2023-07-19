package com.nickd.sw;

import com.nickd.sw.util.Helper;
import junit.framework.TestCase;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.io.File;
import java.util.List;

public class TestReifyIntegration extends TestCase {

    public void testTransformLeavesNoUsesOfReifiedProperty() throws OWLOntologyCreationException {
        Helper helper = new Helper(new File("ontologies/all.owl.ttl"));

        OWLObjectProperty killingOf = helper.prop("killingOf");
        OWLObjectProperty ofProperty = helper.prop("of");
        OWLObjectProperty includedProperty = helper.prop("included");
        OWLClass killingCls = helper.cls("Killing");

        ReifyProperty reifyProperty = new ReifyProperty(killingOf, killingCls, ofProperty, includedProperty);

        List<OWLOntologyChange> changes = reifyProperty.run(helper.mngr.getOntologies(), helper.mngr.getOWLDataFactory());

        helper.mngr.applyChanges(changes);

        helper.ont.getReferencingAxioms(killingOf, Imports.INCLUDED).forEach( ax -> {
            assertTrue("Axiom missed in transform: " + ax, (ax instanceof OWLObjectPropertyAxiom) || (ax instanceof OWLDeclarationAxiom));
        });
    }
}


