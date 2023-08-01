package com.nickd.sw.builder.command;

import com.nickd.sw.builder.ContextBase;
import com.nickd.sw.builder.UserInput;
import com.nickd.sw.util.FinderUtils;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FindCommand implements Command {

    Logger logger = LoggerFactory.getLogger(FindCommand.class);

    private final Helper helper;
    private OWLAnnotationProperty defaultLabel;

    public FindCommand(Helper helper, OWLAnnotationProperty defaultLabel) {
        this.helper = helper;
        this.defaultLabel = defaultLabel;
    }

    @Override
    public List<String> autocomplete(UserInput input, ContextBase context) {
        String autocompleteWord = input.autocompleteWord();
        List<String> results = FinderUtils.annotationContains(autocompleteWord, defaultLabel, helper)
                .stream().map(helper::render).collect(Collectors.toList());
        return results;
    }

    @Override
    public ContextBase handle(UserInput input, ContextBase context) {

        List<String> params = input.params();

        if (!params.isEmpty()) {

            String searchFor = params.get(0);

            List<OWLEntity> results = FinderUtils.annotationContains(searchFor, defaultLabel, helper);
            if (results.isEmpty()) {
                logger.warn("Empty results for " + input);
            }
            if (!results.isEmpty()) {
                return new ContextBase(input.fullText(), context, results);
            }
        }
        return context;
    }
}
