package com.nickd.sw.util;

import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class NameProvider {

    private final Map<String, Integer> eventChildCount = Maps.newHashMap();

    public String render(OWLObject o) {
        final ShortFormProvider sfp = new SimpleShortFormProvider();
        StringWriter w = new StringWriter();
        o.accept(new ManchesterOWLSyntaxObjectRenderer(w, sfp));
        return w.toString();
    }

    public String getName(OWLClassExpression ce, OWLIndividual parentEvent) {
        String name = getDescription(ce) + "_during_" + render(parentEvent).toLowerCase();
        int count = eventChildCount.getOrDefault(name, 0);
        eventChildCount.put(name, count+1);
        if (count > 0) {
            System.out.println("Multiple instances of " + name);
            return name + "_" + count;
        }
        else {
            return name;
        }
    }

    private String getDescription(OWLClassExpression ce) {
        OWLClassExpressionVisitorEx<String> v = new OWLClassExpressionVisitorEx<>() {

            @Override
            public String visit(OWLClass ce) {
                return render(ce);
            }

            @Override
            public String visit(OWLObjectIntersectionOf ce) {
                List<String> ofs = Lists.newArrayList();
                String n = "";
                for (OWLClassExpression e : ce.getOperands()) {
                    if (e.isNamed()) {
                        n = render(e);
                    }
                    else if (e instanceof OWLObjectHasValue) {
                        OWLObjectHasValue value = (OWLObjectHasValue) e;
                        if (render(value.getProperty()).equals("of")) {
                            ofs.add(render(value.getFiller()));
                        }
                    }
                    else if (e instanceof OWLObjectSomeValuesFrom) {
                        OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) e;
                        if (render(some.getProperty()).equals("of")) {
                            if (some.getFiller().isNamed()) {
                                ofs.add("some_" + render(some.getFiller()));
                            }
                            else if (some.getFiller() instanceof OWLObjectSomeValuesFrom) {
                                OWLObjectSomeValuesFrom fillerSVF = ((OWLObjectSomeValuesFrom) some.getFiller());
                                if (render(fillerSVF.getProperty()).equals("hadRole")
                                        && fillerSVF.getFiller().isNamed()) {
                                    ofs.add("some_" + render(fillerSVF.getFiller()));
                                }
                            }
                            else {
                                System.err.println("Filler: " + render(some.getFiller()));
                                // extract hasROle!!!
                            }
                        }
                    }
                }

                if (n.isEmpty()) {
                    n = "Event";
                }

                return (ofs.isEmpty()) ? n : n + "_of_" + StringUtils.join(ofs, "_and_");
            }

            @Override
            public <T> String doDefault(T object) {
                return "Event";
            }
        };
        return ce.accept(v);
    }
}
