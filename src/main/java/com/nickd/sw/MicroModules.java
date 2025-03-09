package com.nickd.sw;

import com.nickd.sw.util.FileUtils;
import com.nickd.sw.util.Helper;
import com.nickd.sw.util.axioms.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.io.File;
import java.util.Map;

public class MicroModules {

    private static final String TRUE_BASE = "https://nickdrummond.github.io/star-wars-ontology/ontologies";
    private static final String BASE = TRUE_BASE + "#";
    public static final String ONTOLOGY_ID_BASE = TRUE_BASE + "/";
    public static final String UTIL_BASE = "https://nickdrummond.github.io/star-wars-ontology/util#";
    public static final IRI PROPERTIES_ONT = IRI.create(ONTOLOGY_ID_BASE + "properties.owl.ttl");

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
        Helper helper = new Helper(new File("ontologies/all.owl.ttl"));
        OWLOntology input = helper.ont;
        OWLOntologyManager output = OWLManager.createOWLOntologyManager();

        TurtleDocumentFormat format = new TurtleDocumentFormat();
        format.setDefaultPrefix(BASE);
        format.copyPrefixesFrom(Map.of(
                "util", UTIL_BASE,
                "sw-imp", ONTOLOGY_ID_BASE));

        File base = FileUtils.ensureDirectoryExists("compiled/modules/");

        new ModuleBuilder<>(input.getClassesInSignature(Imports.INCLUDED), output)
                .withFormat(format)
                .withPropertiesImport(PROPERTIES_ONT)
                .withExtractor(entity -> new ClassAxiomExtractor(entity, input))
                .build(base);

        new ModuleBuilder<>(input.getIndividualsInSignature(Imports.INCLUDED), output)
                .withFormat(format)
                .withPropertiesImport(PROPERTIES_ONT)
                .withExtractor(entity -> new IndividualAxiomExtractor(entity, input))
                .build(base);


//
//        new ModuleBuilder<>(input.getObjectPropertiesInSignature(Imports.INCLUDED), output)
//                .withFormat(format)
//                .withExtractor(entity -> new ObjectPropertyAxiomExtractor(entity, input))
//                .build(base);
//
//        new ModuleBuilder<>(input.getDataPropertiesInSignature(Imports.INCLUDED), output)
//                .withFormat(format)
//                .withExtractor(entity -> new DataPropertyAxiomExtractor(entity, input))
//                .build(base);
//
//        new ModuleBuilder<>(input.getAnnotationPropertiesInSignature(Imports.INCLUDED), output)
//                .withFormat(format)
//                .withExtractor(entity -> new AnnotationPropertyAxiomExtractor(entity, input))
//                .build(base);

        // TODO remove axioms from the source as they are extracted, to detect the remains - eg disjoints etc
    }

}
