package com.nickd.sw;

import com.nickd.sw.command.*;
import com.nickd.sw.util.Helper;
import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BuilderController {

    public static final String BREADCRUMB = " > ";

    public static final String PROMPT = " >> ";

    // previous context contains a ?placeholder?
    public static final String placeHolderPattern = "\\?(.)*\\?";

    private final Map<String, Command> commands;

    private final Helper helper;

    public BuilderController(File file) throws OWLOntologyCreationException {
        helper = new Helper(file);

        // STATE needed instead of context
        // valid commands will be dependent on the current state
        commands = Map.of(
                "find", new FindCommand(helper),
                "ont", new OntologiesCommand(helper),
                "ind", new IndividualsCommand(helper),
                "new", new NewInstanceCommand(helper),
                "add", new AddAxiomCommand(helper),
                "?", new DescribeContextCommand(helper),
                "<", new BackContextCommand(helper),
                "<<", new RootContextCommand(helper),
                "undo", new UndoCommand(helper),
                "save", new SaveCommand(helper)
        );
    }

    public void run() {
        Scanner in = new Scanner(System.in);

        Context currentContext = new Context("all", null, helper.ont);

        boolean exit = false;
        while (!exit) {

            System.out.print(buildPrompt(currentContext));

            String s = in.nextLine();

            if (s.isEmpty()) {
                // ignore
            }
            else if (Objects.equals(s, "quit")) {
                exit = true;
            }
            else {
                currentContext = handleInput(s, currentContext);
            }
        }
    }

    private Context handleInput(final String s, final Context currentContext) {
        String[] v = s.split(" ");

        if (v.length == 1) {
            try {
                int n = Integer.parseInt(s);
                List<? extends OWLObject> selectedObjects = currentContext.getSelectedObjects();
                if (n < selectedObjects.size()) {
                    Context c = replacePlaceholderInAncestorContext(selectedObjects.get(n), currentContext);
                    return (c != null) ? c : new Context("", currentContext, selectedObjects.get(n));
                }
            }
            catch (NumberFormatException e) {
                // don't worry, not a number
            }
        }

        Context c = performCommand(s, currentContext);
        if (c != null) {
            return c;
        }

        return currentContext;
    }

    private Context performCommand(String s, Context currentContext) {
        String firstWord = s.split(" ")[0];
        Command command = commands.getOrDefault(firstWord, new NotFoundCommand());
        Context c = command.handle(s, currentContext);
        if (c != currentContext){
            c.describe(System.out, helper);
        }
        return c;
    }

    private Context replacePlaceholderInAncestorContext(OWLObject owlObject, Context currentContext) {
        if (currentContext.isRoot()) {
            return null;
        }

        String name = currentContext.getName();
        String withReplacement = name.replaceFirst(placeHolderPattern, helper.render(owlObject));
        if (!withReplacement.equals(name)) { // we've substituted the placeholder
            return performCommand(withReplacement, currentContext.getParent());
        }

        return replacePlaceholderInAncestorContext(owlObject, currentContext.getParent());
    }

    private String buildPrompt(Context currentContext) {
        List<String> stackRen = currentContext.stack().stream().map(c -> c.toString(helper)).collect(Collectors.toList());
        return  StringUtils.join(stackRen, BREADCRUMB) + PROMPT;
    }
}
