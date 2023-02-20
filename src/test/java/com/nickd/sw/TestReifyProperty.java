package com.nickd.sw;

import com.nickd.sw.util.Helper;
import com.nickd.sw.util.TestHelper;
import com.nickd.sw.util.TestOntologiesIRIMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.hamcrest.CoreMatchers.*;
import org.semanticweb.owlapi.model.*;

import java.util.List;

import static org.junit.Assert.assertThat;

public class TestReifyProperty extends TestCase {

    private static TestHelper helper;
    private static List<OWLOntologyChange> changes;

    private static OWLObjectProperty killingOf;
    private static OWLObjectProperty ofProperty;
    private static OWLObjectProperty includedProperty;
    private static OWLClass killingCls;
    private static ReifyProperty reifyProperty;
    private static OWLDataFactory df;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(
                new TestSuite(TestReifyProperty.class),
                Helper.BASE + "/test-reification.owl.ttl",
                new TestOntologiesIRIMapper());
        df = helper.df();

        killingOf = helper.prop("killingOf");
        ofProperty = helper.prop("of");
        includedProperty = helper.prop("included");
        killingCls = helper.cls("Killing");

        reifyProperty = new ReifyProperty(killingOf, killingCls, ofProperty, includedProperty);

        changes = reifyProperty.run(helper.onts(), helper.df());

        return helper;
    }

    public void testTransformPropertyAssertion() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("included some (Killing and (of value Beru_Whitesun_Lars))"),
                helper.ind("Attack_on_Lars_Homestead")));

        assertThat(changes, hasItem(expected));
    }

    public void testTransformClassAssertion() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("included some (Killing and (of some Jawa))"),
                helper.ind("Attack_on_Lars_Homestead")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testTransformSubevent() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("included some (Crash" +
                        " and (included some (Killing and (of value Jun_Sato)))" +
                        " and (included some (Killing and (of value Kassius_Konstantine)))" +
                        " and (destructionOf value Phoenix_Nest))"),
                helper.ind("Battle_of_Atollon")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testTransformSubeventGeneric() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("included some (Evacuation" +
                        " and (included some (Killing and (of some (hadRole some Captain))))" +
                        " and (of value Anodyne))"),
                helper.ind("Battle_of_Oetchi")
        ));

        assertThat(changes, hasItem(expected));
    }
}


