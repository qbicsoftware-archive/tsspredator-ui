package view.firstImplementation;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import presenter.Presenter;
import view.ParametersView;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This is a component where the user can set every parameter of his TSSPredator run.
 *
 * @author jmueller
 */
public class ParametersPanel extends CustomComponent implements ParametersView {
    private Presenter presenter;
    private Panel parametersPanel;
    private Layout contentLayout;
    RadioButtonGroup<String> presetOrCustom;
    RadioButtonGroup<String> presetSelection;

    //These parameters are shown in custom mode only
    private VerticalLayout customParameters;
    Slider stepHeight, stepHeightReduction;
    Slider stepFactor, stepFactorReduction;
    Slider enrichmentFactor, processingSiteFactor;
    Slider stepLength, baseHeight;

    //These parameters are always shown
    private VerticalLayout basicParameters;
    Slider normalizationPercentile, enrichedNormalizationPercentile;
    ComboBox<String> clusterMethod;
    Slider clusteringDistance;
    Slider crossDatasetShift, crossReplicateShift;
    ComboBox<Integer> matchingReplicates;
    Slider utrLength, antisenseUtrLength;
    CheckBox writeGraphs;


    public ParametersPanel(Presenter presenter) {
        this.presenter = presenter;
        parametersPanel = designPanel();
        setCompositionRoot(parametersPanel);
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new VerticalLayout();

        presetOrCustom = new RadioButtonGroup<>("Parameters(?)");
        presetOrCustom.setItems("Preset", "Custom");
        presetOrCustom.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        presetSelection = new RadioButtonGroup<>("Choose Parameter Preset");
        presetSelection.setItems("Very Specific", "More Specific", "Default", "More Sensitive", "Very Sensitive");
        setupPresetListeners();
        createParameterLayouts();
        setupParameterListeners();
        setInitialValues();
        contentLayout.addComponents(presetOrCustom, presetSelection, customParameters, basicParameters);
        panel.setContent(contentLayout);
        return panel;
    }

    private void createParameterLayouts() {
        customParameters = new VerticalLayout();

        //TODO: Bind the max value of the reduction sliders to the other sliders so that they don't exceed them
        HorizontalLayout stepParams = new HorizontalLayout();
        stepHeight = new Slider("Step Height");
        stepHeight.setMin(0);
        stepHeight.setMax(1);
        stepHeight.setResolution(1);
        stepHeightReduction = new Slider("Step Height Reduction");
        stepHeightReduction.setMin(0);
        stepHeightReduction.setMax(1);
        stepHeightReduction.setResolution(1);
        stepFactor = new Slider("Step Factor");
        stepFactor.setMin(1);
        stepFactor.setMax(5);
        stepFactor.setResolution(1);
        stepFactorReduction = new Slider("Step Factor Reduction");
        stepFactorReduction.setMin(0);
        stepFactorReduction.setMax(2);
        stepFactorReduction.setResolution(1);
        stepParams.addComponents(stepHeight, stepHeightReduction, stepFactor, stepFactorReduction);

        HorizontalLayout otherCustomParams = new HorizontalLayout();
        enrichmentFactor = new Slider("Enrichment Factor");
        enrichmentFactor.setMin(0);
        enrichmentFactor.setMax(10);
        enrichmentFactor.setResolution(1);
        processingSiteFactor = new Slider("Processing Site Factor");
        processingSiteFactor.setMin(0);
        processingSiteFactor.setMax(10);
        processingSiteFactor.setResolution(1);
        stepLength = new Slider("Step Length");
        stepLength.setMin(0);
        stepLength.setMax(100);
        stepLength.setResolution(0);
        baseHeight = new Slider("Base Height (disabled by default)");
        baseHeight.setEnabled(false);
        otherCustomParams.addComponents(enrichmentFactor, processingSiteFactor, stepLength, baseHeight);

        customParameters.addComponents(stepParams, otherCustomParams);

        basicParameters = new VerticalLayout();

        HorizontalLayout percentiles = new HorizontalLayout();
        normalizationPercentile = new Slider("Normalization Percentile");
        normalizationPercentile.setMin(0);
        normalizationPercentile.setMax(1);
        normalizationPercentile.setResolution(1);
        enrichedNormalizationPercentile = new Slider("Enriched Normalization Percentile");
        enrichedNormalizationPercentile.setMin(0);
        enrichedNormalizationPercentile.setMax(1);
        enrichedNormalizationPercentile.setResolution(1);
        percentiles.addComponents(normalizationPercentile, enrichedNormalizationPercentile);
        HorizontalLayout methodAndDistance = new HorizontalLayout();
        clusterMethod = new ComboBox<>("Clustering Method");
        clusterMethod.setItems("HIGHEST", "FIRST");
        clusteringDistance = new Slider("TSS Clustering Distance");
        clusteringDistance.setMin(0);
        clusteringDistance.setMax(100);
        clusteringDistance.setResolution(0);
        methodAndDistance.addComponents(clusterMethod, clusteringDistance);

        HorizontalLayout allowedShifts = new HorizontalLayout();
        //TODO: Change depending on user choice: Genomes vs. Conditions
        crossDatasetShift = new Slider("Allowed Cross-Condition Shift");
        crossDatasetShift.setMin(0);
        crossDatasetShift.setMax(100);
        crossReplicateShift = new Slider("Allowed Cross-Replication Shift");
        crossReplicateShift.setMin(0);
        crossReplicateShift.setMax(100);
        allowedShifts.addComponents(crossDatasetShift, crossReplicateShift);
        matchingReplicates = new ComboBox<>("Matching Replicates");
        //TODO: Is this the most elegant way to do this?
        //TODO: Replace numReplicates with the actual number of replicates
        int numReplicates = 42;
        Collection<Integer> replicateList = new LinkedList<>();
        for (int i = 0; i < numReplicates; i++) {
            replicateList.add(i + 1);
        }
        matchingReplicates.setItems(replicateList);
        HorizontalLayout utrLengths = new HorizontalLayout();
        utrLength = new Slider("UTR length");
        utrLength.setMin(0);
        utrLength.setMax(1000);
        antisenseUtrLength = new Slider("Antisense UTR length");
        antisenseUtrLength.setMin(0);
        antisenseUtrLength.setMax(1000);
        utrLengths.addComponents(utrLength, antisenseUtrLength);
        writeGraphs = new CheckBox("Write RNA-Seq graphs");

        basicParameters.addComponents(percentiles, methodAndDistance, allowedShifts, matchingReplicates, utrLengths, writeGraphs);



    }

    private void setupPresetListeners() {
        presetOrCustom.addValueChangeListener(vce -> {
            presenter.setParamsCustom(vce.getValue().equals("Custom"));
            customParameters.setVisible(vce.getValue().equals("Custom"));
            presetSelection.setVisible(vce.getValue().equals("Preset"));
        });
        presetSelection.addValueChangeListener(vce -> {
            switch (vce.getValue()) {
                case "Very Specific":
                    presenter.setPreset(Presenter.Preset.VERY_SPECIFIC);
                    break;
                case "More Specific":
                    presenter.setPreset(Presenter.Preset.MORE_SPECIFIC);
                    break;
                case "Default":
                    presenter.setPreset(Presenter.Preset.DEFAULT);
                    break;
                case "More Sensitive":
                    presenter.setPreset(Presenter.Preset.MORE_SENSITIVE);
                    break;
                case "Very Sensitive":
                    presenter.setPreset(Presenter.Preset.VERY_SENSITIVE);
                    break;
            }
            presenter.applyPresetParameters();
        });

    }

    private void setupParameterListeners() {
        stepHeight.addValueChangeListener(vce -> presenter.updateStepHeight(vce.getValue()));
        stepHeightReduction.addValueChangeListener(vce -> presenter.updateStepHeightReduction(vce.getValue()));
        stepFactor.addValueChangeListener(vce -> presenter.updateStepFactor(vce.getValue()));
        stepFactorReduction.addValueChangeListener(vce -> presenter.updateStepFactorReduction(vce.getValue()));
        enrichmentFactor.addValueChangeListener(vce -> presenter.updateEnrichmentFactor(vce.getValue()));
        processingSiteFactor.addValueChangeListener(vce -> presenter.updateProcessingSiteFactor(vce.getValue()));
        stepLength.addValueChangeListener(vce -> presenter.updateStepLength(vce.getValue().intValue()));
        baseHeight.addValueChangeListener(vce -> presenter.updateBaseHeight(vce.getValue()));
        normalizationPercentile.addValueChangeListener(vce -> presenter.updateNormalizationPercentile(vce.getValue()));
        enrichedNormalizationPercentile.addValueChangeListener(vce -> presenter.updateEnrichmentNormalizationPercentile(vce.getValue()));
        clusterMethod.addValueChangeListener(vce -> presenter.updateClusterMethod(vce.getValue()));
        clusteringDistance.addValueChangeListener(vce -> presenter.updateClusteringDistance(vce.getValue().intValue()));
        crossDatasetShift.addValueChangeListener(vce -> presenter.updateAllowedCrossDatasetShift(vce.getValue().intValue()));
        crossReplicateShift.addValueChangeListener(vce -> presenter.updateAllowedCrossReplicateShift(vce.getValue().intValue()));
        matchingReplicates.addValueChangeListener(vce -> presenter.updateMatchingReplicates(vce.getValue()));
        utrLength.addValueChangeListener(vce -> presenter.updateUtrLength(vce.getValue().intValue()));
        antisenseUtrLength.addValueChangeListener(vce -> presenter.updateAntisenseUtrLength(vce.getValue().intValue()));
        writeGraphs.addValueChangeListener(vce -> presenter.updateWriteGraphs(vce.getValue()));


    }

    private void setInitialValues() {
        normalizationPercentile.setValue(0.9);
        enrichedNormalizationPercentile.setValue(0.5);
        clusterMethod.setValue("HIGHEST");
        clusteringDistance.setValue(3.);
        crossDatasetShift.setValue(1.);
        crossReplicateShift.setValue(1.);
        matchingReplicates.setValue(1);
        utrLength.setValue(300.);
        antisenseUtrLength.setValue(100.);
    }

    public RadioButtonGroup<String> getPresetOrCustom() {
        return presetOrCustom;
    }

    public RadioButtonGroup<String> getPresetSelection() {
        return presetSelection;
    }

    public Slider getStepHeight() {
        return stepHeight;
    }

    public Slider getStepHeightReduction() {
        return stepHeightReduction;
    }

    public Slider getStepFactor() {
        return stepFactor;
    }

    public Slider getStepFactorReduction() {
        return stepFactorReduction;
    }

    public Slider getEnrichmentFactor() {
        return enrichmentFactor;
    }

    public Slider getProcessingSiteFactor() {
        return processingSiteFactor;
    }

    public Slider getStepLength() {
        return stepLength;
    }

    public Slider getBaseHeight() {
        return baseHeight;
    }
}
