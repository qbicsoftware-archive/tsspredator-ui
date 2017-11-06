package view.firstImplementation;

import com.vaadin.ui.*;
import model.Genome;
import model.Replicate;
import presenter.Presenter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * In this abstract class, the parts of the DataPanel which are common to both
 * the genome and the condition variant of the workflow are stored.
 * The variable parts are in classes GenomeDataPanel or ConditionDataPanel, respectively.
 *
 * @author jmueller
 */
public abstract class DataPanel extends CustomComponent{
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
                    Component replicateTab = new ReplicateTab(datasetIndex, replicateIndex);
                    ((TabSheet) currentReplicateSheet).addTab(replicateTab, "Replicate " + createReplicateID(replicateIndex));
                    presenter.initReplicateBindings(datasetIndex, replicateIndex);
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
                Component currentTab = this instanceof GenomeDataPanel
                        ? ((GenomeDataPanel) this).createGenomeTab(datasetIndex)
                        : ((ConditionDataPanel) this).createConditionTab(datasetIndex);
                //Tell presenter to set up bindings
                datasetAccordion.addTab(currentTab, "Genome " + (datasetIndex + 1));
                presenter.initDatasetBindings(datasetIndex);
                for (int replicateIndex = 0; replicateIndex < numberOfReplicates; replicateIndex++) {
                    presenter.initReplicateBindings(datasetIndex, replicateIndex);
                }
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
     * This class is extended by GenomeTab in GenomeDataPanel and ConditionTab in ConditionDataPanel.
     */
    public abstract class DatasetTab extends CustomComponent {
        VerticalLayout tab;
        TabSheet replicatesSheet;

        public DatasetTab(int index) {
            tab = new VerticalLayout();
            replicatesSheet = new TabSheet();
            for (int replicateIndex = 0; replicateIndex < numberOfReplicates; replicateIndex++) {
                ReplicateTab replicateTab = new ReplicateTab(index, replicateIndex);
                replicatesSheet.addTab(replicateTab, "Replicate " + createReplicateID(replicateIndex));
            }
            setCompositionRoot(tab);
        }

        public ReplicateTab getReplicateTab(int index) {
            return (ReplicateTab) replicatesSheet.getTab(index).getComponent();
        }

    }

    /**
     * This inner class represents a replicate tab in the dataset accordion.
     * It works for both the genome and the condition variants.
     */
    public class ReplicateTab extends CustomComponent {
        HorizontalLayout layout;
        TextField eplus, eminus, nplus, nminus;

        public ReplicateTab(int datasetIndex, int replicateIndex) {
            layout = new HorizontalLayout();
            VerticalLayout enrichedPart = new VerticalLayout();
            eplus = new TextField("Enriched Plus");
            eminus = new TextField("Enriched Minus");
            enrichedPart.addComponents(eplus, eminus);
            VerticalLayout normalPart = new VerticalLayout();
            nplus = new TextField("Normal Plus");
            nminus = new TextField("Normal Minus");
            normalPart.addComponents(nplus, nminus);
            presenter.updateReplicateID(datasetIndex, replicateIndex, createReplicateID(replicateIndex));
            layout.addComponents(enrichedPart, normalPart);
            setCompositionRoot(layout);

        }

        public TextField getEplus() {
            return eplus;
        }

        public TextField getEminus() {
            return eminus;
        }

        public TextField getNplus() {
            return nplus;
        }

        public TextField getNminus() {
            return nminus;
        }
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

    public ComboBox<Integer> getNumberOfDatasetsBox() {
        return numberOfDatasetsBox;
    }

    public void setNumberOfDatasetsBox(ComboBox<Integer> numberOfDatasetsBox) {
        this.numberOfDatasetsBox = numberOfDatasetsBox;
    }

    public ComboBox<Integer> getNumberOfReplicatesBox() {
        return numberOfReplicatesBox;
    }

    public void setNumberOfReplicatesBox(ComboBox<Integer> numberOfReplicatesBox) {
        this.numberOfReplicatesBox = numberOfReplicatesBox;
    }

    public TextField getGenomeNameField(int index) {

        return null;
    }

    public DatasetTab getDatasetTab(int index) {
        return (DatasetTab) datasetAccordion.getTab(index).getComponent();
    }
}
