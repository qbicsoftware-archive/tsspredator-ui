package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author jmueller
 */
public class ConfigFile {
    private String projectName;
    private int numberOfDatasets;
    private int numberOfReplicates;
    private boolean isModeConditions;
    private String alignmentFile;
    private ArrayList<Genome> genomeList;

    private boolean writeGraphs;
    private double stepHeight;
    private double stepHeightReduction;
    private double stepFactor;
    private double stepFactorReduction;
    private double enrichmentFactor;
    private double processingSiteFactor;
    private int stepLength;
    private double baseHeight;

    private double normalizationPercentile;
    private double enrichmentNormalizationPercentile;
    private String clusterMethod;
    private int tssClusteringDistance;
    private int allowedCrossSubjectShift;
    private int allowedCrossReplicateShift;
    private int matchingReplicates;
    private int utrLength;
    private int antisenseUtrLength;

    public ConfigFile() {
    }

    public void writeConfigFile(File output) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            writeLine(bw, "projectName", projectName);
            writeLine(bw, "numberOfDatasets", Integer.toString(numberOfDatasets));
            writeLine(bw, "numReplicates", Integer.toString(numberOfReplicates));
            if (!isModeConditions)
                writeLine(bw, "xmfa", alignmentFile);
            writeLine(bw, "writeGraphs", writeGraphs ? "1" : "0");
            for (Genome genome : genomeList) {
                writeLine(bw, "genome_" + genome.getAlignmentID(), genome.getFasta());
                writeLine(bw, "annotation_" + genome.getAlignmentID(), genome.getGff());
                writeLine(bw, "outputPrefix_" + genome.getAlignmentID(), genome.getName());
                for (Replicate replicate : genome.getReplicateList()) {
                    writeLine(bw, "fivePrimePlus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getEnrichedCodingStrand());
                    writeLine(bw, "fivePrimeMinus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getEnrichedTemplateStrand());
                    writeLine(bw, "normalPlus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getNormalCodingStrand());
                    writeLine(bw, "normalMinus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getNormalTemplateStrand());
                }
            }
            StringBuilder idList = new StringBuilder();
            for (Genome genome : genomeList) {
                idList.append(genome.getAlignmentID()).append(",");
            }
            writeLine(bw, "allowedCompareShift", Integer.toString(allowedCrossSubjectShift));
            writeLine(bw, "allowedRepCompareShift", Integer.toString(allowedCrossReplicateShift));
            writeLine(bw, "idList", idList.toString().substring(0, idList.length() - 1));
            writeLine(bw, "maxUTRlength", Integer.toString(utrLength));
            writeLine(bw, "maxASutrLength", Integer.toString(antisenseUtrLength));
            writeLine(bw, "maxNormalTo5primeFactor", Double.toString(processingSiteFactor));
            writeLine(bw, "maxTSSinClusterDistance", Integer.toString(tssClusteringDistance));
            writeLine(bw, "min5primeToNormalFactor", Double.toString(enrichmentFactor));
            writeLine(bw, "minCliffFactor", Double.toString(stepFactor));
            writeLine(bw, "minCliffFactorDiscount", Double.toString(stepFactorReduction));
            writeLine(bw, "minCliffHeight", Double.toString(stepHeight));
            writeLine(bw, "minCliffHeightDiscount", Double.toString(stepHeightReduction));
            writeLine(bw, "minNormalHeight", Double.toString(baseHeight));
            writeLine(bw, "minNumRepMatches", Integer.toString(matchingReplicates));
            writeLine(bw, "minPlateauLength", Integer.toString(stepLength));
            writeLine(bw, "mode", isModeConditions ? "cond" : "align");
            writeLine(bw, "normPercentile", Double.toString(normalizationPercentile));
            writeLine(bw, "textNormPercentile", Double.toString(enrichmentNormalizationPercentile));
            writeLine(bw, "TSSinClusterSelectionMethod", clusterMethod);


            //TODO: These values appear in the config file, but aren't customisable yet
            writeLine(bw, "maxGapLengthInGene", "42");
            writeLine(bw, "superGraphCompatibility", "igb");
            writeLine(bw, "writeNocornacFiles", "0");

            bw.close();

        } catch (NullPointerException npe) {
            System.err.println("Config file couldn't be created! Something seems to be missing...");
        } catch (IOException e) {
            System.err.println("Output path invalid");
        }

    }

    private void writeLine(BufferedWriter bw, String key, String value) throws IOException {
        if (value == null)
            System.err.println("Couldn't create config file! Parameter \'" + key + "\' isn't set.");
        else
            bw.write(key + " = " + value + "\n");

    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getNumberOfDatasets() {
        return numberOfDatasets;
    }

    public void setNumberOfDatasets(int numberOfDatasets) {
        this.numberOfDatasets = numberOfDatasets;
    }

    public int getNumberOfReplicates() {
        return numberOfReplicates;
    }

    public void setNumberOfReplicates(int numberOfReplicates) {
        this.numberOfReplicates = numberOfReplicates;
    }

    public void setModeConditions(boolean modeConditions) {
        isModeConditions = modeConditions;
    }

    public void setAlignmentFile(String alignmentFile) {
        this.alignmentFile = alignmentFile;
    }

    public ArrayList<Genome> getGenomeList() {
        return genomeList;
    }

    public void setGenomeList(ArrayList<Genome> genomeList) {
        this.genomeList = genomeList;
    }

    public void setWriteGraphs(boolean writeGraphs) {
        this.writeGraphs = writeGraphs;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

    public void setStepHeightReduction(double stepHeightReduction) {
        this.stepHeightReduction = stepHeightReduction;
    }

    public void setStepFactor(double stepFactor) {
        this.stepFactor = stepFactor;
    }

    public void setStepFactorReduction(double stepFactorReduction) {
        this.stepFactorReduction = stepFactorReduction;
    }

    public void setEnrichmentFactor(double enrichmentFactor) {
        this.enrichmentFactor = enrichmentFactor;
    }

    public void setProcessingSiteFactor(double processingSiteFactor) {
        this.processingSiteFactor = processingSiteFactor;
    }

    public void setStepLength(int stepLength) {
        this.stepLength = stepLength;
    }

    public void setBaseHeight(double baseHeight) {
        this.baseHeight = baseHeight;
    }

    public void setNormalizationPercentile(double normalizationPercentile) {
        this.normalizationPercentile = normalizationPercentile;
    }

    public void setEnrichmentNormalizationPercentile(double enrichmentNormalizationPercentile) {
        this.enrichmentNormalizationPercentile = enrichmentNormalizationPercentile;
    }

    public void setClusterMethod(String clusterMethod) {
        this.clusterMethod = clusterMethod;
    }

    public void setTssClusteringDistance(int tssClusteringDistance) {
        this.tssClusteringDistance = tssClusteringDistance;
    }

    public void setAllowedCrossSubjectShift(int allowedCrossSubjectShift) {
        this.allowedCrossSubjectShift = allowedCrossSubjectShift;
    }

    public void setAllowedCrossReplicateShift(int allowedCrossReplicateShift) {
        this.allowedCrossReplicateShift = allowedCrossReplicateShift;
    }

    public void setMatchingReplicates(int matchingReplicates) {
        this.matchingReplicates = matchingReplicates;
    }

    public void setUtrLength(int utrLength) {
        this.utrLength = utrLength;
    }

    public void setAntisenseUtrLength(int antisenseUtrLength) {
        this.antisenseUtrLength = antisenseUtrLength;
    }


}
