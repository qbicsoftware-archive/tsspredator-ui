package presenter;

import com.vaadin.data.*;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Setter;
import com.vaadin.ui.Notification;
import model.Globals;
import model.beans.*;
import model.config.ConfigFile;
import model.config.Genome;
import model.config.Replicate;
import view.AccordionLayoutMain;
import view.firstImplementation.ConditionDataPanel;
import view.firstImplementation.DataPanel;
import view.firstImplementation.GenomeDataPanel;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jmueller
 */
public class Presenter {
    private ConfigFile configFile;
    private AccordionLayoutMain view;
    private Binder<ConfigFile> configFileBinder;
    private final static Logger presenterLogger = Logger.getLogger(Presenter.class.getName());


    public enum Preset {
        VERY_SPECIFIC, MORE_SPECIFIC, DEFAULT, MORE_SENSITIVE, VERY_SENSITIVE

    }

    private Preset preset = Preset.DEFAULT;

    public Presenter() {
        configFile = new ConfigFile();
        configFile.setGenomeList(new ArrayList<>());
        //We start with one Dataset and one replicate
        addDatasets(1);
        addReplicates(1);
        setInitialConfigParameters();
        configFileBinder = new Binder<>();
        configFileBinder.setBean(configFile);
    }

    /**
     * Initializes the fields of the view
     * TODO: Move stuff from the view here so the view contains less logic
     */
    public void initFields() {
        view.getParametersPanel().getPresetSelection().setSelectedItem("Default");
        view.getGenomeDataPanel().initAccordion();

    }

    public void initBindings() {
        configFileBinder.forField(view.getGeneralConfigPanel().getProjectGrid().asSingleSelect())
                .withValidator(projectBean -> {
                    if (projectBean.toString().equals("null (null)")) {
                        view.getGeneralConfigPanel().getProjectGrid().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please select a project file by double-clicking it!";
                            }
                        });
                    } else {
                        view.getGeneralConfigPanel().getProjectGrid().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, ProjectBean>) configFile -> {
                            return new ProjectBean(); //TODO: Return something useful here
                        },
                        (Setter<ConfigFile, ProjectBean>) (configFile, projectBean) -> configFile.setProjectName(projectBean.getName()));


        configFileBinder.forField(view.getGenomeDataPanel().getNumberOfDatasetsBox())
                .asRequired("Please set a number")
                .withValidator(new IntegerRangeValidator("Please set at least to 1", 1, Integer.MAX_VALUE))
                .bind(
                        (ValueProvider<ConfigFile, Integer>) configFile1 -> configFile1.getNumberOfDatasets(),
                        (Setter<ConfigFile, Integer>) (configFile, number) -> {
                            int oldDatasetCount = configFile.getNumberOfDatasets();
                            //Add or remove datasets in the ConfigFile so they match the given number
                            int delta = number - oldDatasetCount;
                            configFile.setNumberOfDatasets(number);
                            if (delta > 0) {
                                addDatasets(delta);
                            } else {
                                removeDatasets(-delta);
                            }
                            //Update view
                            view.getGenomeDataPanel().updateAccordion(oldDatasetCount, getNumberOfReplicates());

                        });
        configFileBinder.forField(view.getConditionDataPanel().getNumberOfDatasetsBox())
                .asRequired("Please set a number")
                .withValidator(new IntegerRangeValidator("Please set at least to 1", 1, Integer.MAX_VALUE))
                .bind(
                        (ValueProvider<ConfigFile, Integer>) configFile1 -> configFile1.getNumberOfDatasets(),
                        (Setter<ConfigFile, Integer>) (configFile, number) -> {
                            int oldDatasetCount = configFile.getNumberOfDatasets();
                            //Add or remove datasets in the ConfigFile so they match the given number
                            int delta = number - oldDatasetCount;
                            configFile.setNumberOfDatasets(number);
                            if (delta > 0)
                                addDatasets(delta);
                            else
                                removeDatasets(-delta);
                            //Update view
                            view.getConditionDataPanel().updateAccordion(oldDatasetCount, getNumberOfReplicates());
                        });

        configFileBinder.forField(view.getGenomeDataPanel().getNumberOfReplicatesBox())
                .asRequired("Please set a number")
                .withValidator(new IntegerRangeValidator("Please set at least to 1", 1, Integer.MAX_VALUE))
                .bind((ValueProvider<ConfigFile, Integer>) configFile1 -> configFile1.getNumberOfReplicates(),
                        (Setter<ConfigFile, Integer>) (configFile, number) -> {
                            int oldReplicateCount = configFile.getNumberOfReplicates();
                            int delta = number - oldReplicateCount;
                            configFile.setNumberOfReplicates(number);
                            //Add or remove replicates so they match the given number
                            if (delta > 0)
                                addReplicates(delta);
                            else
                                removeReplicates(-delta);
                            //Update view
                            view.getGenomeDataPanel().updateAccordion(getNumberOfDatasets(), oldReplicateCount);
                        });

        configFileBinder.forField(view.getConditionDataPanel().getNumberOfReplicatesBox())
                .asRequired("Please set a number")
                .withValidator(new IntegerRangeValidator("Please set at least to 1", 1, Integer.MAX_VALUE))
                .bind(configFile1 -> configFile1.getNumberOfReplicates(),
                        (Setter<ConfigFile, Integer>) (configFile, number) -> {
                            int oldReplicateCount = configFile.getNumberOfReplicates();
                            int delta = number - oldReplicateCount;
                            configFile.setNumberOfReplicates(number);
                            //Add or remove replicates so they match the given number
                            if (delta > 0)
                                addReplicates(delta);
                            else
                                removeReplicates(-delta);
                            view.getConditionDataPanel().updateAccordion(getNumberOfDatasets(), oldReplicateCount);
                        });

        configFileBinder.forField(view.getGeneralConfigPanel().getProjectTypeButtonGroup())
                .bind(new ValueProvider<ConfigFile, String>() {
                          @Override
                          public String apply(ConfigFile configFile) {
                              return configFile.isModeConditions() ? Globals.COMPARE_CONDITIONS : Globals.COMPARE_GENOMES;
                          }
                      },
                        new Setter<ConfigFile, String>() {
                            @Override
                            public void accept(ConfigFile configFile, String s) {
                                view.getGenomeDataPanel().getNumberOfDatasetsBox().setValue(1);
                                view.getGenomeDataPanel().getNumberOfReplicatesBox().setValue(1);
                                view.getConditionDataPanel().getNumberOfDatasetsBox().setValue(1);
                                view.getConditionDataPanel().getNumberOfReplicatesBox().setValue(1);


                                boolean isModeConditions = s.equals(Globals.COMPARE_CONDITIONS);
                                view.updateDataPanelMode(isModeConditions);
                                configFile.setModeConditions(isModeConditions);
                                configFile.setNumberOfDatasets(1);
                                configFile.setNumberOfReplicates(1);
                                view.getGeneralConfigPanel().getAlignmentFileGrid().setVisible(!isModeConditions);
                                if (isModeConditions) {
                                    view.getGenomeDataPanel().getDatasetAccordion().removeAllComponents();
                                    view.getConditionDataPanel().initAccordion();
                                } else {
                                    view.getConditionDataPanel().getDatasetAccordion().removeAllComponents();
                                    view.getGenomeDataPanel().initAccordion();
                                }


                            }
                        }
                );
        configFileBinder.forField(view.getGeneralConfigPanel().getAlignmentFileGrid().asSingleSelect())
                .withValidator(alignmentFileBean -> {
                    if (alignmentFileBean.toString().equals("null (null, 0kB)")) {
                        view.getGeneralConfigPanel().getAlignmentFileGrid().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please select an alignment file by double-clicking it!";
                            }
                        });
                    } else {
                        view.getGeneralConfigPanel().getAlignmentFileGrid().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, AlignmentFileBean>) configFile -> {
                            return new AlignmentFileBean(); //TODO: Return something useful here
                        },
                        (Setter<ConfigFile, AlignmentFileBean>) (configFile, alignmentFileBean) -> configFile.setAlignmentFile(alignmentFileBean.getName()));

        configFileBinder.forField(view.getParametersPanel().getWriteGraphs())
                .bind(ConfigFile::isWriteGraphs,
                        ConfigFile::setWriteGraphs);
        configFileBinder.forField(view.getParametersPanel().getStepHeight()).bind(
                ConfigFile::getStepHeight,
                ConfigFile::setStepHeight);
        configFileBinder.forField(view.getParametersPanel().getStepHeightReduction()).bind(
                ConfigFile::getStepHeightReduction,
                ConfigFile::setStepHeightReduction);
        configFileBinder.forField(view.getParametersPanel().getStepFactor()).bind(
                ConfigFile::getStepFactor,
                ConfigFile::setStepFactor);
        configFileBinder.forField(view.getParametersPanel().getStepFactorReduction()).bind(
                ConfigFile::getStepFactorReduction,
                ConfigFile::setStepFactorReduction);
        configFileBinder.forField(view.getParametersPanel().getEnrichmentFactor()).bind(
                ConfigFile::getEnrichmentFactor,
                ConfigFile::setEnrichmentFactor);
        configFileBinder.forField(view.getParametersPanel().getProcessingSiteFactor()).bind(
                ConfigFile::getProcessingSiteFactor,
                ConfigFile::setProcessingSiteFactor);
        configFileBinder.forField(view.getParametersPanel().getStepLength())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getStepLength, ConfigFile::setStepLength);
        configFileBinder.forField(view.getParametersPanel().getBaseHeight()).bind(
                ConfigFile::getBaseHeight,
                ConfigFile::setBaseHeight);
        configFileBinder.forField(view.getParametersPanel().getNormalizationPercentile()).bind(
                ConfigFile::getNormalizationPercentile,
                ConfigFile::setNormalizationPercentile);
        configFileBinder.forField(view.getParametersPanel().getTreatedNormalizationPercentile()).bind(
                ConfigFile::getEnrichmentNormalizationPercentile,
                ConfigFile::setEnrichmentNormalizationPercentile);
        configFileBinder.forField(view.getParametersPanel().getClusterMethod()).bind(
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
        configFileBinder.forField(view.getParametersPanel().getMatchingReplicates()).bind(
                ConfigFile::getMatchingReplicates,
                (configFile, value) -> {
                    if (value != null)
                        configFile.setMatchingReplicates(value);

                });
        configFileBinder.forField(view.getParametersPanel().getUtrLength())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getUtrLength, ConfigFile::setUtrLength);
        configFileBinder.forField(view.getParametersPanel().getAntisenseUtrLength())
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(ConfigFile::getAntisenseUtrLength, ConfigFile::setAntisenseUtrLength);

        //Bindings for conditionDataPanel exclusively
        configFileBinder.forField(view.getConditionDataPanel().getFastaGrid().asSingleSelect())
                .withValidator(fastaFileBean -> {
                    if (fastaFileBean.toString().equals("null (null, 0kB)")) {
                        view.getConditionDataPanel().getFastaGrid().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please select a Fasta file by double clicking it!";
                            }
                        });
                    } else {
                        view.getConditionDataPanel().getFastaGrid().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, FastaFileBean>) configFile -> new FastaFileBean(),
                        (Setter<ConfigFile, FastaFileBean>) (configFile, fastaFileBean) -> configFile.setConditionFasta(fastaFileBean.getName()));
        configFileBinder.forField(view.getConditionDataPanel().getGffGrid().asSingleSelect())
                .withValidator(annotationFileBean -> {
                    if (annotationFileBean.toString().equals("null (null, 0kB)")) {
                        view.getConditionDataPanel().getGffGrid().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please select an annotation file by double-clicking it!";
                            }
                        });
                    } else {
                        view.getConditionDataPanel().getGffGrid().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, AnnotationFileBean>) configFile -> new AnnotationFileBean(),
                        (Setter<ConfigFile, AnnotationFileBean>) (configFile, annotationFileBean) -> configFile.setConditionGFF(annotationFileBean.getName()));

        //Bind the matching replicates combobox in the parameters panel
        // to the number of replicates comboboxes in the two data panels
        // using change listeners
        view.getGenomeDataPanel().getNumberOfReplicatesBox().addValueChangeListener(vce -> {
            view.getParametersPanel().getMatchingReplicates().setItems(IntStream.rangeClosed(1, getNumberOfReplicates())
                    .boxed().collect(Collectors.toList()));
        });
        view.getConditionDataPanel().getNumberOfReplicatesBox().addValueChangeListener(vce -> {
            view.getParametersPanel().getMatchingReplicates().setItems(IntStream.rangeClosed(1, getNumberOfReplicates())
                    .boxed().collect(Collectors.toList()));
        });

    }

    public void setInitialConfigParameters() {
        configFile.setModeConditions(Globals.IS_CONDITIONS_INIT);
        configFile.setNumberOfDatasets(Globals.NUMBER_OF_DATASETS_INIT);
        configFile.setNumberOfReplicates(Globals.NUMBER_OF_REPLICATES_INIT);
        configFile.setNormalizationPercentile(Globals.NORMALIZATION_PERCENTILE_INIT);
        configFile.setEnrichmentNormalizationPercentile(Globals.ENRICHMENT_NORMALIZATION_PERCENTILE_INIT);
        configFile.setClusterMethod(Globals.CLUSTER_METHOD_INIT);
        configFile.setTssClusteringDistance(Globals.TSS_CLUSTERING_DISTANCE_INIT);
        configFile.setAllowedCrossDatasetShift(Globals.CROSS_DATASET_SHIFT_INIT);
        configFile.setAllowedCrossReplicateShift(Globals.CROSS_REPLICATE_SHIFT_INIT);
        configFile.setMatchingReplicates(Globals.MATCHING_REPLICATES_INIT);
        configFile.setUtrLength(Globals.UTR_LENGTH_INIT);
        configFile.setAntisenseUtrLength(Globals.ANTISENSE_UTR_LENGTH_INIT);
    }

    public void initDatasetBindings(int index) {
        if (configFile.isModeConditions()) {
            ConditionDataPanel.ConditionTab conditionTab = (ConditionDataPanel.ConditionTab) view.getConditionDataPanel().getDatasetTab(index);
            configFileBinder.forField(conditionTab.getNameField()).asRequired("Please enter the name of the condition")
                    .bind(new ValueProvider<ConfigFile, String>() {
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
        } else {
            GenomeDataPanel.GenomeTab genomeTab = (GenomeDataPanel.GenomeTab) view.getGenomeDataPanel().getDatasetTab(index);
            configFileBinder.forField(genomeTab.getNameField()).asRequired("Please enter the name of the genome")
                    .bind(new ValueProvider<ConfigFile, String>() {
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

            configFileBinder.forField(genomeTab.getFastaGrid().asSingleSelect())
                    .withValidator(fastaFileBean -> {
                        if (fastaFileBean.toString().equals("null (null, 0kB)")) {
                            genomeTab.getFastaGrid().setComponentError(new ErrorMessage() {
                                @Override
                                public ErrorLevel getErrorLevel() {
                                    return null;
                                }

                                @Override
                                public String getFormattedHtmlMessage() {
                                    return "Please select the fasta file of this genome (*.fasta)!";
                                }
                            });
                        } else {
                            genomeTab.getFastaGrid().setComponentError(null);
                        }
                        return true;
                    }, "")
                    .bind((ValueProvider<ConfigFile, FastaFileBean>) configFile -> new FastaFileBean(),
                            (Setter<ConfigFile, FastaFileBean>) (configFile, fastaFileBean) -> configFile.getGenomeList().get(index).setFasta(fastaFileBean.getName()));

            configFileBinder.forField(genomeTab.getIdField()).asRequired("Please enter the alignment id of this genome")
                    .bind(new ValueProvider<ConfigFile, String>() {
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

            configFileBinder.forField(genomeTab.getGffGrid().asSingleSelect())
                    .withValidator(annotationFileBean -> {
                        if (annotationFileBean.toString().equals("null (null, 0kB)")) {
                            genomeTab.getGffGrid().setComponentError(new ErrorMessage() {
                                @Override
                                public ErrorLevel getErrorLevel() {
                                    return null;
                                }

                                @Override
                                public String getFormattedHtmlMessage() {
                                    return "Please select the annotation file of this genome (*.gff)";
                                }
                            });
                        } else {
                            genomeTab.getGffGrid().setComponentError(null);
                        }
                        return true;
                    }, "")
                    .bind((ValueProvider<ConfigFile, AnnotationFileBean>) configFile -> new AnnotationFileBean(),
                            (Setter<ConfigFile, AnnotationFileBean>) (configFile, annotationFileBean) -> configFile.getGenomeList().get(index).setGff(annotationFileBean.getName()));
        }


    }


    public void initReplicateBindings(int datasetIndex, int replicateIndex) {
        DataPanel.ReplicateTab replicateTab;
        if (configFile.isModeConditions()) {
            replicateTab = view.getConditionDataPanel().getDatasetTab(datasetIndex).getReplicateTab(replicateIndex);
        } else {
            replicateTab = view.getGenomeDataPanel().getDatasetTab(datasetIndex).getReplicateTab(replicateIndex);
        }

        configFileBinder.forField(replicateTab.getTreatedCoding().asSingleSelect())
                .withValidator(graphFileBean -> {
                    if (graphFileBean.toString().equals("null (null, 0kB)")) {
                        replicateTab.getTreatedCoding().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please drag a graph file here!";
                            }
                        });
                    } else {
                        replicateTab.getTreatedCoding().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, GraphFileBean>) configFile -> {
                            return new GraphFileBean(); //TODO: Do something useful here
                        }, (Setter<ConfigFile, GraphFileBean>) (configFile, graphFileBean) -> {
                            if (graphFileBean != null) {
                                configFile.getGenomeList().get(datasetIndex)
                                        .getReplicateList().get(replicateIndex)
                                        .setTreatedCodingStrand(graphFileBean.getName());
                            }
                        }
                );
        configFileBinder.forField(replicateTab.getTreatedTemplate().asSingleSelect())
                .withValidator(graphFileBean -> {
                    if (graphFileBean.toString().equals("null (null, 0kB)")) {
                        replicateTab.getTreatedTemplate().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please drag a graph file here!";
                            }
                        });
                    } else {
                        replicateTab.getTreatedTemplate().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, GraphFileBean>) configFile -> new GraphFileBean(),
                        (Setter<ConfigFile, GraphFileBean>) (configFile, graphFileBean) -> {
                            if (graphFileBean != null) {
                                configFile.getGenomeList().get(datasetIndex)
                                        .getReplicateList().get(replicateIndex)
                                        .setTreatedTemplateStrand(graphFileBean.getName());
                            }
                        });
        configFileBinder.forField(replicateTab.getUntreatedCoding().asSingleSelect())
                .withValidator(graphFileBean -> {
                    if (graphFileBean.toString().equals("null (null, 0kB)")) {
                        replicateTab.getUntreatedCoding().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please drag a graph file here!";
                            }
                        });
                    } else {
                        replicateTab.getUntreatedCoding().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, GraphFileBean>) configFile -> new GraphFileBean(),
                        (Setter<ConfigFile, GraphFileBean>) (configFile, graphFileBean) -> {
                            if (graphFileBean != null) {
                                configFile.getGenomeList().get(datasetIndex)
                                        .getReplicateList().get(replicateIndex)
                                        .setUntreatedCodingStrand(graphFileBean.getName());
                            }
                        });
        configFileBinder.forField(replicateTab.getUntreatedTemplate().asSingleSelect())
                .withValidator(graphFileBean -> {
                    if (graphFileBean.toString().equals("null (null, 0kB)")) {
                        replicateTab.getUntreatedTemplate().setComponentError(new ErrorMessage() {
                            @Override
                            public ErrorLevel getErrorLevel() {
                                return null;
                            }

                            @Override
                            public String getFormattedHtmlMessage() {
                                return "Please drag a graph file here!";
                            }
                        });
                    } else {
                        replicateTab.getUntreatedTemplate().setComponentError(null);
                    }
                    return true;
                }, "")
                .bind((ValueProvider<ConfigFile, GraphFileBean>) configFile -> new GraphFileBean(),
                        (Setter<ConfigFile, GraphFileBean>) (configFile, graphFileBean) -> {
                            if (graphFileBean != null) {
                                configFile.getGenomeList().get(datasetIndex)
                                        .getReplicateList().get(replicateIndex)
                                        .setUntreatedTemplateStrand(graphFileBean.getName());
                            }
                        });
    }

    public void addDatasets(int datasetsToAdd) {
        for (int i = 0; i < datasetsToAdd; i++) {
            Genome currentGenome = new Genome();
            //IDs of conditions are set automatically
            if (configFile.isModeConditions()) {
                currentGenome.setAlignmentID("" + (configFile.getNumberOfDatasets() - datasetsToAdd + i));
            }
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
        File file = new File(Globals.CONFIG_FILE_TMP_PATH);
        //Check all validators and fields that are set as required
        BinderValidationStatus<ConfigFile> validationStatus = configFileBinder.validate();
        //There will always be 'silent' errors because of non-visible fields.
        //If we're in condition mode, it's 3 errors, in genome mode it's 4 errors
        int silentErrors = configFile.isModeConditions() ? 3 : 4;

        if (validationStatus.getValidationErrors().size() > silentErrors) {
            Notification.show("There are errors (" + (validationStatus.getValidationErrors().size() - silentErrors)
                    + "). Please check the highlighted fields.");
        } else {
            try {
                String configText = configFile.toString();
                FileWriter writer = new FileWriter(file);
                writer.write(configText);
                writer.close();
            } catch (Exception e) {
                presenterLogger.log(Level.ALL, "Error producing config file:" + e.getMessage());
            }
        }
        return file;

    }

    public void applyPresetParameters() {
        if (preset == null) { //Happens when custom is selected
            return;
        }
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
        configFileBinder.readBean(configFile);

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

    public boolean isModeConditions() {
        return configFile.isModeConditions();
    }

    public int getNumberOfDatasets() {
        return configFile.getNumberOfDatasets();
    }

    public int getNumberOfReplicates() {
        return configFile.getNumberOfReplicates();
    }
}
