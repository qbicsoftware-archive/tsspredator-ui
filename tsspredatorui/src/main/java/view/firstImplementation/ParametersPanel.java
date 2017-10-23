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
    private Layout contentLayout, buttonLayout, parameterLayout1, parameterLayout2, parameterLayout3, parameterLayout4;
    private LinkedList<Layout> layoutList;
    private int layoutIndex;
    Slider stepHeight, stepHeightReduction;
    Slider stepFactor, stepFactorReduction;
    Slider enrichmentFactor, processingSiteFactor;
    Slider stepLength, baseHeight;
    Slider normalizationPercentile, enrichedNormalizationPercentile;
    ComboBox<String> clusterMethod;
    Slider clusteringDistance;
    Slider crossDatasetShift, crossReplicateShift;
    ComboBox<Integer> matchingReplicates;
    Slider utrLength, antisenseUtrLength;
    CheckBox writeGraphs;


    public ParametersPanel(Presenter presenter) {
        parametersPanel = designPanel();
        setCompositionRoot(parametersPanel);
        this.presenter = presenter;
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new VerticalLayout();


        createParameterLayouts();
        createButtonLayout();
        for (Layout layout : layoutList) {
            contentLayout.addComponents(layout);
        }
        contentLayout.addComponents(buttonLayout);
        panel.setContent(contentLayout);
        return panel;
    }

    private void createParameterLayouts() {
        layoutIndex = 0;
        layoutList = new LinkedList<>();

        parameterLayout1 = new VerticalLayout();
        //TODO: Bind the max value of the reduction sliders to the other sliders so that they don't exceed them
        HorizontalLayout heightParams = new HorizontalLayout();
        stepHeight = new Slider("Step Height");
        stepHeight.setMin(0);
        stepHeight.setMax(1);
        stepHeight.setResolution(1);
        stepHeightReduction = new Slider("Step Height Reduction");
        stepHeightReduction.setMin(0);
        stepHeightReduction.setMax(1);
        stepHeightReduction.setResolution(1);
        heightParams.addComponents(stepHeight, stepHeightReduction);
        HorizontalLayout factorParams = new HorizontalLayout();
        stepFactor = new Slider("Step Factor");
        stepFactor.setMin(1);
        stepFactor.setMax(2);
        stepFactor.setResolution(1);
        stepFactorReduction = new Slider("Step Factor Reduction");
        stepFactorReduction.setMin(0);
        stepFactorReduction.setMax(2);
        stepFactorReduction.setResolution(1);
        factorParams.addComponents(stepFactor, stepFactorReduction);
        parameterLayout1.addComponents(heightParams, factorParams);
        layoutList.add(parameterLayout1);

        parameterLayout2 = new VerticalLayout();
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
        parameterLayout2.addComponents(enrichmentFactor, processingSiteFactor, stepLength, baseHeight);
        layoutList.add(parameterLayout2);

        parameterLayout3 = new VerticalLayout();
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
        parameterLayout3.addComponents(percentiles, methodAndDistance);
        layoutList.add(parameterLayout3);

        parameterLayout4 = new VerticalLayout();
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
        utrLength.setMax(100);
        antisenseUtrLength = new Slider("Antisense UTR length");
        antisenseUtrLength.setMin(0);
        antisenseUtrLength.setMax(100);
        utrLengths.addComponents(utrLength, antisenseUtrLength);
        writeGraphs = new CheckBox("Write RNA-Seq graphs");
        parameterLayout4.addComponents(allowedShifts, matchingReplicates, utrLength, writeGraphs);
        layoutList.add(parameterLayout4);


        //Set all but the first layout invisible
        for (Layout layout : layoutList) {
            layout.setVisible(false);
        }
        layoutList.getFirst().setVisible(true);

        setupListeners();
    }


    /**
     * This method enables navigation between the parameter layouts via two buttons at the bottom
     */
    private void createButtonLayout() {
        buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        Button previousButton = new Button("Previous");
        previousButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        previousButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        previousButton.setIcon(VaadinIcons.ARROW_CIRCLE_LEFT_O);
        Button nextButton = new Button("Next");
        nextButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        nextButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        nextButton.setIcon(VaadinIcons.ARROW_CIRCLE_RIGHT_O);

        previousButton.setEnabled(false);

        previousButton.addClickListener(event -> {
            layoutList.get(layoutIndex).setVisible(false);
            if (layoutIndex == layoutList.size() - 1)
                nextButton.setEnabled(true);
            layoutIndex--;
            layoutList.get(layoutIndex).setVisible(true);
            if (layoutIndex == 0)
                previousButton.setEnabled(false);
        });


        nextButton.addClickListener(event -> {
            layoutList.get(layoutIndex).setVisible(false);
            if (layoutIndex == 0)
                previousButton.setEnabled(true);
            layoutIndex++;
            layoutList.get(layoutIndex).setVisible(true);
            if (layoutIndex == layoutList.size() - 1)
                nextButton.setEnabled(false);

        });

        buttonLayout.addComponents(previousButton, nextButton);

    }

    private void setupListeners() {
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
}
