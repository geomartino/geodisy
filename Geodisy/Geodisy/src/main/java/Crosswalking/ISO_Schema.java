/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import BaseFiles.GeoLogger;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static Crosswalking.XML.XMLTools.XMLStrings.OPEN_METADATA_LOCAL_REPO;
import static Crosswalking.XML.XMLTools.XMLStrings.TEST_OPEN_METADATA_LOCAL_REPO;


/**
 * Abstract class for general ISO schema crosswalking. Probably will only be for ISO 19115, but I'm including this so
 * Geodisy can more easily be extended to other ISO formats in the future, if necessary.
 * @author pdante
 */
public abstract class ISO_Schema implements XMLSchema {
    GeoLogger logger = new GeoLogger(this.getClass());

    public void saveXMLToFile(Document doc, String doi) {
        saveXMLToFile(doc, doi, false);
    }

    public void saveXMLToFile(Document doc, String doi, boolean testing) {
        if (!doc.toString().contains("Junk_Dont_Use_This_XML"))
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                DOMSource source = new DOMSource(doc);
                String loc = testing ? TEST_OPEN_METADATA_LOCAL_REPO : OPEN_METADATA_LOCAL_REPO;
                File fileDir = genBaseDirs(doi, loc);
                File file = new File(fileDir + "/" + "iso19115.xml");
                FileWriter writer = new FileWriter(file);
                StreamResult result = new StreamResult(writer);

                transformer.transform(source, result);
            } catch (IOException e) {
                logger.error("Something went wrong creating the FileWriter for " + doi);
            } catch (TransformerException e) {
                logger.error("Something went wrong when trying to write " + doi + "xml to the local repo.");
            }
    }

    private String safeDOI(String doi) {
        return doi.replaceAll("/", "_");
    }

    public File genDirs(String doi, String localRepoPath) {
        {
            File fileDir = new File("./" + localRepoPath + doi);
            if (!fileDir.exists())
                fileDir.mkdirs();

            return fileDir;
        }
    }

    public File genBaseDirs(String doi, String localRepoPath) {
        File fileDir = genDirs(doi, localRepoPath);
        try {
            FileUtils.cleanDirectory(fileDir);
        } catch (IOException e) {
            logger.error("Something went wrong cleaning the base directory for the XML/JSON");
        }
        return fileDir;
    }
}

