package com.nickd.sw;

import com.nickd.sw.util.Helper;
import com.nickd.sw.util.TestHelper;
import com.nickd.sw.util.TestOntologiesIRIMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.owlapi.model.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class TestAboxRefactor extends TestCase {

    private static TestHelper helper;
    private static List<OWLOntologyChange> changes;

    private static OWLObjectProperty includedProperty;
    private static OWLObjectProperty duringProperty;
    private static OWLDataFactory df;

    private static AboxRefactor refactor;

    // One time load
    public static Test suite() throws OWLOntologyCreationException {
        helper = new TestHelper(
                new TestSuite(TestAboxRefactor.class),
                Helper.BASE + "/test-refactor-abox.owl.ttl",
                new TestOntologiesIRIMapper());
        df = helper.df();

        includedProperty = helper.prop("included");
        duringProperty = helper.prop("during");

        refactor = new AboxRefactor(includedProperty, duringProperty);

        changes = refactor.run(helper.onts(), helper.df());

        return helper;
    }

    public void testRefactorRemovesSubclassAssertions() {
        RemoveAxiom expected = new RemoveAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("included some(Killing and (of value Droidbait))"),
                helper.ind("Attack_on_Rishi_Station_Simple")));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorCreatesNewSubEvent() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.cls("Killing"),
                helper.ind("_during_attack_on_rishi_station_simple_0")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorLinksSubEventBackToEvent() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind("_during_attack_on_rishi_station_simple_0"),
                helper.ind("Attack_on_Rishi_Station_Simple")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorGeneratesPropertyAssertionsFromHasValue() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                helper.prop("of"),
                helper.ind("_during_attack_on_rishi_station_simple_0"),
                helper.ind("Droidbait")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorGeneratesClassAssertionFromSome() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("of some BX-series_droid_commando"),
                helper.ind("_during_attack_on_rishi_station_some_0")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorMultipleSubEvents() {
        AddAxiom expected1 = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind("_during_attack_on_rishi_station_both_0"),
                helper.ind("Attack_on_Rishi_Station_Both")
        ));
        AddAxiom expected2 = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind("_during_attack_on_rishi_station_both_1"),
                helper.ind("Attack_on_Rishi_Station_Both")
        ));


        assertThat(changes, hasItem(expected1));
        assertThat(changes, hasItem(expected2));
    }

    public void testRefactorGeneratesNestedEvents() {
        String subEventName = "_during_attack_on_rishi_station_nested_0";
        String subSubEventName = "_during__during_attack_on_rishi_station_nested_0_0";

        AddAxiom during = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind(subSubEventName),
                helper.ind(subEventName)
        ));

        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("of some (BX-series_droid_commando and (disguisedAs some Trooper))"),
                helper.ind(subSubEventName)
        ));

        assertThat(changes, hasItem(during));
        assertThat(changes, hasItem(expected));
    }
}
