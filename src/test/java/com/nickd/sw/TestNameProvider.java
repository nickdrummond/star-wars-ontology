package com.nickd.sw;

import com.nickd.sw.util.NameProvider;
import junit.framework.TestCase;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

public class TestNameProvider extends TestCase {

    final String something = "Something";

    final OWLDataFactory df = new OWLDataFactoryImpl();
    final OWLNamedIndividual parent = df.getOWLNamedIndividual("parent_event");
    final OWLObjectProperty of = df.getOWLObjectProperty("of");
    final OWLObjectProperty boringProperty = df.getOWLObjectProperty("boring");
    final OWLClassExpression anotherThing = df.getOWLClass("AnotherThing");
    final OWLNamedIndividual individual = df.getOWLNamedIndividual("individual");
    final OWLNamedIndividual individual2 = df.getOWLNamedIndividual("individual2");
    final OWLClassExpression childType = df.getOWLClass(something);

    public void testGetNameUsesNamedClass() {
        String expected = "Something_during_parent_event";

        String actual = new NameProvider().getName(childType, parent);

        assertEquals(expected, actual);
    }

    public void testGetNameDefaultsToEvent() {
        String expected = "Event_during_parent_event";

        OWLObjectHasValue hasValue = df.getOWLObjectHasValue(boringProperty, individual);
        String actual = new NameProvider().getName(hasValue, parent);

        assertEquals(expected, actual);
    }

    public void testGetNameDefaultsToEventType() {
        String expected = "Event_during_parent_event";

        OWLObjectHasValue hasValue = df.getOWLObjectHasValue(boringProperty, individual);
        OWLObjectHasValue hasValue2 = df.getOWLObjectHasValue(boringProperty, individual2);
        OWLObjectIntersectionOf and = df.getOWLObjectIntersectionOf(hasValue, hasValue2);

        String actual = new NameProvider().getName(and, parent);

        assertEquals(expected, actual);
    }

    public void testGetNameWithHasValueOf() {
        String expected = "Something_of_individual_during_parent_event";

        OWLObjectHasValue hasValue = df.getOWLObjectHasValue(of, individual);
        OWLObjectIntersectionOf and = df.getOWLObjectIntersectionOf(childType, hasValue);

        String actual = new NameProvider().getName(and, parent);
        assertEquals(expected, actual);
    }

    public void testGetNameWithMultipleHasValueOf() {
        String expected = "Something_of_individual_and_individual2_during_parent_event";

        OWLObjectHasValue hasValue = df.getOWLObjectHasValue(of, individual);
        OWLObjectHasValue hasValue2 = df.getOWLObjectHasValue(of, individual2);
        OWLObjectIntersectionOf and = df.getOWLObjectIntersectionOf(childType, hasValue, hasValue2);

        String actual = new NameProvider().getName(and, parent);
        assertEquals(expected, actual);
    }

    public void testGetNameWithSomeValuesFromOf() {
        String expected = "Something_of_some_AnotherThing_during_parent_event";

        OWLObjectSomeValuesFrom svf = df.getOWLObjectSomeValuesFrom(of, anotherThing);
        OWLObjectIntersectionOf and = df.getOWLObjectIntersectionOf(childType, svf);

        String actual = new NameProvider().getName(and, parent);
        assertEquals(expected, actual);
    }

    public void testGetNameWithSomeValuesFromOfHadRole() {
        String expected = "Something_of_some_Stormtrooper_during_parent_event";

        OWLObjectProperty hadRole = df.getOWLObjectProperty("hadRole");
        OWLClass stormtrooper = df.getOWLClass("Stormtrooper");
        OWLObjectSomeValuesFrom someHadRoleStormtrooper = df.getOWLObjectSomeValuesFrom(hadRole, stormtrooper);
        OWLObjectSomeValuesFrom svf = df.getOWLObjectSomeValuesFrom(of, someHadRoleStormtrooper);
        OWLObjectIntersectionOf and = df.getOWLObjectIntersectionOf(childType, svf);

        String actual = new NameProvider().getName(and, parent);
        assertEquals(expected, actual);
    }

}
