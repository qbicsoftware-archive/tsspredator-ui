package model;

/**TODO: This should represent a situation where most values of the config file are set to default
 * @author jmueller
 */
public class BasicConfigFileBuilder implements ConfigFileBuilder{
    @Override
    public void buildProjectName(String name) {

    }

    @Override
    public void buildNumberOfDatasets(int number) {

    }

    @Override
    public void buildNumberOfReplicates(int number) {

    }

    @Override
    public void buildMode(Boolean isConditions) {

    }

    @Override
    public void buildAlignmentFile(String alignmentFile) {

    }


    @Override
    public void addDatasets(int datasetsToAdd) {

    }

    @Override
    public void removeDatasets(int datasetsToRemove) {

    }

    @Override
    public void buildDatasetName(int index, String name) {

    }

    @Override
    public void buildGenomeFasta(int index, String fasta) {

    }

    @Override
    public void buildGenomeAlignmentID(int index, String id) {

    }

    @Override
    public void buildGenomeAnnotation(int index, String annotation) {

    }

    @Override
    public void addReplicates(int replicatesToAdd) {

    }

    @Override
    public void removeReplicates(int replicatesToRemove) {

    }

    @Override
    public void buildReplicateID(int genomeIndex, int replicateIndex, String id) {

    }

    @Override
    public void buildEnrichedPlus(int genomeIndex, int replicateIndex, String strand) {

    }

    @Override
    public void buildEnrichedMinus(int genomeIndex, int replicateIndex, String strand) {

    }

    @Override
    public void buildNormalPlus(int genomeIndex, int replicateIndex, String strand) {

    }

    @Override
    public void buildNormalMinus(int genomeIndex, int replicateIndex, String strand) {

    }

    @Override
    public void buildWriteGraphs(boolean writeGraphs) {

    }

    @Override
    public void buildStepHeight(double stepHeight) {

    }

    @Override
    public void buildStepHeightReduction(double stepHeightReduction) {

    }

    @Override
    public void buildStepFactor(double stepFactor) {

    }

    @Override
    public void buildStepFactorReduction(double stepFactorReduction) {

    }

    @Override
    public void buildEnrichmentFactor(double enrichmentFactor) {

    }

    @Override
    public void buildProcessingSiteFactor(double factor) {

    }

    @Override
    public void buildStepLength(int stepLength) {

    }

    @Override
    public void buildBaseHeight(double baseHeight) {

    }

    @Override
    public void buildNormalizationPercentile(double normPerc) {

    }

    @Override
    public void buildEnrichmentNormalizationPercentile(double enrNormPerc) {

    }

    @Override
    public void buildClusterMethod(String method) {

    }

    @Override
    public void buildClusteringDistance(int distance) {

    }

    @Override
    public void buildAllowedCrossDatasetShift(int shift) {

    }

    @Override
    public void buildAllowedCrossReplicateShift(int shift) {

    }

    @Override
    public void buildMatchingReplicates(int matchinReplicates) {

    }

    @Override
    public void buildUtrLength(int utrLength) {

    }

    @Override
    public void buildAntisenseUtrLength(int antisenseUtrLength) {

    }

    @Override
    public ConfigFile createConfigFile() {
        return null;
    }
}
