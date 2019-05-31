package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import java.util.List;

public class AstroFields extends MetadataType {
    private SimpleAstroFields simpleAstroFields;
    private Spatial spatialFields;
    private Spectral spectralFields;
    private Temporal temporalFields;
    private Redshift redshiftFields;
    protected String doi;

    public AstroFields(String doi) {
        this.doi = doi;
        this.simpleAstroFields = new SimpleAstroFields();
        this.spatialFields = new Spatial(doi);
        this.spectralFields = new Spectral(doi);
        this.temporalFields = new Temporal(doi);
        this.redshiftFields = new Redshift(doi);

    }

    //TODO implement Class fields

    @Override
    public void setFields(JSONObject jo) {

    }

    @Override
    public List getListField(String fieldName) {
        return null;
    }

    @Override
    public String getDoi() {
        return null;
    }

    @Override
    public void setDoi(String doi) {

    }

    @Override
    public boolean hasBB() {
        return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }
}