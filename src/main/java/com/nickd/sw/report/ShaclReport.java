package com.nickd.sw.report;

import com.nickd.sw.util.JenaHelper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class ShaclReport {

    private static Logger logger = Logger.getLogger(ShaclReport.class.getName());

    public static void main(String[] args) throws IOException {
        Model shapeModel = new JenaHelper().getTurtleModelFor("shacl/constraints.owl.ttl");
        Model dataModel = new JenaHelper().getModelWithImportsFor("ontologies/all.owl.ttl");

        logger.info("Validating...");
        Resource reportResource = ValidationUtil.validateModel(dataModel, shapeModel, true);
        boolean conforms  = reportResource.getProperty(SH.conforms).getBoolean();
        logger.info("Conforms = " + conforms);

        File reportFile = new File("shacl-report.ttl");
        reportFile.createNewFile();
        logger.info("reportFile = " + reportFile.getAbsolutePath());
        OutputStream reportOutputStream = new FileOutputStream(reportFile);

        RDFDataMgr.write(reportOutputStream, reportResource.getModel(), RDFFormat.TURTLE);
    }

}
