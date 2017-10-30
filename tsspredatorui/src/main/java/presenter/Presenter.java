package presenter;

import model.BasicConfigFileBuilder;
import model.ConfigFile;
import model.ConfigFileBuilder;
import model.ExtensiveConfigFileBuilder;
import view.AccordionLayoutMain;

import java.io.File;

/**
 * @author jmueller
 */
public class Presenter {
    private ConfigFileBuilder configFileBuilder;
    private boolean isConfigExtensive;
    private AccordionLayoutMain view;

    public Presenter() {
        isConfigExtensive = true; //Extensive config by default
        createBuilder(); //TODO: This should be called somewhere else
    }

    public void createBuilder() {
        configFileBuilder = isConfigExtensive ? new ExtensiveConfigFileBuilder() : new BasicConfigFileBuilder();
    }

    public void setConfigExtensive(boolean configExtensive) {
        isConfigExtensive = configExtensive;
    }

    public void updateProjectName(String name) {
        configFileBuilder.buildProjectName(name);
    }

    public void updateNumberOfDatasets(int number) {
        configFileBuilder.buildNumberOfDatasets(number);
    }

    public void updateNumberOfReplicates(int number) {
        configFileBuilder.buildNumberOfReplicates(number);
    }

    public void updateMode(Boolean isConditions) {
        configFileBuilder.buildMode(isConditions);
        view.updateDataPanelMode(isConditions);
    }

    public void updateAlignmentFile(String alignmentFile) {
        configFileBuilder.buildAlignmentFile(alignmentFile);
    }

    public void updateGenomeName(int index, String name) {
        configFileBuilder.buildDatasetName(index, name);
    }

    public void updateGenomeFasta(int index, String name) {
        configFileBuilder.buildGenomeFasta(index, name);
    }

    public void updateGenomeAlignmentID(int index, String id) {
        configFileBuilder.buildGenomeAlignmentID(index, id);
    }

    public void updateGenomeAnnotation(int index, String annotation) {
        configFileBuilder.buildGenomeAnnotation(index, annotation);
    }

    public void updateReplicateID(int datasetIndex, int replicateIndex, String id) {
        configFileBuilder.buildReplicateID(datasetIndex, replicateIndex, id);
    }

    public void updateEnrichedPlus(int datasetIndex, int replicateIndex, String strand) {
        configFileBuilder.buildEnrichedPlus(datasetIndex, replicateIndex, strand);
    }

    public void updateEnrichedMinus(int datasetIndex, int replicateIndex, String strand) {
        configFileBuilder.buildEnrichedMinus(datasetIndex, replicateIndex, strand);
    }

    public void updateNormalPlus(int datasetIndex, int replicateIndex, String strand) {
        configFileBuilder.buildNormalPlus(datasetIndex, replicateIndex, strand);
    }

    public void updateNormalMinus(int datasetIndex, int replicateIndex, String strand) {
        configFileBuilder.buildNormalMinus(datasetIndex, replicateIndex, strand);
    }

    public void updateWriteGraphs(boolean writeGraphs) {
        configFileBuilder.buildWriteGraphs(writeGraphs);
    }

    public void updateStepHeight(double stepHeight) {
        configFileBuilder.buildStepHeight(stepHeight);
    }

    public void updateStepHeightReduction(double shr) {
        configFileBuilder.buildStepHeightReduction(shr);
    }

    public void updateStepFactor(double sf) {
        configFileBuilder.buildStepFactor(sf);
    }

    public void updateStepFactorReduction(double sfr) {
        configFileBuilder.buildStepFactor(sfr);
    }

    public void updateEnrichmentFactor(double ef) {
        configFileBuilder.buildEnrichmentFactor(ef);
    }

    public void updateProcessingSiteFactor(double psf) {
        configFileBuilder.buildProcessingSiteFactor(psf);
    }

    public void updateStepLength(int sl) {
        configFileBuilder.buildStepLength(sl);
    }

    public void updateBaseHeight(double bh) {
        configFileBuilder.buildBaseHeight(bh);
    }

    public void updateNormalizationPercentile(double normPerc) {
        configFileBuilder.buildNormalizationPercentile(normPerc);
    }

    public void updateEnrichmentNormalizationPercentile(double enrNormPerc) {
        configFileBuilder.buildEnrichmentNormalizationPercentile(enrNormPerc);
    }

    public void updateClusterMethod(String method) {
        configFileBuilder.buildClusterMethod(method);
    }

    public void updateClusteringDistance(int distance) {
        configFileBuilder.buildClusteringDistance(distance);
    }

    public void updateAllowedCrossDatasetShift(int shift) {
        configFileBuilder.buildAllowedCrossDatasetShift(shift);
    }

    public void updateAllowedCrossReplicateShift(int shift) {
        configFileBuilder.buildAllowedCrossDatasetShift(shift);
    }

    public void updateMatchingReplicates(int mr) {
        configFileBuilder.buildMatchingReplicates(mr);
    }

    public void updateUtrLength(int utrLength) {
        configFileBuilder.buildUtrLength(utrLength);
    }

    public void updateAntisenseUtrLength(int aul) {
        configFileBuilder.buildAntisenseUtrLength(aul);
    }

    public File produceConfigFile() {
        ConfigFile configFile = configFileBuilder.createConfigFile();
        File file = new File("/tmp/tssconfiguration.conf");
        return configFile.writeConfigFile(file);

    }

    public void setView(AccordionLayoutMain view) {
        this.view = view;
    }

    public ConfigFileBuilder getConfigFileBuilder() {
        return configFileBuilder;
    }

    public void setConfigFileBuilder(ConfigFileBuilder configFileBuilder) {
        this.configFileBuilder = configFileBuilder;
    }


    //These three methods are only for temporary use. They are needed because as of now,
    //the config file stores a genome name, fasta and gff for every dataset, regardless of the workflow variant
    //However, in the condition variant there's only one of these
    // --> this needs to be changed in the config logic itself
    public void updateAllGenomeFastas(String name) {
        for (int i = 0; i < configFileBuilder.createConfigFile().getNumberOfDatasets(); i++) {
            configFileBuilder.buildGenomeFasta(i, name);
        }

    }

    public void updateAllGenomeAnnotations(String annotation) {
        for (int i = 0; i < configFileBuilder.createConfigFile().getNumberOfDatasets(); i++) {
            configFileBuilder.buildGenomeAnnotation(i, annotation);
        }

    }


}
