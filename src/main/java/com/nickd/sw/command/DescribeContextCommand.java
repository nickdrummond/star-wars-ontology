package com.nickd.sw.command;

import com.nickd.sw.util.DescriptionVisitorEx;
import com.nickd.sw.util.Helper;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

public class DescribeContextCommand implements Command {
    private Helper helper;

    public DescribeContextCommand(Helper helper) {
        this.helper = helper;
    }

    @Override
    public Context handle(String commandStr, Context context) {
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

}
