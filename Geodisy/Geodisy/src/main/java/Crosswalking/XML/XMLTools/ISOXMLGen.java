package Crosswalking.XML.XMLTools;


import Crosswalking.XML.XMLGroups.DataQualityInfo;
import Crosswalking.XML.XMLGroups.DistributionInfo;
import Crosswalking.XML.XMLGroups.IdentificationInfo;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.*;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

import java.util.List;

import static BaseFiles.GeodisyStrings.XMLNS;
import static Dataverse.DVFieldNameStrings.*;

public class ISOXMLGen extends DjoXMLGenerator {
    private CitationFields citationFields;
    private SimpleCitationFields simple;

    XMLDocObject doc;

    public ISOXMLGen(DataverseJavaObject djo) {
        this.djo = djo;
        this.citationFields = djo.getCitationFields();
        this.simple = citationFields.getSimpleCitationFields();
        doc = new XMLDocObject();
        doc.setDoi(djo.getDOI());
    }

    @Override
    public XMLDocObject generateXMLFile(){
        Element rootElement = getRoot();

        //DataIdentification Fields (most of the fields)
        SubElement ident = new IdentificationInfo(djo, doc, rootElement);
        rootElement = ident.getFields();

        //DataQuality Fields
        if(simple.hasField(ACCESS_TO_SOURCES)||simple.hasField(ORIG_OF_SOURCES)||simple.hasField(CHAR_OF_SOURCES)||citationFields.getListField(DATA_SOURCES).size()>0) {
            SubElement dqi = new DataQualityInfo(djo, doc, rootElement);
            rootElement.appendChild(dqi.getFields());
        }

        //Distributor Fields
        List distributors = citationFields.getListField(DISTRIB);
        if(distributors.size()>0||simple.hasField(DEPOSITOR)) {
            SubElement distribution = new DistributionInfo(djo,doc,rootElement,simple, distributors);
            rootElement.appendChild(distribution.getFields());
        }


        //TODO check the following line. I think dealing with other IDs happens elsewhere.
        //if(otherIds.size()>0)
        doc.addRoot(rootElement);
        return doc;
    }
    @Override
    protected Element getRoot() {
        // root element
        Element rootElement = doc.createGMDElement("MD_Metadata");
        rootElement.setAttribute("xmlns",XMLNS + "gmd");
        rootElement.setAttribute("xmlns:gco",XMLNS + "gco");
        rootElement.setAttribute("xmlns:gts",XMLNS + "gts");
        rootElement.setAttribute("xmlns:srv",XMLNS + "srv");
        rootElement.setAttribute("xmlns:gml",XMLNS + "gml");
        rootElement.setAttribute("xmlns:xlink","http://www.w3.org/1999/xlink");
        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        return rootElement;
    }
}
