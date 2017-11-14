package view.firstImplementation;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.server.Setter;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import model.Globals;
import presenter.Presenter;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a component where the user can set every parameter of his TSSPredator run.
 *
 * @author jmueller
 */
public class ParametersPanel extends CustomComponent {
    private Presenter presenter;
    private Panel parametersPanel;
    private VerticalLayout contentLayout;
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

        presetOrCustom = new RadioButtonGroup<>();
        presetOrCustom.setItems(Globals.PARAMETERS_PRESET, Globals.PARAMETERS_CUSTOM);
        presetOrCustom.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        presetSelection = new RadioButtonGroup<>("Choose Parameter Preset");
        presetSelection.setItems("Very Specific", "More Specific", "Default", "More Sensitive", "Very Sensitive");
        setupPresetListeners();
        createParameterLayouts();
        contentLayout.addComponents(presetOrCustom, presetSelection, customParameters, basicParameters);
        panel.setContent(contentLayout);
        return panel;
    }

    private void setupPresetListeners() {
        presetOrCustom.addValueChangeListener(vce -> {
            customParameters.setVisible(vce.getValue().equals(Globals.PARAMETERS_CUSTOM));
            presetSelection.setVisible(vce.getValue().equals(Globals.PARAMETERS_PRESET));
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

    private void createParameterLayouts() {
        customParameters = new VerticalLayout();

        VerticalLayout helpLayout = new VerticalLayout();
        String basepath = VaadinService.getCurrent().getBaseDirectory().getParent();
        FileResource resource = new FileResource(new File(basepath + "/resources/Dummy.svg"));
        Image image = new Image(null, resource);
        helpLayout.addComponent(image);
        PopupView helpView = new PopupView(null, helpLayout);
        contentLayout.addComponent(helpView);
        contentLayout.setComponentAlignment(helpView, Alignment.MIDDLE_CENTER);

        //TODO: Bind the max value of the reduction sliders to the other sliders so that they don't exceed them
        HorizontalLayout stepParams = new HorizontalLayout();
        stepHeight = new Slider("Step Height");
        stepHeight.setMin(0);
        stepHeight.setMax(1);
        stepHeight.setResolution(1);
        //TODO: Create ONE style and set this style to all of the buttons here
        Button stepHeightInfo = new Button(VaadinIcons.INFO_CIRCLE);
        stepHeightInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        stepHeightInfo.addStyleName("my-info-button");
        stepHeightInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });

        stepHeightReduction = new Slider("Step Height Reduction");
        stepHeightReduction.setMin(0);
        stepHeightReduction.setMax(1);
        stepHeightReduction.setResolution(1);
        Button stepHeightReductionInfo = new Button(VaadinIcons.INFO_CIRCLE);
        stepHeightReductionInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        stepHeightReductionInfo.addStyleName("my-info-button");
        stepHeightReductionInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });

        stepFactor = new Slider("Step Factor");
        stepFactor.setMin(1);
        stepFactor.setMax(5);
        stepFactor.setResolution(1);
        Button stepFactorInfo = new Button(VaadinIcons.INFO_CIRCLE);
        stepFactorInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        stepFactorInfo.addStyleName("my-info-button");
        stepFactorInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });

        stepFactorReduction = new Slider("Step Factor Reduction");
        stepFactorReduction.setMin(0);
        stepFactorReduction.setMax(2);
        stepFactorReduction.setResolution(1);
        Button stepFactorReductionInfo = new Button(VaadinIcons.INFO_CIRCLE);
        stepFactorReductionInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        stepFactorReductionInfo.addStyleName("my-info-button");
        stepFactorReductionInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });

        stepParams.addComponents(
                new HorizontalLayout(stepHeight, stepHeightInfo),
                new HorizontalLayout(stepHeightReduction, stepHeightReductionInfo),
                new HorizontalLayout(stepFactor, stepFactorInfo),
                new HorizontalLayout(stepFactorReduction, stepFactorReductionInfo));

        HorizontalLayout otherCustomParams = new HorizontalLayout();
        enrichmentFactor = new Slider("Enrichment Factor");
        enrichmentFactor.setMin(0);
        enrichmentFactor.setMax(10);
        enrichmentFactor.setResolution(1);
        Button enrichmentFactorInfo = new Button(VaadinIcons.INFO_CIRCLE);
        enrichmentFactorInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        enrichmentFactorInfo.addStyleName("my-info-button");
        enrichmentFactorInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });

        processingSiteFactor = new Slider("Processing Site Factor");
        processingSiteFactor.setMin(0);
        processingSiteFactor.setMax(10);
        processingSiteFactor.setResolution(1);
        Button processingSiteFactorInfo = new Button(VaadinIcons.INFO_CIRCLE);
        processingSiteFactorInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        processingSiteFactorInfo.addStyleName("my-info-button");
        processingSiteFactorInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });

        stepLength = new Slider("Step Length");
        stepLength.setMin(0);
        stepLength.setMax(100);
        stepLength.setResolution(0);
        Button stepLengthInfo = new Button(VaadinIcons.INFO_CIRCLE);
        stepLengthInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        stepLengthInfo.addStyleName("my-info-button");
        stepLengthInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });

        baseHeight = new Slider("Base Height (disabled by default)");
        baseHeight.setEnabled(false);
        Button baseHeightInfo = new Button(VaadinIcons.INFO_CIRCLE);
        baseHeightInfo.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ALIGN_TOP);
        baseHeightInfo.addStyleName("my-info-button");
        baseHeightInfo.addClickListener(clickEvent -> {
            helpView.setPopupVisible(true);
        });
        baseHeightInfo.setVisible(false);
        otherCustomParams.addComponents(
                new HorizontalLayout(enrichmentFactor, enrichmentFactorInfo),
                new HorizontalLayout(processingSiteFactor, processingSiteFactorInfo),
                new HorizontalLayout(stepLength, stepLengthInfo),
                new HorizontalLayout(baseHeight, baseHeightInfo));

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
        clusterMethod.setItems(Globals.CLUSTER_METHOD_HIGHEST, Globals.CLUSTER_METHOD_FIRST);
        clusteringDistance = new Slider("TSS Clustering Distance");
        clusteringDistance.setMin(0);
        clusteringDistance.setMax(100);
        clusteringDistance.setResolution(0);
        methodAndDistance.addComponents(clusterMethod, clusteringDistance);

        HorizontalLayout allowedShifts = new HorizontalLayout();
        crossDatasetShift = new Slider();
        crossDatasetShift.setCaption(presenter.isModeConditions()
                ? "Allowed Cross-Condition Shift"
                : "Allowed Cross-Genome Shift");
        crossDatasetShift.setMin(0);
        crossDatasetShift.setMax(100);
        crossReplicateShift = new Slider("Allowed Cross-Replication Shift");
        crossReplicateShift.setMin(0);
        crossReplicateShift.setMax(100);
        allowedShifts.addComponents(crossDatasetShift, crossReplicateShift);
        matchingReplicates = new ComboBox<>("Matching Replicates");
        matchingReplicates.setItems(IntStream.rangeClosed(1, presenter.getNumberOfReplicates())
                        .boxed().collect(Collectors.toList()));
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

    public Slider getNormalizationPercentile() {
        return normalizationPercentile;
    }

    public Slider getEnrichedNormalizationPercentile() {
        return enrichedNormalizationPercentile;
    }

    public ComboBox<String> getClusterMethod() {
        return clusterMethod;
    }

    public Slider getClusteringDistance() {
        return clusteringDistance;
    }

    public Slider getCrossDatasetShift() {
        return crossDatasetShift;
    }

    public Slider getCrossReplicateShift() {
        return crossReplicateShift;
    }

    public ComboBox<Integer> getMatchingReplicates() {
        return matchingReplicates;
    }

    public Slider getUtrLength() {
        return utrLength;
    }

    public Slider getAntisenseUtrLength() {
        return antisenseUtrLength;
    }

    public CheckBox getWriteGraphs() {
        return writeGraphs;
    }
}
