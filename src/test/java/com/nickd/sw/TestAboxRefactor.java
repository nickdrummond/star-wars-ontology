package com.nickd.sw;

import com.nickd.sw.util.Helper;
import com.nickd.sw.util.NameProvider;
import com.nickd.sw.util.TestHelper;
import com.nickd.sw.util.TestOntologiesIRIMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

public class TestAboxRefactor extends TestCase {

    private static TestHelper helper;
    private static List<OWLOntologyChange> changes;

    private static OWLObjectProperty includedProperty;
    private static OWLObjectProperty duringProperty;
    private static OWLClass eventClass;
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
        eventClass = helper.cls("Event");

        refactor = new AboxRefactor(includedProperty, duringProperty, eventClass);

        changes = refactor.run(helper.onts(), helper.df(), new NameProvider());

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
                helper.ind("_Killing_of_Droidbait_during_attack_on_rishi_station_simple")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorLinksSubEventBackToEvent() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind("_Killing_of_Droidbait_during_attack_on_rishi_station_simple"),
                helper.ind("Attack_on_Rishi_Station_Simple")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorGeneratesPropertyAssertionsFromHasValue() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                helper.prop("of"),
                helper.ind("_Killing_of_Droidbait_during_attack_on_rishi_station_simple"),
                helper.ind("Droidbait")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorGeneratesClassAssertionFromSome() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("of some BX-series_droid_commando"),
                helper.ind("_Killing_of_some_BX-series_droid_commando_during_attack_on_rishi_station_some")
        ));

        assertThat(changes, hasItem(expected));
    }

    public void testRefactorUsesEventAsDefaultType() {
        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                eventClass,
                helper.ind("_Event_during_attack_on_rishi_station_no_type")
        ));

        assertThat(renderAxioms(changes, "no_type"), changes, hasItem(expected));
    }

    public void testRefactorMultipleSubEvents() {
        AddAxiom expected1 = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind("_Killing_of_some_BX-series_droid_commando_during_attack_on_rishi_station_both"),
                helper.ind("Attack_on_Rishi_Station_Both")
        ));
        AddAxiom expected2 = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind("_Killing_of_Droidbait_during_attack_on_rishi_station_both"),
                helper.ind("Attack_on_Rishi_Station_Both")
        ));


        assertThat(changes, hasItem(expected1));
        assertThat(changes, hasItem(expected2));
    }

    public void testRefactorGeneratesNestedEvents() {
        String subEventName = "_Arrival_of_Cody_during_attack_on_rishi_station_nested";
        String subSubEventName = "_Killing_during__arrival_of_cody_during_attack_on_rishi_station_nested";

        AddAxiom during = new AddAxiom(helper.ont(), df.getOWLObjectPropertyAssertionAxiom(
                duringProperty,
                helper.ind(subSubEventName),
                helper.ind(subEventName)
        ));

        AddAxiom expected = new AddAxiom(helper.ont(), df.getOWLClassAssertionAxiom(
                helper.mos("of some (BX-series_droid_commando and (disguisedAs some Trooper))"),
                helper.ind(subSubEventName)
        ));

        assertThat(renderAxioms(changes, "nested"), changes, hasItem(during));
        assertThat(renderAxioms(changes, "nested"), changes, hasItem(expected));
    }

    private String renderAxioms(List<OWLOntologyChange> changes, String search) {
        return StringUtils.join(changes.stream()
                .map(c -> helper.render(c.getAxiom()))
                .filter(a -> a.contains(search))
                .collect(Collectors.toSet()), "\n");
    }
}
