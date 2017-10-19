package model;

/**
 * Contains all the information about a replicate that are written to the config file
 * @author jmueller
 */
public class Replicate {
    private String replicateID;
    private String enrichedCodingStrand, enrichedTemplateStrand, normalCodingStrand, normalTemplateStrand;

    public Replicate() {
    }

    public String getReplicateID() {
        return replicateID;
    }

    public void setReplicateID(String replicateID) {
        this.replicateID = replicateID;
    }

    public String getEnrichedCodingStrand() {
        return enrichedCodingStrand;
    }

    public void setEnrichedCodingStrand(String enrichedCodingStrand) {
        this.enrichedCodingStrand = enrichedCodingStrand;
    }

    public String getEnrichedTemplateStrand() {
        return enrichedTemplateStrand;
    }

    public void setEnrichedTemplateStrand(String enrichedTemplateStrand) {
        this.enrichedTemplateStrand = enrichedTemplateStrand;
    }

    public String getNormalCodingStrand() {
        return normalCodingStrand;
    }

    public void setNormalCodingStrand(String normalCodingStrand) {
        this.normalCodingStrand = normalCodingStrand;
    }

    public String getNormalTemplateStrand() {
        return normalTemplateStrand;
    }

    public void setNormalTemplateStrand(String normalTemplateStrand) {
        this.normalTemplateStrand = normalTemplateStrand;
    }
}
