package com.nickd.sw.builder.command;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SaveCommand implements Command {

    private Logger logger = LoggerFactory.getLogger(SaveCommand.class);

    private Helper helper;

    public SaveCommand(Helper helper) {
        this.helper = helper;
    }


    @Override
    public List<String> autocomplete(UserInput commandStr, Context context) {
        return List.of("Save all changed ontologies");
    }

    @Override
    public Context handle(UserInput commandStr, Context context) {
        try {
            helper.saveChanged();
        } catch (OWLOntologyStorageException e) {
            logger.error("Failed to save changed ontologies", e);
        }
        return context;
    }
}
