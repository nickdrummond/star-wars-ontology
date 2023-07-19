package com.nickd.sw.command;

import com.nickd.sw.util.FinderUtils;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddAxiomCommand implements Command {
    private final Helper helper;

    public AddAxiomCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context context) {
        String param = commandStr.substring(commandStr.indexOf(" "));

        OWLOntology targetOntology = context.getOntology(helper);
        Optional<OWLEntity> targetEntity = context.getOWLEntity();

        // make subject of entity
        String axiomExpression = targetEntity.map(e -> param.replaceAll("&1", helper.render(e))).orElse(param);

        try {
            OWLAxiom ax = helper.mosAxiom(axiomExpression);

            List<OWLOntologyChange> changes = new ArrayList<>();
            changes.add(new AddAxiom(targetOntology, ax));
            System.out.println("Added " + helper.render(ax) + " to " + helper.renderOntology(targetOntology));

            helper.mngr.applyChanges(changes);
        }
        catch (ParserException e) {
            return createPlaceholderContext(commandStr, e, context);
        }
        return context;
    }

    private Context createPlaceholderContext(String commandStr, ParserException e, Context context) {
        EntityType type = getExpectedType(e);
        String token = e.getCurrentToken();
        List<OWLEntity> entities = FinderUtils.findByAnnotation(token, helper.df.getRDFSLabel(), type, helper);
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
