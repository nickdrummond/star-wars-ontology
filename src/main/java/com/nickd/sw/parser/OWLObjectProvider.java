package com.nickd.sw.parser;

import org.semanticweb.owlapi.model.*;

public interface OWLObjectProvider {

    OWLObjectPropertyExpression objProp(String key);

    OWLDataPropertyExpression dataProp(String key);

    OWLIndividual ind(String key);

    OWLClassExpression cls(String key);

    OWLLiteral lit(String key);

    OWLClassExpression clsExpr(String key);
}
