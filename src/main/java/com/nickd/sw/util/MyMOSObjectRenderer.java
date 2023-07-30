package com.nickd.sw.util;

import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.io.Writer;

public class MyMOSObjectRenderer extends ManchesterOWLSyntaxObjectRenderer {
    public MyMOSObjectRenderer(Writer writer, ShortFormProvider entityShortFormProvider) {
        super(writer, entityShortFormProvider);
    }

    public MyMOSObjectRenderer(Writer writer, boolean explicitXsdString, ShortFormProvider entityShortFormProvider) {
        super(writer, explicitXsdString, entityShortFormProvider);
    }

    @Override
    public void visit(IRI iri) {
        this.write(iri.getRemainder().orElse(iri.getIRIString()));
    }
}
