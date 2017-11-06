package model;

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
    //If we choose to compare conditions, there's only one fasta and one gff file.
    private String conditionFasta, conditionGFF;

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
    private int allowedCrossDatasetShift;
    private int allowedCrossReplicateShift;
    private int matchingReplicates;
    private int utrLength;
    private int antisenseUtrLength;

    public ConfigFile() {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        buildLine(builder, "projectName", projectName);
        buildLine(builder, "numberOfDatasets", Integer.toString(numberOfDatasets));
        buildLine(builder, "numReplicates", Integer.toString(numberOfReplicates));
        if (!isModeConditions)
            buildLine(builder, "xmfa", alignmentFile);
        buildLine(builder, "writeGraphs", writeGraphs ? "1" : "0");

        if (isModeConditions) {
            buildLine(builder, "fasta", conditionFasta);
            buildLine(builder, "annotation", conditionGFF);
            for (Genome genome : genomeList) {
                buildLine(builder, "outputPrefix_" + genome.getAlignmentID(), genome.getName());
                for (Replicate replicate : genome.getReplicateList()) {
                    buildLine(builder, "fivePrimePlus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getEnrichedCodingStrand());
                    buildLine(builder, "fivePrimeMinus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getEnrichedTemplateStrand());
                    buildLine(builder, "normalPlus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getNormalCodingStrand());
                    buildLine(builder, "normalMinus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getNormalTemplateStrand());
                }
            }

        } else {
            for (Genome genome : genomeList) {
                buildLine(builder, "genome_" + genome.getAlignmentID(), genome.getFasta());
                buildLine(builder, "annotation_" + genome.getAlignmentID(), genome.getGff());
                buildLine(builder, "outputPrefix_" + genome.getAlignmentID(), genome.getName());
                for (Replicate replicate : genome.getReplicateList()) {
                    buildLine(builder, "fivePrimePlus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getEnrichedCodingStrand());
                    buildLine(builder, "fivePrimeMinus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getEnrichedTemplateStrand());
                    buildLine(builder, "normalPlus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getNormalCodingStrand());
                    buildLine(builder, "normalMinus_" + genome.getAlignmentID() + replicate.getReplicateID(), replicate.getNormalTemplateStrand());
                }
            }

        }
        StringBuilder idList = new StringBuilder();
        for (Genome genome : genomeList) {
            idList.append(genome.getAlignmentID()).append(",");
        }
        buildLine(builder, "idList", idList.toString().substring(0, idList.length() - 1));
        buildLine(builder, "allowedCompareShift", Integer.toString(allowedCrossDatasetShift));
        buildLine(builder, "allowedRepCompareShift", Integer.toString(allowedCrossReplicateShift));
        buildLine(builder, "maxUTRlength", Integer.toString(utrLength));
        buildLine(builder, "maxASutrLength", Integer.toString(antisenseUtrLength));
        buildLine(builder, "maxNormalTo5primeFactor", Double.toString(processingSiteFactor));
        buildLine(builder, "maxTSSinClusterDistance", Integer.toString(tssClusteringDistance));
        buildLine(builder, "min5primeToNormalFactor", Double.toString(enrichmentFactor));
        buildLine(builder, "minCliffFactor", Double.toString(stepFactor));
        buildLine(builder, "minCliffFactorDiscount", Double.toString(stepFactorReduction));
        buildLine(builder, "minCliffHeight", Double.toString(stepHeight));
        buildLine(builder, "minCliffHeightDiscount", Double.toString(stepHeightReduction));
        buildLine(builder, "minNormalHeight", Double.toString(baseHeight));
        buildLine(builder, "minNumRepMatches", Integer.toString(matchingReplicates));
        buildLine(builder, "minPlateauLength", Integer.toString(stepLength));
        buildLine(builder, "mode", isModeConditions ? "cond" : "align");
        buildLine(builder, "normPercentile", Double.toString(normalizationPercentile));
        buildLine(builder, "textNormPercentile", Double.toString(enrichmentNormalizationPercentile));
        buildLine(builder, "TSSinClusterSelectionMethod", clusterMethod);


        //TODO: These values appear in the config file, but aren't customisable yet
        buildLine(builder, "maxGapLengthInGene", "42");
        buildLine(builder, "superGraphCompatibility", "igb");
        buildLine(builder, "writeNocornacFiles", "0");

        return builder.toString();
    }

    private void buildLine(StringBuilder builder, String key, String value) {
        if (value == null)
            System.err.println("Couldn't create config file! Parameter \'" + key + "\' isn't set.");
        else
            builder.append(key).append(" = ").append(value).append("\n");
    }

    public String getProjectName() {
        return projectName;
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

    public boolean isModeConditions() {
        return isModeConditions;
    }

    public void setModeConditions(boolean modeConditions) {
        isModeConditions = modeConditions;
    }

    public String getAlignmentFile() {
        return alignmentFile;
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

    public String getConditionFasta() {
        return conditionFasta;
    }

    public void setConditionFasta(String conditionFasta) {
        this.conditionFasta = conditionFasta;
    }

    public String getConditionGFF() {
        return conditionGFF;
    }

    public void setConditionGFF(String conditionGFF) {
        this.conditionGFF = conditionGFF;
    }

    public boolean isWriteGraphs() {
        return writeGraphs;
    }

    public void setWriteGraphs(boolean writeGraphs) {
        this.writeGraphs = writeGraphs;
    }

    public double getStepHeight() {
        return stepHeight;
    }

    public void setStepHeight(double stepHeight) {
        this.stepHeight = stepHeight;
    }

    public double getStepHeightReduction() {
        return stepHeightReduction;
    }

    public void setStepHeightReduction(double stepHeightReduction) {
        this.stepHeightReduction = stepHeightReduction;
    }

    public double getStepFactor() {
        return stepFactor;
    }

    public void setStepFactor(double stepFactor) {
        this.stepFactor = stepFactor;
    }

    public double getStepFactorReduction() {
        return stepFactorReduction;
    }

    public void setStepFactorReduction(double stepFactorReduction) {
        this.stepFactorReduction = stepFactorReduction;
    }

    public double getEnrichmentFactor() {
        return enrichmentFactor;
    }

    public void setEnrichmentFactor(double enrichmentFactor) {
        this.enrichmentFactor = enrichmentFactor;
    }

    public double getProcessingSiteFactor() {
        return processingSiteFactor;
    }

    public void setProcessingSiteFactor(double processingSiteFactor) {
        this.processingSiteFactor = processingSiteFactor;
    }

    public int getStepLength() {
        return stepLength;
    }

    public void setStepLength(int stepLength) {
        this.stepLength = stepLength;
    }

    public double getBaseHeight() {
        return baseHeight;
    }

    public void setBaseHeight(double baseHeight) {
        this.baseHeight = baseHeight;
    }

    public double getNormalizationPercentile() {
        return normalizationPercentile;
    }

    public void setNormalizationPercentile(double normalizationPercentile) {
        this.normalizationPercentile = normalizationPercentile;
    }

    public double getEnrichmentNormalizationPercentile() {
        return enrichmentNormalizationPercentile;
    }

    public void setEnrichmentNormalizationPercentile(double enrichmentNormalizationPercentile) {
        this.enrichmentNormalizationPercentile = enrichmentNormalizationPercentile;
    }

    public String getClusterMethod() {
        return clusterMethod;
    }

    public void setClusterMethod(String clusterMethod) {
        this.clusterMethod = clusterMethod;
    }

    public int getTssClusteringDistance() {
        return tssClusteringDistance;
    }

    public void setTssClusteringDistance(int tssClusteringDistance) {
        this.tssClusteringDistance = tssClusteringDistance;
    }

    public int getAllowedCrossDatasetShift() {
        return allowedCrossDatasetShift;
    }

    public void setAllowedCrossDatasetShift(int allowedCrossSubjectShift) {
        this.allowedCrossDatasetShift = allowedCrossSubjectShift;
    }

    public int getAllowedCrossReplicateShift() {
        return allowedCrossReplicateShift;
    }

    public void setAllowedCrossReplicateShift(int allowedCrossReplicateShift) {
        this.allowedCrossReplicateShift = allowedCrossReplicateShift;
    }

    public int getMatchingReplicates() {
        return matchingReplicates;
    }

    public void setMatchingReplicates(int matchingReplicates) {
        this.matchingReplicates = matchingReplicates;
    }

    public int getUtrLength() {
        return utrLength;
    }

    public void setUtrLength(int utrLength) {
        this.utrLength = utrLength;
    }

    public int getAntisenseUtrLength() {
        return antisenseUtrLength;
    }

    public void setAntisenseUtrLength(int antisenseUtrLength) {
        this.antisenseUtrLength = antisenseUtrLength;
    }


}
