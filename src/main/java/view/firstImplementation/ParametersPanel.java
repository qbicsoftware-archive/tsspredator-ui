package view.firstImplementation;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
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
    //Normalization part
    private VerticalLayout normalizationLayout;
    ParameterSetter normalizationPercentile;
    ParameterSetter enrichmentNormalizationPercentile;
    CheckBox writeNormalizedGraphs;

    //Pre-prediction part
    private VerticalLayout prePredictionLayout;
    private ParameterSetter stepHeight, stepHeightReduction;
    private ParameterSetter stepFactor, stepFactorReduction;
    private ParameterSetter enrichmentFactor, processingSiteFactor;
    private ParameterSetter stepLength, baseHeight;

    //Post-prediction part
    private VerticalLayout postPredictionLayout;
    ComboBox<String> clusterMethod;
    ParameterSetter clusteringDistance;
    ParameterSetter crossDatasetShift;
    ParameterSetter crossReplicateShift;
    ComboBox<Integer> matchingReplicates;
    ParameterSetter utrLength;
    ParameterSetter antisenseUtrLength;

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
        contentLayout.addComponents(new InfoBar(Globals.PARAMETER_INFO), normalizationLayout, prePredictionLayout, postPredictionLayout);
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
                               int minValue, int maxValue, int resolution, String imagePath) {
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
                if (resolution == 0) {
                    valueDisplay.setValue(String.valueOf(event.getValue().intValue()));
                } else {
                    valueDisplay.setValue(String.valueOf(event.getValue()));
                }

                if (caption.contains("Shift") || caption.contains("UTR") || caption.contains("Clustering Distance")) {
                    valueDisplay.setValue(valueDisplay.getValue() + " Base Pairs");
                }
            });

            //Setup infoButton with helpGraphic as Tooltip
            infoButton = new Button(VaadinIcons.INFO_CIRCLE);
            infoButton.addStyleNames(
                    ValoTheme.BUTTON_ICON_ONLY,
                    ValoTheme.BUTTON_BORDERLESS,
                    ValoTheme.BUTTON_ICON_ALIGN_TOP,
                    ValoTheme.BUTTON_SMALL);
            infoButton.setDescription("<img src=\"" + imagePath + "\"/>", ContentMode.HTML);
            //Create layout, put all components there and set as root
            layout = new VerticalLayout();
            layout.addComponents(new HorizontalLayout(slider, infoButton), valueDisplay);
            layout.setComponentAlignment(valueDisplay, Alignment.BOTTOM_CENTER);
            layout.addStyleNames("layout-with-border");
            layout.setHeight(75, Unit.PERCENTAGE);
            setCompositionRoot(layout);
        }
    }

    private void createParameterLayouts() {

        //Normalization Part
        normalizationPercentile = new ParameterSetter(
                "Normalization Percentile", 0, 1, 1, "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        enrichmentNormalizationPercentile = new ParameterSetter(
                "Enrichment Normalization Percentile", 0, 1, 1, "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        writeNormalizedGraphs = new CheckBox("Write Normalized graph files");

        //Pre-Prediction Part

        //TODO: Bind the max value of the reduction sliders to the other sliders so that they don't exceed them
        stepHeight = new ParameterSetter("Step Height",
                0, 1, 1,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        stepHeightReduction = new ParameterSetter("Step Height Reduction",
                0, 1, 1,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        stepFactor = new ParameterSetter("Step Factor",
                1, 5, 1,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        stepFactorReduction = new ParameterSetter("Step Factor Reduction",
                0, 2, 1,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");

        enrichmentFactor = new ParameterSetter("Enrichment Factor",
                0, 10, 1,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        processingSiteFactor = new ParameterSetter("Processing Site Factor",
                0, 10, 1,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        stepLength = new ParameterSetter("Step Length",
                0, 100, 0,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        baseHeight = new ParameterSetter("Base Height (disabled by default)",
                0, 1, 0,
                "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        baseHeight.setEnabled(false);


        //Post-prediction Part
        clusterMethod = new ComboBox<>("Clustering Method");
        clusterMethod.setItems(Globals.CLUSTER_METHOD_HIGHEST, Globals.CLUSTER_METHOD_FIRST);
        clusteringDistance = new ParameterSetter("TSS Clustering Distance", 0, 100, 0, "../VAADIN/themes/mytheme/css_resources/Dummy.svg");

        //TODO: Bind caption to mode
        crossDatasetShift = new ParameterSetter("Allowed Cross-Genome Shift", 0, 100, 0, "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        crossReplicateShift = new ParameterSetter("Allowed Cross-Replication Shift", 0, 100, 0, "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        matchingReplicates = new ComboBox<>("Matching Replicates");
        matchingReplicates.setItems(IntStream.rangeClosed(1, presenter.getNumberOfReplicates())
                .boxed().collect(Collectors.toList()));
        HorizontalLayout utrLengths = new HorizontalLayout();
        utrLength = new ParameterSetter("UTR length", 0, 1000, 0, "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        antisenseUtrLength = new ParameterSetter("Antisense UTR length", 0, 1000, 0, "../VAADIN/themes/mytheme/css_resources/Dummy.svg");
        utrLengths.addComponents(utrLength, antisenseUtrLength);

        //Layouts
        normalizationLayout = new VerticalLayout(
                new Label("<b>Normalization</b>", ContentMode.HTML),
                new InfoBar(Globals.NORMALIZATION_INFO),
                new HorizontalLayout(normalizationPercentile, enrichmentNormalizationPercentile),
                writeNormalizedGraphs);
        prePredictionLayout = new VerticalLayout(
                new Label("<b>Pre-prediction</b>", ContentMode.HTML),
                new InfoBar(Globals.PRE_PREDICTION_INFO),
                presetSelection,
                new HorizontalLayout(stepHeight, stepHeightReduction, stepFactor, stepFactorReduction),
                new HorizontalLayout(enrichmentFactor, processingSiteFactor, stepLength, baseHeight));
        postPredictionLayout = new VerticalLayout(
                new Label("<b>Post-Prediction</b>", ContentMode.HTML),
                new InfoBar(Globals.POST_PREDICTION_INFO),
                new HorizontalLayout(matchingReplicates, utrLengths),
                new HorizontalLayout(crossDatasetShift, crossReplicateShift),
                new HorizontalLayout(clusterMethod, clusteringDistance));

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
        return normalizationPercentile.slider;
    }

    public Slider getEnrichmentNormalizationPercentile() {
        return enrichmentNormalizationPercentile.slider;
    }

    public ComboBox<String> getClusterMethod() {
        return clusterMethod;
    }

    public Slider getClusteringDistance() {
        return clusteringDistance.slider;
    }

    public Slider getCrossDatasetShift() {
        return crossDatasetShift.slider;
    }

    public Slider getCrossReplicateShift() {
        return crossReplicateShift.slider;
    }

    public ComboBox<Integer> getMatchingReplicates() {
        return matchingReplicates;
    }

    public Slider getUtrLength() {
        return utrLength.slider;
    }

    public Slider getAntisenseUtrLength() {
        return antisenseUtrLength.slider;
    }

    public CheckBox getWriteNormalizedGraphs() {
        return writeNormalizedGraphs;
    }
}
