package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import DataSourceLocations.DataLocation;
import Dataverse.DataverseAPI;
import Dataverse.SourceAPI;
import Dataverse.DataverseJavaObject;

import DataSourceLocations.Dataverse;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.BASE_DV_URL;


/**
 * This is the main activity class of the tests middleware.
 * From here the calls for harvesting of Dataverse and exporting to Geoserver happen.
 * @author pdante
 */
public class Geodisy {
    public Geodisy() {
    }

    /**
     * Front side of middleware, this part harvests data from Dataverse
     */

    public List<DataverseJavaObject> harvestDataverse() {
        Dataverse dv = new Dataverse();
        String[] dvs = dv.getDataLocationURLs();
        List<DataverseJavaObject> records = new LinkedList<>();
        for (String s : dvs) {
            SourceAPI dVAPI = new DataverseAPI(s);
            LinkedList<DataverseJavaObject> current = dVAPI.harvest();
            for(DataverseJavaObject djo:current){
                records.add(djo);
            }
        }
        return records;
    }


    /** 
     * Creates the universal part of the Dataverse BaseFiles.API search/retrieve
     * URLs "://{database name}/api/". 
     * Will still need to add http/http and whatever is needed at the end.
     */
    
    private String createDataverseURL(String s) {
        return "://" + s + "/api/";
    }

    /**
     * Backside of middleware, this is the part that sends the processed data/metadata to Geoserver
     */
    public void exportToGeoserver(){
        //TODO
    }
}
