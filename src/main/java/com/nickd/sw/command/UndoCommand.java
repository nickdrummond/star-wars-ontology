package com.nickd.sw.command;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class UndoCommand implements Command {
    private final OWLOntologyChangeListener changeListener;
    private Helper helper;
    private Stack<List<? extends OWLOntologyChange>> history = new Stack<>();

    public UndoCommand(Helper helper) {
        this.helper = helper;
        this.changeListener = list -> {
            history.push(list);
        };
        this.helper.mngr.addOntologyChangeListener(changeListener);
    }

    @Override
    public Context handle(String commandStr, Context context) {
        List<? extends OWLOntologyChange> lastChanges = history.pop();
        helper.mngr.applyChanges(reverseChanges(lastChanges));
        return context;
    }

    private List<? extends OWLOntologyChange> reverseChanges(List<? extends OWLOntologyChange> lastChanges) {
        //TODO need to reverse order?
        return lastChanges.stream().map(OWLOntologyChange::reverseChange).collect(Collectors.toList());
    }
}
