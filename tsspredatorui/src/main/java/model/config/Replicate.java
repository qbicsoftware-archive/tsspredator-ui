package model.config;

/**
 * Contains all the information about a replicate that are written to the config file
 * @author jmueller
 */
public class Replicate {
    private String replicateID;
    private String treatedCodingStrand, treatedTemplateStrand, untreatedCodingStrand, untreatedTemplateStrand;

    public Replicate() {
    }

    public String getReplicateID() {
        return replicateID;
    }

    public void setReplicateID(String replicateID) {
        this.replicateID = replicateID;
    }

    public String getTreatedCodingStrand() {
        return treatedCodingStrand;
    }

    public void setTreatedCodingStrand(String treatedCodingStrand) {
        this.treatedCodingStrand = treatedCodingStrand;
    }

    public String getTreatedTemplateStrand() {
        return treatedTemplateStrand;
    }

    public void setTreatedTemplateStrand(String treatedTemplateStrand) {
        this.treatedTemplateStrand = treatedTemplateStrand;
    }

    public String getUntreatedCodingStrand() {
        return untreatedCodingStrand;
    }

    public void setUntreatedCodingStrand(String untreatedCodingStrand) {
        this.untreatedCodingStrand = untreatedCodingStrand;
    }

    public String getUntreatedTemplateStrand() {
        return untreatedTemplateStrand;
    }

    public void setUntreatedTemplateStrand(String untreatedTemplateStrand) {
        this.untreatedTemplateStrand = untreatedTemplateStrand;
    }
}
