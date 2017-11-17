package view.firstImplementation;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import model.Globals;
import presenter.Presenter;

import java.io.File;
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
    RadioButtonGroup<String> presetSelection;

    //These parameters may be set by a preset
    private VerticalLayout customParameters;
    private ParameterSetter stepHeight, stepHeightReduction;
    private ParameterSetter stepFactor, stepFactorReduction;
    private ParameterSetter enrichmentFactor, processingSiteFactor;
    private ParameterSetter stepLength, baseHeight;

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
        presetSelection = new RadioButtonGroup<>("Choose Parameter Preset");
        //TODO: Add some kind of separator between Presets and "Custom"
        presetSelection.setItems("Very Specific", "More Specific", "Default", "More Sensitive", "Very Sensitive", Globals.PARAMETERS_CUSTOM);
        setupPresetListeners();
        createParameterLayouts();
        contentLayout.addComponents(new HorizontalLayout(presetSelection, customParameters), basicParameters);
        panel.setContent(contentLayout);
        return panel;
    }

    private void setupPresetListeners() {
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
                case Globals.PARAMETERS_CUSTOM:
                    presenter.setPreset(null);
            }
            presenter.applyPresetParameters();
        });

    }

    /**
     * Template for a component where the user can adjust a parameter.
     * Consists of a slider, a label displaying the slider's value and a button with info about the parameter
     */
    private class ParameterSetter extends CustomComponent {
        VerticalLayout layout;
        Slider slider;
        Button infoButton;
        Label valueDisplay;

        public ParameterSetter(String caption,
                               int minValue, int maxValue, int resolution,
                               File helpGraphic, String buttonStyle) {
            //Setup slider
            slider = new Slider(caption);
            slider.setMin(minValue);
            slider.setMax(maxValue);
            slider.setResolution(resolution);
            slider.addValueChangeListener(vce -> {
                if (vce.isUserOriginated()) {
                    presetSelection.setSelectedItem(Globals.PARAMETERS_CUSTOM);
                }
            });

            //Setup valueDisplay, bind label to value of slider
            valueDisplay = new Label();
            slider.addValueChangeListener(event -> {
                valueDisplay.setValue(String.valueOf(event.getValue()));
            });

            //Setup infoButton with helpGraphic
            infoButton = new Button(VaadinIcons.INFO_CIRCLE);
            infoButton.addStyleNames(
                    buttonStyle,
                    ValoTheme.BUTTON_ICON_ONLY,
                    ValoTheme.BUTTON_BORDERLESS,
                    ValoTheme.BUTTON_ICON_ALIGN_TOP);

            //TODO: Implement hovering, change layout to a fixed view
            VerticalLayout helpLayout = new VerticalLayout();
            FileResource resource = new FileResource(helpGraphic);
            Image image = new Image(null, resource);
            helpLayout.addComponent(image);
            PopupView helpView = new PopupView(null, helpLayout);
            contentLayout.addComponent(helpView);
            contentLayout.setComponentAlignment(helpView, Alignment.MIDDLE_CENTER);
            infoButton.addClickListener(e -> helpView.setPopupVisible(true));

            //Create layout, put all components there and set as root
            layout = new VerticalLayout();
            layout.addComponents(new HorizontalLayout(slider, infoButton), valueDisplay);
            layout.addStyleNames("layout-with-border");
            setCompositionRoot(layout);
        }
    }

    private void createParameterLayouts() {
        customParameters = new VerticalLayout();
        String basepath = VaadinService.getCurrent().getBaseDirectory().getParent();

        //TODO: Bind the max value of the reduction sliders to the other sliders so that they don't exceed them
        HorizontalLayout stepParams = new HorizontalLayout();
        stepHeight = new ParameterSetter("Step Height",
                0, 1, 1,
                new File(basepath + "/resources/Dummy.svg"), "stepHeightInfo");
        stepHeightReduction = new ParameterSetter("Step Height Reduction",
                0, 1, 1,
                new File(basepath + "/resources/Dummy.svg"), "stepHeightReductionInfo");
        stepFactor = new ParameterSetter("Step Factor",
                1, 5, 1,
                new File(basepath + "/resources/Dummy.svg"), "stepFactorInfo");
        stepFactorReduction = new ParameterSetter("Step Factor Reduction",
                0, 2, 1,
                new File(basepath + "/resources/Dummy.svg"), "stepFactorReductionInfo");
        stepParams.addComponents(stepHeight, stepHeightReduction, stepFactor, stepFactorReduction);

        HorizontalLayout otherCustomParams = new HorizontalLayout();
        enrichmentFactor = new ParameterSetter("Enrichment Factor",
                0, 10, 1,
                new File(basepath + "/resources/Dummy.svg"), "enrichmentFactorInfo");
        processingSiteFactor = new ParameterSetter("Processing Site Factor",
                0, 10, 1,
                new File(basepath + "/resources/Dummy.svg"), "processingSiteFactorInfo");
        stepLength = new ParameterSetter("Step Length",
                0, 100, 0,
                new File(basepath + "/resources/Dummy.svg"), "stepLengthInfo");
        baseHeight = new ParameterSetter("Base Height (disabled by default)",
                0, 1, 0,
                new File(basepath + "/resources/Dummy.svg"), "baseHeightInfo");
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


    public RadioButtonGroup<String> getPresetSelection() {
        return presetSelection;
    }

    public Slider getStepHeight() {
        return stepHeight.slider;
    }

    public Slider getStepHeightReduction() {
        return stepHeightReduction.slider;
    }

    public Slider getStepFactor() {
        return stepFactor.slider;
    }

    public Slider getStepFactorReduction() {
        return stepFactorReduction.slider;
    }

    public Slider getEnrichmentFactor() {
        return enrichmentFactor.slider;
    }

    public Slider getProcessingSiteFactor() {
        return processingSiteFactor.slider;
    }

    public Slider getStepLength() {
        return stepLength.slider;
    }

    public Slider getBaseHeight() {
        return baseHeight.slider;
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
