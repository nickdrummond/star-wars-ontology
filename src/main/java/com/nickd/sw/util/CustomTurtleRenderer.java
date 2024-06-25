package com.nickd.sw.util;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.rdf.turtle.renderer.TurtleRenderer;

import java.io.Writer;

// Remove comments and large indenting
public class CustomTurtleRenderer extends TurtleRenderer {

    public CustomTurtleRenderer(OWLOntology ontology, Writer writer, OWLDocumentFormat format) {
        super(ontology, writer, format);
    }

    @Override
    protected void writeClassComment(OWLClass cls) {
        // nothing
    }

    @Override
    protected void writeObjectPropertyComment(OWLObjectProperty prop) {
        // nothing
    }

    @Override
    protected void writeDataPropertyComment(OWLDataProperty prop) {
        // nothing
    }

    @Override
    protected void writeIndividualComments(OWLNamedIndividual ind) {
        // nothing
    }

    @Override
    protected void writeAnnotationPropertyComment(OWLAnnotationProperty prop) {
        // nothing
    }

    @Override
    protected void writeDatatypeComment(OWLDatatype datatype) {
        // nothing
    }

    @Override
    protected void writeBanner(String name) {
        // nothing
    }

    @Override
    protected void pushTab() {
        int last = tabs.isEmpty() ? 1 : tabs.peek();
        tabs.push(last + 1);
    }
}
