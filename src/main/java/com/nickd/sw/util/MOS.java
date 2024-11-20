package com.nickd.sw.util;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.StringWriter;

public class MOS {

    public static void setToString() {
        MOSObjectRenderer mosObjectRenderer = new MOSObjectRenderer();
        mosObjectRenderer.setShortFormProvider(new SimpleShortFormProvider());
        ToStringRenderer.setRenderer(() -> mosObjectRenderer);
    }

    private static class MOSObjectRenderer implements OWLObjectRenderer {
        private ShortFormProvider sfp;

        @Override
        public void setShortFormProvider(ShortFormProvider shortFormProvider) {
            this.sfp = shortFormProvider;
        }

        @Override
        public String render(OWLObject owlObject) {
            StringWriter writer = new StringWriter();
            var mos = new ManchesterOWLSyntaxObjectRenderer(writer, sfp);
            owlObject.accept(mos);
            return writer.toString().replace("\n", " ");
        }
    }
}
