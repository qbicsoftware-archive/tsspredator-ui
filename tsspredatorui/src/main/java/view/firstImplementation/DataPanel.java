package view.firstImplementation;

import com.vaadin.ui.*;
import presenter.Presenter;
import view.DataView;

import java.util.Collection;
import java.util.LinkedList;

/**
 * In this abstract class, the parts of the DataPanel which are common to both
 * the genome and the condition variant of the workflow are stored.
 * The variable parts are in classes GenomeDataPanel or ConditionDataPanel, respectively.
 *
 * @author jmueller
 */
public abstract class DataPanel extends CustomComponent implements DataView {
    Presenter presenter;
    Panel dataPanel;
    Layout contentLayout;
    int numberOfDatasets, numberOfReplicates;
    Accordion datasetAccordion;
    ComboBox<Integer> numberOfDatasetsBox;
    ComboBox<Integer> numberOfReplicatesBox;
    Button setNumbers;

    public DataPanel(Presenter presenter) {
        dataPanel = designPanel();
        setCompositionRoot(dataPanel);
        this.presenter = presenter;
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new FormLayout();
        numberOfDatasetsBox = new ComboBox<>();
        numberOfReplicatesBox = new ComboBox<>("Select number of Replicates");


        //TODO: Is this the most elegant way to do this?
        Collection<Integer> possibleGenomesOrConditions = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            possibleGenomesOrConditions.add(i + 1);
        }
        numberOfDatasetsBox.setItems(possibleGenomesOrConditions);

        Collection<Integer> possibleReplicates = new LinkedList<>();
        //TODO: Extend range to 100 using aa, ab, ac, ...
        for (int i = 0; i < 100; i++) {
            possibleReplicates.add(i + 1);
        }
        numberOfReplicatesBox.setItems(possibleReplicates);
        setNumbers = new Button("Set selection", e -> {
            int oldDatasetCount = numberOfDatasets;
            int oldReplicateCount = numberOfReplicates;
            numberOfDatasets = numberOfDatasetsBox.getValue();
            numberOfReplicates = numberOfReplicatesBox.getValue();
            updateAccordion(oldDatasetCount, oldReplicateCount);

        });


        datasetAccordion = new Accordion();
        datasetAccordion.setWidth("100%");
        datasetAccordion.setVisible(false);

        setupListeners();
        panel.setContent(contentLayout);
        return panel;
    }


    /**
     * The genomes/conditions are organized as tabs in an Accordion. When the user changes
     * the number of genomes/conditions or replicates, this Accordion has to be updated,
     * which happens here
     */
    void updateAccordion(int oldDatasetCount, int oldReplicateCount) {
        //Adjust number of replicate tabs for each dataset tab
        int replicateDelta = numberOfReplicates - oldReplicateCount;
        for (int datasetIndex = 0; datasetIndex < oldDatasetCount; datasetIndex++) {
            datasetAccordion.getTab(datasetIndex);
            TabSheet.Tab tab = datasetAccordion.getTab(datasetIndex);
            //Access the replicate sheet of the current tab
            //It's the last of its three components, so its index is 2
            Component currentReplicateSheet = ((VerticalLayout) tab.getComponent()).getComponent(2);
            if (replicateDelta > 0) {
                //Add new replicate tabs
                for (int replicateIndex = oldReplicateCount; replicateIndex < numberOfReplicates; replicateIndex++) {
                    HorizontalLayout replicateTab = createReplicateTab(datasetIndex, replicateIndex);
                    ((TabSheet) currentReplicateSheet).addTab(replicateTab, "Replicate " + createReplicateID(replicateIndex));
                }
            } else {
                //Remove excess replicate tabs
                for (int replicateIndex = oldReplicateCount; replicateIndex > numberOfReplicates; replicateIndex--) {
                    ((TabSheet) currentReplicateSheet).removeTab(((TabSheet) currentReplicateSheet).getTab(replicateIndex - 1));
                }
            }
        }


        int datasetDelta = numberOfDatasets - oldDatasetCount;
        if (datasetDelta > 0) {
            //Add new dataset tabs
            for (int datasetIndex = oldDatasetCount; datasetIndex < numberOfDatasets; datasetIndex++) {
                Component currentTab = createAccordionTab(datasetIndex);
                datasetAccordion.addTab(currentTab, "Genome " + (datasetIndex + 1));
            }
        } else {
            //Remove excess dataset tabs
            for (int i = oldDatasetCount; i > numberOfDatasets; i--) {
                datasetAccordion.removeTab(datasetAccordion.getTab(i - 1));
            }
        }
        datasetAccordion.setVisible(true);

    }


    /**
     * Helper method for updateAccordion()
     *
     * @return
     */
    abstract Component createAccordionTab(int index);


    /**
     * Helper method for createAccordionTab()
     *
     * @param datasetIndex
     * @param replicateIndex
     * @return
     */
    HorizontalLayout createReplicateTab(int datasetIndex, int replicateIndex) {
        HorizontalLayout replicateTab = new HorizontalLayout();
        VerticalLayout enrichedPart = new VerticalLayout();
        TextField eplus = new TextField("Enriched Plus");
        TextField eminus = new TextField("Enriched Minus");
        enrichedPart.addComponents(eplus, eminus);
        VerticalLayout normalPart = new VerticalLayout();
        TextField nplus = new TextField("Normal Plus");
        TextField nminus = new TextField("Normal Minus");
        normalPart.addComponents(nplus, nminus);
        setupReplicateTabListeners(datasetIndex, replicateIndex, eplus, eminus, nplus, nminus);
        presenter.updateReplicateID(datasetIndex, replicateIndex, createReplicateID(replicateIndex));
        replicateTab.addComponents(enrichedPart, normalPart);
        return replicateTab;
    }

    /**
     * Converts the numerical replicateIndex to an 'abc-value' as follows:
     * 0 -> a
     * 1 -> b
     * ...
     * 25 -> z
     * 26 -> aa
     * 27 -> ab
     * ...
     */
    String createReplicateID(int replicateIndex) {
        StringBuilder builder = new StringBuilder();
        while (replicateIndex >= 26) {
            builder.append((char) (97 + (replicateIndex % 26)));
            replicateIndex /= 26;
            replicateIndex--;
        }
        builder.append((char) (97 + replicateIndex));
        return builder.reverse().toString();

    }

    void setupListeners() {
        numberOfDatasetsBox.addValueChangeListener(vce -> presenter.updateNumberOfDatasets(vce.getValue()));
        numberOfReplicatesBox.addValueChangeListener(vce -> presenter.updateNumberOfReplicates(vce.getValue()));
    }

    void setupReplicateTabListeners(int datasetIndex, int replicateIndex,
                                    TextField eplus, TextField eminus, TextField nplus, TextField nminus) {
        eplus.addValueChangeListener(vce -> presenter.updateEnrichedPlus(datasetIndex, replicateIndex, eplus.getValue()));
        eminus.addValueChangeListener(vce -> presenter.updateEnrichedMinus(datasetIndex, replicateIndex, eminus.getValue()));
        nplus.addValueChangeListener(vce -> presenter.updateNormalPlus(datasetIndex, replicateIndex, nplus.getValue()));
        nminus.addValueChangeListener(vce -> presenter.updateNormalMinus(datasetIndex, replicateIndex, nminus.getValue()));

    }
}
