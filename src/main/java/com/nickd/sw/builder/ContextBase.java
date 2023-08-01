package com.nickd.sw.builder;

import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ContextBase implements Context {

    @Nonnull
    private final String name;
    private final ContextBase parent;
    private final List<? extends OWLObject> selectedObjects;

    public ContextBase(@Nonnull String name, @Nonnull ContextBase parent, List<? extends OWLObject> selectedObjects) {
        this.name = name;
        this.parent = parent;
        this.selectedObjects = selectedObjects;
    }

    public ContextBase(@Nonnull String name, @Nonnull ContextBase parent, OWLObject owlObject) {
        this(name, parent, Collections.singletonList(owlObject));
    }

    public ContextBase() {
        this("", null, Collections.emptyList());
    }

    public List<? extends OWLObject> getSelectedObjects() {
        return selectedObjects;
    }

    @Override
    public ContextBase getParent() {
        return parent;
    }

    public List<Context> stack(int promptDepth) {
        if (promptDepth == 0 || parent == null) {
            return new ArrayList<>();
        }
        List<Context> stack = parent.stack(promptDepth-1);
        stack.add(this);
        return stack;
    }


    public List<Context> stack() {
        if (parent == null) {
            return new ArrayList<>();
        }
        List<Context> stack = parent.stack();
        stack.add(this);
        return stack;
    }

    @Override
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
        if (!isSingleSelection()) {
            for (int i = 0; i < selectedObjects.size(); i++) {
                OWLObject o = selectedObjects.get(i);
                out.println("\t" + i + ") " + ((o instanceof OWLOntology)
                        ? helper.renderOntology((OWLOntology) o)
                        : helper.render(o)));
            }
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

    @Override
    public boolean isRoot() {
        return parent == null;
    }
}
