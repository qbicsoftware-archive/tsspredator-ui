package model;

public interface ConfigFileBuilder {

    void buildProjectName(String name);

    void buildNumberOfDatasets(int number);

    void buildNumberOfReplicates(int number);

    void buildMode(Boolean isConditions);

    void buildAlignmentFile(String alignmentFile);

    //TODO: Should the four add/remove methods be visible in the interface?
    void addDatasets(int datasetsToAdd);

    void removeDatasets(int datasetsToRemove);

    void buildDatasetName(int index, String name);

    void buildGenomeFasta(int index, String fasta);

    void buildGenomeAlignmentID(int index, String id);

    void buildGenomeAnnotation(int index, String annotation);

    void addReplicates(int replicatesToAdd);

    void removeReplicates(int replicatesToRemove);

    void buildReplicateID(int genomeIndex, int replicateIndex, String id);

    void buildEnrichedPlus(int genomeIndex, int replicateIndex, String strand);

    void buildEnrichedMinus(int genomeIndex, int replicateIndex, String strand);

    void buildNormalPlus(int genomeIndex, int replicateIndex, String strand);

    void buildNormalMinus(int genomeIndex, int replicateIndex, String strand);

    void buildWriteGraphs(boolean writeGraphs);

    void buildStepHeight(double stepHeight);

    void buildStepHeightReduction(double stepHeightReduction);

    void buildStepFactor(double stepFactor);

    void buildStepFactorReduction(double stepFactorReduction);

    void buildEnrichmentFactor(double enrichmentFactor);

    void buildProcessingSiteFactor(double factor);

    void buildStepLength(int stepLength);

    void buildBaseHeight(double baseHeight);

    void buildNormalizationPercentile(double normPerc);

    void buildEnrichmentNormalizationPercentile(double enrNormPerc);

    void buildClusterMethod(String method);

    void buildClusteringDistance(int distance);

    void buildAllowedCrossDatasetShift(int shift);

    void buildAllowedCrossReplicateShift(int shift);

    void buildMatchingReplicates(int matchingReplicates);

    void buildUtrLength(int utrLength);

    void buildAntisenseUtrLength(int antisenseUtrLength);

    ConfigFile createConfigFile();

}
