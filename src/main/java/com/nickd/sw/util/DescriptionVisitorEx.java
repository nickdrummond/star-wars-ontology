package com.nickd.sw.util;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Get axioms from the given ontology that have the entity as subject.
 * Used to build a "description" of the given entity.
 */
public class DescriptionVisitorEx implements OWLEntityVisitorEx<List<OWLAxiom>> {

    private final Set<AxiomType<?>> indTypes = Set.of(
            AxiomType.CLASS_ASSERTION,
            AxiomType.OBJECT_PROPERTY_ASSERTION,
            AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION,
            AxiomType.DATA_PROPERTY_ASSERTION,
            AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION,
            AxiomType.SAME_INDIVIDUAL,
            AxiomType.ANNOTATION_ASSERTION);
    private final Set<AxiomType<?>> clsTypes = Set.of(
            AxiomType.SUBCLASS_OF,
            AxiomType.EQUIVALENT_CLASSES,
            AxiomType.OBJECT_PROPERTY_DOMAIN,
            AxiomType.HAS_KEY,
            AxiomType.ANNOTATION_ASSERTION);

    private final Set<AxiomType<?>> objPropTypes = Set.of(
            AxiomType.SUB_OBJECT_PROPERTY,
            AxiomType.EQUIVALENT_OBJECT_PROPERTIES,
            AxiomType.OBJECT_PROPERTY_DOMAIN,
            AxiomType.OBJECT_PROPERTY_RANGE,
            AxiomType.INVERSE_OBJECT_PROPERTIES,
            AxiomType.FUNCTIONAL_OBJECT_PROPERTY,
            AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY,
            AxiomType.TRANSITIVE_OBJECT_PROPERTY,
            AxiomType.SYMMETRIC_OBJECT_PROPERTY,
            AxiomType.ASYMMETRIC_OBJECT_PROPERTY,
            AxiomType.REFLEXIVE_OBJECT_PROPERTY,
            AxiomType.IRREFLEXIVE_OBJECT_PROPERTY,
            AxiomType.SUB_PROPERTY_CHAIN_OF,
            AxiomType.ANNOTATION_ASSERTION);

    private final Set<AxiomType<?>> dataPropTypes = Set.of(
            AxiomType.SUB_DATA_PROPERTY,
            AxiomType.EQUIVALENT_DATA_PROPERTIES,
            AxiomType.DATA_PROPERTY_DOMAIN,
            AxiomType.DATA_PROPERTY_RANGE,
            AxiomType.FUNCTIONAL_DATA_PROPERTY,
            AxiomType.ANNOTATION_ASSERTION);

    private final Set<AxiomType<?>> annotPropTypes = Set.of(
            AxiomType.SUB_ANNOTATION_PROPERTY_OF,
            AxiomType.ANNOTATION_PROPERTY_DOMAIN,
            AxiomType.ANNOTATION_PROPERTY_RANGE,
            AxiomType.ANNOTATION_ASSERTION);

    private final Set<AxiomType<?>> dtTypes = Set.of(
            AxiomType.DATATYPE_DEFINITION,
            AxiomType.ANNOTATION_ASSERTION);

    private OWLOntology ont;

    public DescriptionVisitorEx(OWLOntology ont) {
        this.ont = ont;
    }

    @Override
    public List<OWLAxiom> visit(OWLClass cls) {
        return getRefs(cls).filter(ax -> ax.isOfType(clsTypes))
                .filter(ax -> isSubject(cls, ax))
                .collect(Collectors.toList());
    }

    @Override
    public List<OWLAxiom> visit(OWLNamedIndividual ind) {
        return getRefs(ind).filter(ax -> ax.isOfType(indTypes))
                .filter(ax -> isSubject(ind, ax))
                .collect(Collectors.toList());
    }

    @Override
    public List<OWLAxiom> visit(OWLObjectProperty property) {
        return getRefs(property).filter(ax -> ax.isOfType(objPropTypes))
                .filter(ax -> isSubject(property, ax))
                .collect(Collectors.toList());
    }

    @Override
    public List<OWLAxiom> visit(OWLDataProperty property) {
        return getRefs(property).filter(ax -> ax.isOfType(dataPropTypes))
                .filter(ax -> isSubject(property, ax))
                .collect(Collectors.toList());
    }

    @Override
    public List<OWLAxiom> visit(OWLAnnotationProperty property) {
        return getRefs(property).filter(ax -> ax.isOfType(annotPropTypes))
                .filter(ax -> isSubject(property, ax))
                .collect(Collectors.toList());
    }

    @Override
    public List<OWLAxiom> visit(OWLDatatype node) {
        return getRefs(node).filter(ax -> ax.isOfType(dtTypes))
                .filter(ax -> isSubject(node, ax))
                .collect(Collectors.toList());
    }

    private boolean isSubject(OWLEntity entity, OWLAxiom ax) {
        return ax.accept(new IsSubjectOfAxiomVisitorEx(entity));
    }

    private Stream<OWLAxiom> getRefs(OWLEntity entity) {
        return Stream.concat(
                ont.referencingAxioms(entity, Imports.INCLUDED),
                ont.annotationAssertionAxioms(entity.getIRI(), Imports.INCLUDED));
    }
}
