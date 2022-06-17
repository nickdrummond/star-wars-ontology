package com.nickd.sw;

import org.semanticweb.owlapi.model.OWLOntologyLoaderListener;

public class LoadLogger implements OWLOntologyLoaderListener {
    @Override
    public void startedLoadingOntology(OWLOntologyLoaderListener.LoadingStartedEvent loadingStartedEvent) { }

    @Override
    public void finishedLoadingOntology(OWLOntologyLoaderListener.LoadingFinishedEvent loadingFinishedEvent) {
        System.out.println("Loaded " + loadingFinishedEvent.getOntologyID());
    }
}
