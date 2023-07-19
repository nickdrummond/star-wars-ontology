package com.nickd.sw.command;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.*;

public class Context {

    @Nonnull
    private final String name;
    private final Context parent;
    private final List<? extends OWLObject> selectedObjects;

    public Context(@Nonnull String name, @Nonnull Context parent, List<? extends OWLObject> selectedObjects) {
        this.name = name;
        this.parent = parent;
        this.selectedObjects = selectedObjects;
    }

    public Context(@Nonnull String name, @Nonnull Context parent, OWLObject owlObject) {
        this(name, parent, Collections.singletonList(owlObject));
    }

    public Context() {
        this("", null, Collections.emptyList());
    }

    public List<? extends OWLObject> getSelectedObjects() {
        return selectedObjects;
    }

    public Context getParent() {
        return parent;
    }

    public List<Context> stack() {
        if (parent == null) {
            return new ArrayList<>();
        }
        List<Context> stack = parent.stack();
        stack.add(this);
        return stack;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public String toString(Helper helper) {
        int size = selectedObjects.size();
        return (size == 1) ? renderFirst(helper) : name + " (" + size + ")";
    }

    private String renderFirst(Helper helper) {
        OWLObject o = getSelected();
        if (o instanceof OWLOntology) {
            return helper.renderOntology((OWLOntology) o);
        }
        else {
            return helper.render(o);
        }
    }

    public OWLObject getSelected() {
        return selectedObjects.get(0);
    }

    public void describe(PrintStream out, Helper helper) {
            for (int i=0; i<selectedObjects.size(); i++) {
                OWLObject o = selectedObjects.get(i);
                out.println("\t" + i + ") " + ((o instanceof OWLOntology)
                        ? helper.renderOntology((OWLOntology)o)
                        : helper.render(o).replaceAll("\n", "")));
            }
    }

    public boolean isSingleSelection() {
        return selectedObjects.size() == 1;
    }

    public OWLOntology getOntology(Helper helper) {
        if (isSingleSelection()) {
            OWLObject o = getSelected();
            if (o instanceof OWLOntology) {
                return (OWLOntology) o;
            }
        }
        else if (parent == null) {
            return helper.ont; // root
        }

        return parent.getOntology(helper); // check parents
    }


    public Optional<OWLEntity> getOWLEntity() {
        if (isSingleSelection()) {
            OWLObject o = getSelected();
            if (o instanceof OWLEntity) {
                return Optional.of((OWLEntity) o);
            }
        }

        return (parent == null)  ? Optional.empty() : parent.getOWLEntity();
    }

    public Optional<OWLClass> getOWLClass() {
        if (isSingleSelection()) {
            OWLObject o = getSelected();
            if (o instanceof OWLClass) {
                return Optional.of((OWLClass) o);
            }
        }
        else if (parent == null) {
            return Optional.empty();
        }

        return parent.getOWLClass();
    }

    public boolean isRoot() {
        return parent == null;
    }
}
