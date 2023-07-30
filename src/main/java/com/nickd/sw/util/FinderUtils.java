package com.nickd.sw.util;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FinderUtils {

    public static List<OWLEntity> annotationExact(@Nonnull String value,
                                                  @Nonnull OWLAnnotationProperty searchProp,
                                                  @Nonnull Helper helper) {
        return annotationMatchesPredicate(searchProp, helper, ax -> literalValueEquals(ax, value));
    }

    public static List<OWLEntity> annotationContains(@Nonnull String value,
                                                     @Nonnull OWLAnnotationProperty searchProp,
                                                     @Nonnull Helper helper) {
        return annotationMatchesPredicate(searchProp, helper, ax -> literalValueContains(ax, value));
    }

    public static List<OWLEntity> annotationContains(@Nonnull String value,
                                                     @Nonnull OWLAnnotationProperty searchProp,
                                                     @Nonnull EntityType type,
                                                     @Nonnull Helper helper) {
        return annotationContains(value, searchProp, helper).stream()
                .filter(entity -> entity.isType(type))
                .collect(Collectors.toList());
    }

    public static List<OWLEntity> annotationMatchesPredicate(@Nonnull OWLAnnotationProperty searchProp,
                                                             @Nonnull Helper helper,
                                                             @Nonnull Predicate<OWLAnnotationAssertionAxiom> test) {

        return helper.mngr.ontologies()
                .flatMap(o -> o.axioms(AxiomType.ANNOTATION_ASSERTION))
                .filter(ax -> ax.getProperty().equals(searchProp))
                .filter(test)
                .map(OWLAnnotationAssertionAxiom::getSubject)
                .map(OWLAnnotationObject::asIRI)
                .flatMap(Optional::stream)
                .map(helper::entityForIRI)
                .collect(Collectors.toList());
    }

    private static boolean literalValueContains(OWLAnnotationAssertionAxiom ax, String value) {
        OWLAnnotationValue v = ax.getValue();
        return (v.isLiteral() && v.asLiteral().isPresent()
                && v.asLiteral().get().getLiteral().contains(value));
    }

    private static boolean literalValueEquals(OWLAnnotationAssertionAxiom ax, String value) {
        OWLAnnotationValue v = ax.getValue();
        return (v.isLiteral() && v.asLiteral().isPresent() && v.asLiteral().get().getLiteral().equals(value));
    }

}
