package presenter;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import model.*;
import view.AccordionLayoutMain;
import view.firstImplementation.DataPanel;
import view.firstImplementation.GenomeDataPanel;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * @author jmueller
 */
public class Presenter {
    private ConfigFile configFile;
    private boolean isParamsCustom;
    private AccordionLayoutMain view;
    private Binder<ConfigFile> configFileBinder;

    public enum Preset {
        VERY_SPECIFIC, MORE_SPECIFIC, DEFAULT, MORE_SENSITIVE, VERY_SENSITIVE

    }

    private Preset preset = Preset.DEFAULT;

    public Presenter() {
        isParamsCustom = false; //Preset parameters by default
        configFile = new ConfigFile();
        configFile.setGenomeList(new ArrayList<>());
        setInitialConfigParameters();
        configFileBinder = new Binder<>();
        configFileBinder.setBean(configFile);
    }

    /**
     * Initializes the fields of the view
     * TODO: Move stuff from the view here so the view contains less logic
     */
    public void initFields() {
        view.getParametersPanel().getPresetOrCustom().setSelectedItem("Preset");
        view.getParametersPanel().getPresetSelection().setSelectedItem("Default");

    }

    public void initBindings() {
        configFileBinder.bind(view.getGeneralConfigPanel().getProjectName(),
                ConfigFile::getProjectName,
                ConfigFile::setProjectName);
        //TODO: Only works for genome because I can't bind to two fields - workaround needed!
        configFileBinder.bind(view.getGenomeDataPanel().getNumberOfDatasetsBox(),
                (ValueProvider<ConfigFile, Integer>) configFile1 -> configFile1.getNumberOfDatasets(),
                (Setter<ConfigFile, Integer>) (configFile, number) -> {
                    int delta = number - configFile.getNumberOfDatasets();
                    configFile.setNumberOfDatasets(number);
                    //Add or remove datasets so they match the given number
                    if (delta > 0)
                        addDatasets(delta);
                    else
                        removeDatasets(-delta);

                });
//        //TODO: See above
        configFileBinder.bind(view.getGenomeDataPanel().getNumberOfReplicatesBox(),
                (ValueProvider<ConfigFile, Integer>)   configFile1 -> configFile1.getNumberOfReplicates(),
                (Setter<ConfigFile, Integer>) (configFile, number) -> {
                    int delta = number - configFile.getNumberOfReplicates();
                    configFile.setNumberOfReplicates(number);
                    //Add or remove replicates so they match the given number
                    if (delta > 0)
                        addReplicates(delta);
                    else
                        removeReplicates(-delta);
                });
        configFileBinder.bind(view.getGeneralConfigPanel().getProjectTypeButtonGroup(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        //TODO: Replace hard-coded strings by global variables (and also replace in GeneralConfigPanel!)
                        return configFile.isModeConditions() ? "Compare Conditions" : "Compare Strain/Species";
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String s) {
                        view.updateDataPanelMode(s.equals("Compare Conditions"));
                        configFile.setModeConditions(s.equals("Compare Conditions"));
                    }
                }
        );
        configFileBinder.bind(view.getGeneralConfigPanel().getAlignmentFileUpload(),
                ConfigFile::getAlignmentFile,
                ConfigFile::setAlignmentFile);
        configFileBinder.bind(view.getParametersPanel().getWriteGraphs(),
                ConfigFile::isWriteGraphs,
                ConfigFile::setWriteGraphs);
        configFileBinder.bind(view.getParametersPanel().getStepHeight(),
                ConfigFile::getStepHeight,
                ConfigFile::setStepHeight);
        configFileBinder.bind(view.getParametersPanel().getStepHeightReduction(),
                ConfigFile::getStepHeightReduction,
                ConfigFile::setStepHeightReduction);
        configFileBinder.bind(view.getParametersPanel().getStepFactor(),
                ConfigFile::getStepFactor,
                ConfigFile::setStepFactor);
        configFileBinder.bind(view.getParametersPanel().getStepFactorReduction(),
                ConfigFile::getStepFactorReduction,
                ConfigFile::setStepFactorReduction);
        configFileBinder.bind(view.getParametersPanel().getEnrichmentFactor(),
                ConfigFile::getEnrichmentFactor,
                ConfigFile::setEnrichmentFactor);
        configFileBinder.bind(view.getParametersPanel().getProcessingSiteFactor(),
                ConfigFile::getProcessingSiteFactor,
                ConfigFile::setProcessingSiteFactor);
        configFileBinder.forField(view.getParametersPanel().getStepLength())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getStepLength, ConfigFile::setStepLength);
        configFileBinder.bind(view.getParametersPanel().getBaseHeight(),
                ConfigFile::getBaseHeight,
                ConfigFile::setBaseHeight);
        configFileBinder.bind(view.getParametersPanel().getNormalizationPercentile(),
                ConfigFile::getNormalizationPercentile,
                ConfigFile::setNormalizationPercentile);
        configFileBinder.bind(view.getParametersPanel().getEnrichedNormalizationPercentile(),
                ConfigFile::getEnrichmentNormalizationPercentile,
                ConfigFile::setEnrichmentNormalizationPercentile);
        configFileBinder.bind(view.getParametersPanel().getClusterMethod(),
                ConfigFile::getClusterMethod,
                ConfigFile::setClusterMethod);
        configFileBinder.forField(view.getParametersPanel().getClusteringDistance())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getTssClusteringDistance, ConfigFile::setTssClusteringDistance);
        configFileBinder.forField(view.getParametersPanel().getCrossDatasetShift())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getAllowedCrossDatasetShift, ConfigFile::setAllowedCrossDatasetShift);
        configFileBinder.forField(view.getParametersPanel().getCrossReplicateShift())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getAllowedCrossReplicateShift, ConfigFile::setAllowedCrossReplicateShift);
        configFileBinder.bind(view.getParametersPanel().getMatchingReplicates(),
                ConfigFile::getMatchingReplicates,
                ConfigFile::setMatchingReplicates);
        configFileBinder.forField(view.getParametersPanel().getUtrLength())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getUtrLength, ConfigFile::setUtrLength);
        configFileBinder.forField(view.getParametersPanel().getAntisenseUtrLength())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getAntisenseUtrLength, ConfigFile::setAntisenseUtrLength);

    }

    public void setInitialConfigParameters(){
        configFile.setNormalizationPercentile(0.9);
        configFile.setEnrichmentNormalizationPercentile(0.5);
        configFile.setClusterMethod("HIGHEST");
        configFile.setTssClusteringDistance(3);
        configFile.setAllowedCrossDatasetShift(1);
        configFile.setAllowedCrossReplicateShift(1);
        configFile.setMatchingReplicates(1);
        configFile.setUtrLength(300);
        configFile.setAntisenseUtrLength(100);
    }

    //TODO: Yet another method that so far only work for GenomeDataPanel!
    public void initDatasetBindings(int index) {
        DataPanel.DatasetTab tab = view.getGenomeDataPanel().getDatasetTab(index);
        GenomeDataPanel.GenomeTab genomeTab = (GenomeDataPanel.GenomeTab) tab;
        configFileBinder.bind(genomeTab.getNameField(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(index).getName();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String name) {
                        configFile.getGenomeList().get(index).setName(name);
                    }
                });
        configFileBinder.bind(genomeTab.getFastaField(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(index).getFasta();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String fasta) {
                        configFile.getGenomeList().get(index).setFasta(fasta);
                    }
                });
        configFileBinder.bind(genomeTab.getIdField(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(index).getAlignmentID();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String id) {
                        configFile.getGenomeList().get(index).setAlignmentID(id);
                    }
                });
        configFileBinder.bind(genomeTab.getGffField(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(index).getGff();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String annotation) {
                        configFile.getGenomeList().get(index).setGff(annotation);
                    }
                });

    }



    //TODO: Yet another method that so far only work for GenomeDataPanel!
    public void initReplicateBindings(int datasetIndex, int replicateIndex) {
        DataPanel.ReplicateTab replicateTab = view.getGenomeDataPanel().getDatasetTab(datasetIndex).getReplicateTab(replicateIndex);
        configFileBinder.bind(replicateTab.getEplus(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).getEnrichedCodingStrand();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String strand) {
                        configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).setEnrichedCodingStrand(strand);
                    }
                });
        configFileBinder.bind(replicateTab.getEminus(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).getEnrichedTemplateStrand();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String strand) {
                        configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).setEnrichedTemplateStrand(strand);
                    }
                });
        configFileBinder.bind(replicateTab.getNplus(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).getNormalCodingStrand();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String strand) {
                        configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).setNormalCodingStrand(strand);
                    }
                });
        configFileBinder.bind(replicateTab.getNminus(),
                new ValueProvider<ConfigFile, String>() {
                    @Override
                    public String apply(ConfigFile configFile) {
                        return configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).getNormalTemplateStrand();
                    }
                },
                new Setter<ConfigFile, String>() {
                    @Override
                    public void accept(ConfigFile configFile, String strand) {
                        configFile.getGenomeList().get(datasetIndex).getReplicateList().get(replicateIndex).setNormalTemplateStrand(strand);
                    }
                });
    }


    public void setParamsCustom(boolean paramsCustom) {
        isParamsCustom = paramsCustom;
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

    public void updateReplicateID(int datasetIndex, int replicateIndex, String id) {
        configFile
                .getGenomeList()
                .get(datasetIndex)
                .getReplicateList()
                .get(replicateIndex)
                .setReplicateID(id);

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
                configFile.setStepHeight(1);
                configFile.setStepHeightReduction(0.5);
                configFile.setStepFactor(2);
                configFile.setStepFactorReduction(0.5);
                configFile.setEnrichmentFactor(3);
                configFile.setProcessingSiteFactor(1);
                configFile.setStepLength(0);
                configFile.setBaseHeight(0);
                break;
            case MORE_SPECIFIC:
                configFile.setStepHeight(0.5);
                configFile.setStepHeightReduction(0.2);
                configFile.setStepFactor(2);
                configFile.setStepFactorReduction(0.5);
                configFile.setEnrichmentFactor(2);
                configFile.setProcessingSiteFactor(1.2);
                configFile.setStepLength(0);
                configFile.setBaseHeight(0);
                break;
            case DEFAULT:
                configFile.setStepHeight(0.3);
                configFile.setStepHeightReduction(0.2);
                configFile.setStepFactor(2);
                configFile.setStepFactorReduction(0.5);
                configFile.setEnrichmentFactor(2);
                configFile.setProcessingSiteFactor(1.5);
                configFile.setStepLength(0);
                configFile.setBaseHeight(0);
                break;
            case MORE_SENSITIVE:
                configFile.setStepHeight(0.2);
                configFile.setStepHeightReduction(0.15);
                configFile.setStepFactor(1.5);
                configFile.setStepFactorReduction(0.5);
                configFile.setEnrichmentFactor(1.5);
                configFile.setProcessingSiteFactor(2);
                configFile.setStepLength(0);
                configFile.setBaseHeight(0);
                break;
            case VERY_SENSITIVE:
                configFile.setStepHeight(0.1);
                configFile.setStepHeightReduction(0.09);
                configFile.setStepFactor(1);
                configFile.setStepFactorReduction(0.);
                configFile.setEnrichmentFactor(1);
                configFile.setProcessingSiteFactor(3);
                configFile.setStepLength(0);
                configFile.setBaseHeight(0);
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

    //These methods are only for temporary use. They are needed because as of now,
    //the config file stores a genome name, fasta and gff for every dataset, regardless of the workflow variant
    //However, in the condition variant there's only one of these
    // --> this needs to be changed in the config logic itself
    //TODO: How does this fit into the binder pattern?
    public void updateAllGenomeFastas(String name) {
        for (int i = 0; i < configFile.getNumberOfDatasets(); i++) {
            //updateGenomeFasta(i, name);
        }

    }

    public void updateAllGenomeAnnotations(String annotation) {
        for (int i = 0; i < configFile.getNumberOfDatasets(); i++) {
           // updateGenomeGFF(i, annotation);
        }

    }


}
