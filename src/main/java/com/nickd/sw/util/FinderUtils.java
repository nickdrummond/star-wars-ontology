package com.nickd.sw.util;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class FinderUtils {

    public static List<OWLEntity> findByAnnotation(@Nonnull String value,
                                                    @Nonnull OWLAnnotationProperty searchProp,
                                                    @Nonnull Helper helper) {
        return helper.mngr.ontologies()
                .flatMap(o -> o.axioms(AxiomType.ANNOTATION_ASSERTION))
                .filter(ax -> ax.getProperty().equals(searchProp))
                .filter(ax -> literalValueContains(ax, value))
                .map(OWLAnnotationAssertionAxiom::getSubject)
                .filter(OWLAnnotationSubject::isIRI) // ignore anon instances
                .map(iri -> helper.entityForIRI(iri.asIRI().get()))
                .collect(Collectors.toList());
    }

    public static List<OWLEntity> findByAnnotation(@Nonnull String value,
                                                   @Nonnull OWLAnnotationProperty searchProp,
                                                   @Nonnull EntityType type,
                                                   @Nonnull Helper helper) {
        return findByAnnotation(value, searchProp, helper).stream()
                .filter(entity -> entity.isType(type))
                .collect(Collectors.toList());
    }

    private static boolean literalValueContains(OWLAnnotationAssertionAxiom ax, String value) {
        OWLAnnotationValue v = ax.getValue();
        return (v.isLiteral() && v.asLiteral().isPresent() && v.asLiteral().get().getLiteral().contains(value));
    }
}
