package GeoServer;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;

import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.awt.image.ImageWatched;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.LinkedList;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * Class for unzipping a file right before uploading things to Geosever
 * and then deleting the unzipped files after the uploadVector process is finished
 */
public class Unzip {
    GeoLogger logger;


    //TODO call unzip when adding zipped files to Geoserver and then call deleteUnzippedFiles() after uploadVector is done to save space
    public LinkedList<DataverseRecordFile> unzip(String filePath, String destPath, DataverseRecordFile dRF, DataverseJavaObject djo ) throws NullPointerException{
        //String destPath = filePath.substring(0,filePath.length()-4);
        File destDir = new File(destPath);
        destDir.mkdirs();
        Path path = Paths.get(destPath);
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();

        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            logger.error("Something went wrong unzipping" + filePath + ". The file couldn't be found somehow.");
        }
        ZipEntry zipEntry = null;

        if(zis!=null) {
            try {
                zipEntry = zis.getNextEntry();
                do  {
                    zipEntry = zis.getNextEntry();
                    if(zipEntry==null)
                        break;
                    if (GeodisyStrings.fileTypesToIgnore(zipEntry.getName())) {
                        continue;
                    }

                    File newFile = newFile(destDir, zipEntry);
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    if(newFile.getName().toLowerCase().endsWith(".zip"))
                        drfs.addAll(unzip(newFile.getPath(),destPath,dRF,djo));
                    else {
                        DataverseRecordFile zippedFile = new DataverseRecordFile(zipEntry.getName(), dRF.getFileIdent(), dRF.getDbID(), dRF.getServer(), dRF.getDatasetIdent());
                        zippedFile.setOriginalTitle(dRF.getOriginalTitle());
                        drfs.add(zippedFile);
                    }
                } while ((zipEntry != null));
                zis.closeEntry();
                zis.close();
            } catch (FileNotFoundException e) {
                logger.error("Something went wrong creating new file (File wasn't found, somehow) " + filePath);
            }  catch (IOException e) {
                logger.error("Something went wrong unzipping " + dRF.getFileURL());
            }
        }
        return drfs;
    }


    private File newFile(File destDir, ZipEntry zipEntry) {
        File destFile = new File(destDir, zipEntry.getName());

        String destDirPath = null;
        try {
            destDirPath = destDir.getCanonicalPath();
            String destFilePath = destFile.getCanonicalPath();

            if (!destFilePath.startsWith(destDirPath + File.separator)) {
                throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
            }
        } catch (IOException e) {
            logger.error("Something went wrong create file while unzipping " + destDir);
        }
        return destFile;
    }

    public void deleteUnzippedFiles(String filePath){
        File f = new File(filePath.substring(0,filePath.length()-4));
        deleteDir(f);
    }
    private void deleteDir(File f) {
        File[] files = f.listFiles();
        if(files != null) {
            for (File myFile : files) {
                if (myFile.isDirectory()) {
                    deleteDir(myFile);
                }
                myFile.delete();
            }
        }
    }
}
