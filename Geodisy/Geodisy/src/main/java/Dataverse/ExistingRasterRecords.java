package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import static BaseFiles.GeodisyStrings.RASTER_RECORDS;


public class ExistingRasterRecords extends ExisitingFile implements Serializable {
    private static final long serialVersionUID = 5416853597895403201L;
    private HashMap<String, String> records;
    private static ExistingRasterRecords single_instance = null;

    public static ExistingRasterRecords getExistingRasters() {
        if (single_instance == null) {
            single_instance = new ExistingRasterRecords();
        }
        return single_instance;
    }

    private ExistingRasterRecords(){
        logger = new GeoLogger(this.getClass());
        records = readExistingRecords(RASTER_RECORDS);
    }

    public void addOrReplaceRecord(String doi, String fileName){
        records.put(doi+fileName,fileName);
    }

    public HashMap<String, String> readExistingRecords(String path){
        HashMap<String, String> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        try {
            records =  (HashMap<String, String>) fw.readSavedObject(GeodisyStrings.replaceSlashes(path));
            return records;
        } catch (IOException e) {
            logger.error("Something went wrong reading " + path);
            return newFile;
        } catch (ClassNotFoundException e) {
            logger.error("Something went wrong parsing " + path);
            return newFile;
        } catch (NullPointerException e){
            return newFile;
        }
    }
    public void removeRecord(String doi, String filename){
        if(hasRecord(doi, filename));
            records.remove(doi+filename);
    }

    public HashMap<String, String> getRecords() {
        return records;
    }

    public boolean hasRecord(String doi, String filename){
        return records.containsKey(doi+filename);
    }
}