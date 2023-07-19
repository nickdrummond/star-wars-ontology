package com.nickd.sw.command;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class SaveCommand implements Command {

    private Helper helper;

    public SaveCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context context) {
        try {
            helper.saveChanged();
        } catch (OWLOntologyStorageException e) {
            System.err.println("Failed to save changed ontologies: " + e.getMessage());
        }
        return context;
    }
}
