package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains all the information about a genome that are written to the config file
 * @author jmueller
 */
public class Genome {
    private String name;
    private String fasta;
    private String alignmentID;
    private String gff;
    private ArrayList<Replicate> replicateList;

    public Genome(){
        replicateList = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFasta() {
        return fasta;
    }

    public void setFasta(String fasta) {
        this.fasta = fasta;
    }

    public String getAlignmentID() {
        return alignmentID;
    }

    public void setAlignmentID(String alignmentID) {
        this.alignmentID = alignmentID;
    }

    public String getGff() {
        return gff;
    }

    public void setGff(String gff) {
        this.gff = gff;
    }

    public ArrayList<Replicate> getReplicateList() {
        return replicateList;
    }

    public void setReplicateList(ArrayList<Replicate> replicateList) {
        this.replicateList = replicateList;
    }
}
