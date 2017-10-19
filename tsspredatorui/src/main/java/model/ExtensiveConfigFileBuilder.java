package model;

import java.util.ArrayList;

public abstract class ExtensiveConfigFileBuilder implements ConfigFileBuilder {
    ConfigFile configFile;

    public ExtensiveConfigFileBuilder() {
        configFile = new ConfigFile();
        configFile.setGenomeList(new ArrayList<>());
    }


    @Override
    public void buildProjectName(String projectName) {
        configFile.setProjectName(projectName);
    }

    @Override
    public void buildNumberOfDatasets(int number) {
        int delta = number - configFile.getNumberOfDatasets();
        configFile.setNumberOfDatasets(number);
        //Add or remove datasets so they match the given number
        if(delta > 0)
            addDatasets(delta);
        else
            removeDatasets(-delta);


    }

    @Override
    public void buildNumberOfReplicates(int number) {
        int delta = number - configFile.getNumberOfReplicates();
        configFile.setNumberOfReplicates(number);
        //Add or remove replicates so they match the given number
        if(delta > 0)
            addReplicates(delta);
        else
            removeReplicates(-delta);
    }

    @Override
    public abstract void buildMode();

    @Override
    public void addDatasets(int datasetsToAdd) {
        for (int i = 0; i < datasetsToAdd; i++) {
            configFile.getGenomeList().add(new Genome());
        }

    }

    @Override
    public void removeDatasets(int datasetsToRemove) {
        int startIndex = configFile.getGenomeList().size() - datasetsToRemove;
        for (int i = startIndex; i < configFile.getGenomeList().size(); i++)
            configFile.getGenomeList().remove(i);
    }

    @Override
    public void buildGenomeName(int index, String name) {
        configFile.getGenomeList().get(index).setName(name);
    }

    @Override
    public void buildGenomeFasta(int index, String fasta) {
        configFile.getGenomeList().get(index).setFasta(fasta);
    }

    @Override
    public void buildGenomeAlignmentID(int index, String alignmentID) {
        configFile.getGenomeList().get(index).setAlignmentID(alignmentID);
    }

    @Override
    public void buildGenomeAnnotation(int index, String annotation) {
        configFile.getGenomeList().get(index).setGff(annotation);
    }

    @Override
    public void addReplicates(int replicatesToAdd) {
        for (int i = 0; i < replicatesToAdd; i++) {
            for (Genome genome : configFile.getGenomeList()) {
                genome.getReplicateList().add(new Replicate());
            }
        }
    }

    @Override
    public void removeReplicates(int replicatesToRemove) {
        int startIndex = configFile.getGenomeList().get(0).getReplicateList().size() - replicatesToRemove;
        for (Genome genome : configFile.getGenomeList()) {
            for (int i = startIndex; i < genome.getReplicateList().size(); i++) {
                genome.getReplicateList().remove(i);
            }
        }
    }

    @Override
    public void buildReplicateID(int genomeIndex, int replicateIndex, String id){
        configFile
                .getGenomeList()
                .get(genomeIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setReplicateID(id);
    }

    @Override
    public void buildEnrichedPlus(int genomeIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(genomeIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setEnrichedCodingStrand(strand);
    }

    @Override
    public void buildEnrichedMinus(int genomeIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(genomeIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setEnrichedTemplateStrand(strand);
    }

    @Override
    public void buildNormalPlus(int genomeIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(genomeIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setNormalCodingStrand(strand);
    }

    @Override
    public void buildNormalMinus(int genomeIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(genomeIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setNormalTemplateStrand(strand);
    }

    @Override
    public void buildWriteGraphs(boolean writeGraphs) {
        configFile.setWriteGraphs(writeGraphs);
    }

    @Override
    public void buildStepHeight(double stepHeight) {
        configFile.setStepHeight(stepHeight);
    }

    @Override
    public void buildStepHeightReduction(double stepHeightReduction) {
        configFile.setStepHeightReduction(stepHeightReduction);
    }

    @Override
    public void buildStepFactor(double stepFactor) {
        configFile.setStepFactor(stepFactor);
    }

    @Override
    public void buildStepFactorReduction(double stepFactorReduction) {
        configFile.setStepFactorReduction(stepFactorReduction);
    }

    @Override
    public void buildEnrichmentFactor(double enrichmentFactor) {
        configFile.setEnrichmentFactor(enrichmentFactor);
    }

    @Override
    public void buildProcessingSiteFactor(double processingSiteFactor) {
        configFile.setProcessingSiteFactor(processingSiteFactor);
    }

    @Override
    public void buildStepLength(int stepLength) {
        configFile.setStepLength(stepLength);
    }

    @Override
    public void buildBaseHeight(double baseHeight) {
        configFile.setBaseHeight(baseHeight);
    }

    @Override
    public void buildNormalizationPercentile(double normalizationPercentile) {
        configFile.setNormalizationPercentile(normalizationPercentile);
    }

    @Override
    public void buildEnrichmentNormalizationPercentile(double enrichmentNormalizationPercentile) {
        configFile.setEnrichmentNormalizationPercentile(enrichmentNormalizationPercentile);
    }

    @Override
    public void buildClusterMethod(String clusterMethod) {
        configFile.setClusterMethod(clusterMethod);
    }

    @Override
    public void buildTssClusteringDistance(int clusteringDistance) {
        configFile.setTssClusteringDistance(clusteringDistance);
    }

    @Override
    public void buildAllowedCrossSubjectShift(int allowedCrossSubjectShift) {
        configFile.setAllowedCrossSubjectShift(allowedCrossSubjectShift);
    }

    @Override
    public void buildAllowedCrossReplicateShift(int allowedCrossReplicateShift) {
        configFile.setAllowedCrossReplicateShift(allowedCrossReplicateShift);
    }

    @Override
    public void buildMatchingReplicates(int matchingReplicates) {
        configFile.setMatchingReplicates(matchingReplicates);
    }

    @Override
    public void buildUtrLength(int utrLength) {
        configFile.setUtrLength(utrLength);
    }

    @Override
    public void buildAntisenseUtrLength(int antisenseUtrLength) {
        configFile.setAntisenseUtrLength(antisenseUtrLength);
    }

    @Override
    public ConfigFile createConfigFile() {
        return configFile;
    }
}
