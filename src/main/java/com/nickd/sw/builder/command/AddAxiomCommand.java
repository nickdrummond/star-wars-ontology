package com.nickd.sw.builder.command;

import com.nickd.sw.util.FinderUtils;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddAxiomCommand implements Command {

    Logger logger = LoggerFactory.getLogger(AddAxiomCommand.class);

    private final Helper helper;

    public AddAxiomCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public List<String> autocomplete(UserInput input, Context context) {
        return FinderUtils.annotationContains(input.autocompleteWord(), helper.df.getRDFSLabel(), helper).stream()
                .map(helper::render).collect(Collectors.toList());
    }

    @Override
    public Context handle(UserInput commandStr, Context context) {
        String param = commandStr.paramsAsString();

        OWLOntology targetOntology = context.getOntology(helper);
        Optional<OWLEntity> targetEntity = context.getOWLEntity();

        // TODO NO - lets make this a general rule - replace &n with the previous context item n
        // make subject of entity
        String axiomExpression = targetEntity.map(e -> param.replaceAll("&1", helper.render(e))).orElse(param);

        try {
            OWLAxiom ax = helper.mosAxiom(axiomExpression);

            List<OWLOntologyChange> changes = new ArrayList<>();
            changes.add(new AddAxiom(targetOntology, ax));
            logger.debug("Added " + helper.render(ax) + " to " + helper.renderOntology(targetOntology));

            helper.mngr.applyChanges(changes);
        }
        catch (ParserException e) {
            logger.debug(e.getMessage());
            return createPlaceholderContext(commandStr.fullText(), e, context);
        }
        return context;
    }

    private Context createPlaceholderContext(String commandStr, ParserException e, Context context) {
        EntityType type = getExpectedType(e);
        String token = e.getCurrentToken();
        List<OWLEntity> entities = FinderUtils.annotationContains(token, helper.df.getRDFSLabel(), type, helper);
        String s = commandStr.replace(token, "?" + token + "?");
        return new Context(s, context, entities);
    }

    private EntityType getExpectedType(ParserException e) {
        if (e.isIndividualNameExpected()) {
            return EntityType.NAMED_INDIVIDUAL;
        }
        else if (e.isClassNameExpected()) {
            return EntityType.CLASS;
        }
        else if (e.isObjectPropertyNameExpected()) {
            return EntityType.OBJECT_PROPERTY;
        }
        else if (e.isDataPropertyNameExpected()) {
            return EntityType.DATA_PROPERTY;
        }
        else if (e.isAnnotationPropertyNameExpected()) {
            return EntityType.ANNOTATION_PROPERTY;
        }
        else if (e.isDatatypeNameExpected()) {
            return EntityType.DATATYPE;
        }
        throw new RuntimeException("Unknown expected type");
    }
}
