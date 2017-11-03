package presenter;

import com.vaadin.data.BeanValidationBinder;
import model.*;
import view.AccordionLayoutMain;

import javax.management.Notification;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author jmueller
 */
public class Presenter {
    private ConfigFile configFile;
    private boolean isParamsCustom;
    private AccordionLayoutMain view;
    private BeanValidationBinder<ConfigFile> beanBinder;

    public enum Preset {
        VERY_SPECIFIC, MORE_SPECIFIC, DEFAULT, MORE_SENSITIVE, VERY_SENSITIVE

    }

    private Preset preset = Preset.DEFAULT;

    public Presenter() {
        isParamsCustom = false; //Preset parameters by default
        configFile = new ConfigFile();
        configFile.setGenomeList(new ArrayList<>());
        beanBinder = new BeanValidationBinder<>(ConfigFile.class);
        beanBinder.setBean(configFile);
        initBindings();
    }

    /**
     * Initializes the fields of the view
     * TODO: Move stuff from the view here so the view contains less logic
     */
    public void initFields() {
        view.getParametersPanel().getPresetOrCustom().setSelectedItem("Preset");
        view.getParametersPanel().getPresetSelection().setSelectedItem("Default");

    }

    public void initBindings(){
        beanBinder.bind(view.getGeneralConfigPanel().getProjectName(), "projectName");
        //TODO: beanBinder.bind(numberOfDatasets);
        //TODO: beanBinder.bind(numberOfReplicates);
        //TODO: beanBinder.bind(mode); confgFile is easy, but how do I update the view?
        beanBinder.bind(view.getGeneralConfigPanel().getAlignmentFileUpload(), "alignmentFile");
        //TODO: DatasetName, Fasta, Annotation: Ambiguous!
        //TODO: Alignment id, annotation, replicateid, wiggle files
        beanBinder.bind(view.getParametersPanel().getWriteGraphs(), "writeGraphs");
        beanBinder.bind(view.getParametersPanel().getStepHeight(), "stepHeight");
        beanBinder.bind(view.getParametersPanel().getStepHeightReduction(), "stepHeightReduction");
        beanBinder.bind(view.getParametersPanel().getStepFactor(), "stepFactor");
        beanBinder.bind(view.getParametersPanel().getStepFactorReduction(), "stepFactorReduction");
        beanBinder.bind(view.getParametersPanel().getEnrichmentFactor(), "enrichmentFactor");
        beanBinder.bind(view.getParametersPanel().getProcessingSiteFactor(), "processingSiteFactor");
        beanBinder.bind(view.getParametersPanel().getStepLength(), "stepLength");
        beanBinder.bind(view.getParametersPanel().getBaseHeight(), "baseHeight");
        beanBinder.bind(view.getParametersPanel().getNormalizationPercentile(), "normalizationPercentile");
        beanBinder.bind(view.getParametersPanel().getEnrichedNormalizationPercentile(), "enrichedNormalizationPercentile");
        beanBinder.bind(view.getParametersPanel().getClusterMethod(), "clusterMethod");
        beanBinder.bind(view.getParametersPanel().getClusteringDistance(), "clusteringDistance");
        beanBinder.bind(view.getParametersPanel().getCrossDatasetShift(), "allowedCrossDatasetShift");
        beanBinder.bind(view.getParametersPanel().getCrossReplicateShift(), "allowedCrossReplicateShift");
        beanBinder.bind(view.getParametersPanel().getMatchingReplicates(), "matchingReplicates");
        beanBinder.bind(view.getParametersPanel().getUtrLength(), "utrLength");
        beanBinder.bind(view.getParametersPanel().getAntisenseUtrLength(), "antisenseUtrLength");

    }

    public void setParamsCustom(boolean paramsCustom) {
        isParamsCustom = paramsCustom;
    }

    public void updateProjectName(String projectName) {
        configFile.setProjectName(projectName);
    }

    public void updateNumberOfDatasets(int number) {
        int delta = number - configFile.getNumberOfDatasets();
        configFile.setNumberOfDatasets(number);
        //Add or remove datasets so they match the given number
        if (delta > 0)
            addDatasets(delta);
        else
            removeDatasets(-delta);
    }

    public void addDatasets(int datasetsToAdd) {
        for (int i = 0; i < datasetsToAdd; i++) {
            Genome currentGenome = new Genome();
            //Add as many replicates as the other Genomes have
            for (int j = 0; j < configFile.getNumberOfReplicates(); j++) {
                currentGenome.getReplicateList().add(new Replicate());
            }
            configFile.getGenomeList().add(currentGenome);
        }
    }


    public void removeDatasets(int datasetsToRemove) {
        int oldGenomeListSize = configFile.getGenomeList().size();
        int startIndex = oldGenomeListSize - datasetsToRemove;
        for (int i = startIndex; i < oldGenomeListSize; i++)
            //Remove tail until desired size is reached
            configFile.getGenomeList().remove(configFile.getGenomeList().size() - 1);
    }

    public void updateNumberOfReplicates(int number) {
        int delta = number - configFile.getNumberOfReplicates();
        configFile.setNumberOfReplicates(number);
        //Add or remove replicates so they match the given number
        if (delta > 0)
            addReplicates(delta);
        else
            removeReplicates(-delta);
    }


    public void addReplicates(int replicatesToAdd) {
        for (int i = 0; i < replicatesToAdd; i++) {
            for (Genome genome : configFile.getGenomeList()) {
                genome.getReplicateList().add(new Replicate());
            }
        }
    }


    public void removeReplicates(int replicatesToRemove) {
        int oldReplicateListSize = configFile.getGenomeList().get(0).getReplicateList().size();
        int startIndex = oldReplicateListSize - replicatesToRemove;
        for (Genome genome : configFile.getGenomeList()) {
            for (int i = startIndex; i < oldReplicateListSize; i++) {
                //Remove tail until desired size is reached
                genome.getReplicateList().remove(genome.getReplicateList().size() - 1);
            }
        }
    }

    public void updateMode(Boolean isConditions) {
        configFile.setModeConditions(isConditions);
        view.updateDataPanelMode(isConditions);
    }

    public void updateAlignmentFile(String alignmentFile) {
        configFile.setAlignmentFile(alignmentFile);
    }

    public void updateDatasetName(int index, String name) {
        configFile.getGenomeList().get(index).setName(name);
    }

    public void updateGenomeFasta(int index, String fasta) {
        configFile.getGenomeList().get(index).setFasta(fasta);
    }

    public void updateGenomeAlignmentID(int index, String id) {
        configFile.getGenomeList().get(index).setAlignmentID(id);
    }

    public void updateGenomeAnnotation(int index, String annotation) {
        configFile.getGenomeList().get(index).setGff(annotation);
    }

    public void updateReplicateID(int datasetIndex, int replicateIndex, String id) {
        configFile
                .getGenomeList()
                .get(datasetIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setReplicateID(id);

    }

    public void updateEnrichedPlus(int datasetIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(datasetIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setEnrichedCodingStrand(strand);

    }

    public void updateEnrichedMinus(int datasetIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(datasetIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setEnrichedTemplateStrand(strand);

    }

    public void updateNormalPlus(int datasetIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(datasetIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setNormalCodingStrand(strand);

    }

    public void updateNormalMinus(int datasetIndex, int replicateIndex, String strand) {
        configFile
                .getGenomeList()
                .get(datasetIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setNormalTemplateStrand(strand);

    }

    public void updateWriteGraphs(boolean writeGraphs) {
        configFile.setWriteGraphs(writeGraphs);
    }

    public void updateStepHeight(double stepHeight) {
        configFile.setStepHeight(stepHeight);
    }

    public void updateStepHeightReduction(double stepHeightReduction) {
        configFile.setStepHeightReduction(stepHeightReduction);
    }

    public void updateStepFactor(double stepFactor) {
        configFile.setStepFactor(stepFactor);
    }

    public void updateStepFactorReduction(double stepFactorReduction) {
        configFile.setStepFactorReduction(stepFactorReduction);
    }

    public void updateEnrichmentFactor(double enrichmentFactor) {
        configFile.setEnrichmentFactor(enrichmentFactor);
    }

    public void updateProcessingSiteFactor(double processingSiteFactor) {
        configFile.setProcessingSiteFactor(processingSiteFactor);
    }

    public void updateStepLength(int stepLength) {
        configFile.setStepLength(stepLength);
    }

    public void updateBaseHeight(double baseHeight) {
        configFile.setBaseHeight(baseHeight);
    }

    public void updateNormalizationPercentile(double normalizationPercentile) {
        configFile.setNormalizationPercentile(normalizationPercentile);
    }

    public void updateEnrichmentNormalizationPercentile(double enrichmentNormalizationPercentile) {
        configFile.setEnrichmentNormalizationPercentile(enrichmentNormalizationPercentile);
    }

    public void updateClusterMethod(String clusterMethod) {
        configFile.setClusterMethod(clusterMethod);
    }

    public void updateClusteringDistance(int clusteringDistance) {
        configFile.setTssClusteringDistance(clusteringDistance);
    }

    public void updateAllowedCrossDatasetShift(int allowedCrossDatasetShift) {
        configFile.setAllowedCrossDatasetShift(allowedCrossDatasetShift);
    }

    public void updateAllowedCrossReplicateShift(int allowedCrossReplicateShift) {
        configFile.setAllowedCrossReplicateShift(allowedCrossReplicateShift);
    }

    public void updateMatchingReplicates(int matchingReplicates) {
        configFile.setMatchingReplicates(matchingReplicates);
    }

    public void updateUtrLength(int utrLength) {
        configFile.setUtrLength(utrLength);
    }

    public void updateAntisenseUtrLength(int antisenseUtrLength) {
        configFile.setAntisenseUtrLength(antisenseUtrLength);
    }

    public File produceConfigFile() {
        File file = new File("/tmp/tssconfiguration.conf");
        String configText = configFile.toString();
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(configText);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

    public void applyPresetParameters() {
        switch (preset) {

            case VERY_SPECIFIC:
                view.getParametersPanel().getStepHeight().setValue(1.);
                view.getParametersPanel().getStepHeightReduction().setValue(.5);
                view.getParametersPanel().getStepFactor().setValue(2.);
                view.getParametersPanel().getStepFactorReduction().setValue(.5);
                view.getParametersPanel().getEnrichmentFactor().setValue(3.);
                view.getParametersPanel().getProcessingSiteFactor().setValue(1.);
                view.getParametersPanel().getStepLength().setValue(0.);
                view.getParametersPanel().getBaseHeight().setValue(0.);
                break;
            case MORE_SPECIFIC:
                view.getParametersPanel().getStepHeight().setValue(0.5);
                view.getParametersPanel().getStepHeightReduction().setValue(0.2);
                view.getParametersPanel().getStepFactor().setValue(2.);
                view.getParametersPanel().getStepFactorReduction().setValue(0.5);
                view.getParametersPanel().getEnrichmentFactor().setValue(2.);
                view.getParametersPanel().getProcessingSiteFactor().setValue(1.2);
                view.getParametersPanel().getStepLength().setValue(0.);
                view.getParametersPanel().getBaseHeight().setValue(0.);
                break;
            case DEFAULT:
                view.getParametersPanel().getStepHeight().setValue(.3);
                view.getParametersPanel().getStepHeightReduction().setValue(.2);
                view.getParametersPanel().getStepFactor().setValue(2.);
                view.getParametersPanel().getStepFactorReduction().setValue(.5);
                view.getParametersPanel().getEnrichmentFactor().setValue(2.);
                view.getParametersPanel().getProcessingSiteFactor().setValue(1.5);
                view.getParametersPanel().getStepLength().setValue(0.);
                view.getParametersPanel().getBaseHeight().setValue(0.);
                break;
            case MORE_SENSITIVE:
                view.getParametersPanel().getStepHeight().setValue(0.2);
                view.getParametersPanel().getStepHeightReduction().setValue(0.15);
                view.getParametersPanel().getStepFactor().setValue(1.5);
                view.getParametersPanel().getStepFactorReduction().setValue(0.5);
                view.getParametersPanel().getEnrichmentFactor().setValue(1.5);
                view.getParametersPanel().getProcessingSiteFactor().setValue(2.);
                view.getParametersPanel().getStepLength().setValue(0.);
                view.getParametersPanel().getBaseHeight().setValue(0.);
                break;
            case VERY_SENSITIVE:
                view.getParametersPanel().getStepHeight().setValue(0.1);
                view.getParametersPanel().getStepHeightReduction().setValue(0.09);
                view.getParametersPanel().getStepFactor().setValue(1.);
                view.getParametersPanel().getStepFactorReduction().setValue(0.);
                view.getParametersPanel().getEnrichmentFactor().setValue(1.);
                view.getParametersPanel().getProcessingSiteFactor().setValue(3.);
                view.getParametersPanel().getStepLength().setValue(0.);
                view.getParametersPanel().getBaseHeight().setValue(0.);
                break;
        }
    }

    public void setView(AccordionLayoutMain view) {
        this.view = view;
    }


    public Preset getPreset() {
        return preset;
    }

    public void setPreset(Preset preset) {
        this.preset = preset;
    }

    //These three methods are only for temporary use. They are needed because as of now,
    //the config file stores a genome name, fasta and gff for every dataset, regardless of the workflow variant
    //However, in the condition variant there's only one of these
    // --> this needs to be changed in the config logic itself
    public void updateAllGenomeFastas(String name) {
        for (int i = 0; i < configFile.getNumberOfDatasets(); i++) {
            updateGenomeFasta(i, name);
        }

    }

    public void updateAllGenomeAnnotations(String annotation) {
        for (int i = 0; i < configFile.getNumberOfDatasets(); i++) {
            updateGenomeAnnotation(i, annotation);
        }

    }


}
