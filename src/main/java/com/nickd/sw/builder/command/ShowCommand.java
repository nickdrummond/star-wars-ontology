package com.nickd.sw.builder.command;

import com.nickd.sw.util.DescriptionVisitorEx;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

public class ShowCommand implements Command {
    private Helper helper;

    public ShowCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(UserInput input, Context context) {
        if (context.isSingleSelection()) {
            OWLObject sel = context.getSelected();
            if (sel instanceof OWLEntity) {
                List<OWLAxiom> axioms = ((OWLEntity)sel).accept(new DescriptionVisitorEx(context.getOntology(helper)));
                return new Context("axioms", context, axioms);
            }
            else {
                return new Context("entities", context, new ArrayList<>(sel.getSignature()));
            }
        }
        return context;
    }


    @Override
    public List<String> autocomplete(UserInput commandStr, Context context) {
        return List.of("Describes the context");
    }
}
