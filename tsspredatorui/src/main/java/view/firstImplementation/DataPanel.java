package view.firstImplementation;

import com.vaadin.ui.*;
import model.beans.GraphFileBean;
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
public abstract class DataPanel extends CustomComponent {
    Presenter presenter;
    Panel dataPanel;
    Layout contentLayout;
    Accordion datasetAccordion;
    ComboBox<Integer> numberOfDatasetsBox;
    ComboBox<Integer> numberOfReplicatesBox;
    //Button setNumbers;

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

//        setNumbers = new Button("Set selection", e -> {
//            int oldDatasetCount = presenter.getNumberOfDatasets();
//            int oldReplicateCount = presenter.getNumberOfReplicates();
//            updateAccordion(oldDatasetCount, oldReplicateCount);
//
//        });


        datasetAccordion = new Accordion();
        datasetAccordion.setWidth("100%");

        panel.setContent(contentLayout);
        return panel;
    }

    /**
     * The accordion is initialized with one dataset (genome/condition) and one replicate
     */
    public void initAccordion(){
        Component initialTab = this instanceof GenomeDataPanel
                ? ((GenomeDataPanel) this).createGenomeTab(0)
                : ((ConditionDataPanel) this).createConditionTab(0);
        //Tell presenter to set up bindings
        datasetAccordion.addTab(initialTab, "Genome " + 1);
        presenter.initDatasetBindings(0);
        presenter.initReplicateBindings(0,0);
    }


    /**
     * The genomes/conditions are organized as tabs in an Accordion. When the user changes
     * the number of genomes/conditions or replicates, this Accordion has to be updated,
     * which happens here
     */
    public void updateAccordion(int oldDatasetCount, int oldReplicateCount) {
        //Adjust number of replicate tabs for each dataset tab
        int replicateDelta = presenter.getNumberOfReplicates() - oldReplicateCount;
        for (int datasetIndex = 0; datasetIndex < oldDatasetCount; datasetIndex++) {
            datasetAccordion.getTab(datasetIndex);
            TabSheet.Tab tab = datasetAccordion.getTab(datasetIndex);
            //Access the replicate sheet of the current tab
            //It's the last of its three components, so its index is 2
            TabSheet currentReplicateSheet = ((DatasetTab) tab.getComponent()).replicatesSheet;
            if (replicateDelta > 0) {
                //Add new replicate tabs
                for (int replicateIndex = oldReplicateCount;
                     replicateIndex < presenter.getNumberOfReplicates();
                     replicateIndex++) {
                    Component replicateTab = new ReplicateTab(datasetIndex, replicateIndex);
                    currentReplicateSheet.addTab(replicateTab, "Replicate " + createReplicateID(replicateIndex));
                    presenter.initReplicateBindings(datasetIndex, replicateIndex);
                }
            } else {
                //Remove excess replicate tabs
                for (int replicateIndex = oldReplicateCount; replicateIndex > presenter.getNumberOfReplicates(); replicateIndex--) {
                    currentReplicateSheet.removeTab(currentReplicateSheet.getTab(replicateIndex - 1));
                }
            }
        }


        int datasetDelta = presenter.getNumberOfDatasets() - oldDatasetCount;
        if (datasetDelta > 0) {
            //Add new dataset tabs
            for (int datasetIndex = oldDatasetCount; datasetIndex < presenter.getNumberOfDatasets(); datasetIndex++) {
                Component currentTab = this instanceof GenomeDataPanel
                        ? ((GenomeDataPanel) this).createGenomeTab(datasetIndex)
                        : ((ConditionDataPanel) this).createConditionTab(datasetIndex);
                //Tell presenter to set up bindings
                datasetAccordion.addTab(currentTab, "Genome " + (datasetIndex + 1));
                presenter.initDatasetBindings(datasetIndex);
                for (int replicateIndex = 0;
                     replicateIndex < presenter.getNumberOfReplicates();
                     replicateIndex++) {
                    presenter.initReplicateBindings(datasetIndex, replicateIndex);
                }
            }
        } else {
            //Remove excess dataset tabs
            for (int i = oldDatasetCount; i > presenter.getNumberOfDatasets(); i--) {
                datasetAccordion.removeTab(datasetAccordion.getTab(i - 1));
            }
        }

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
            for (int replicateIndex = 0;
                 replicateIndex < presenter.getNumberOfReplicates();
                 replicateIndex++) {
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
        VerticalLayout layout;
        TextField enrichedCoding, enrichedTemplate, normalCoding, normalTemplate;
        Grid<GraphFileBean> graphFileGrid;

        public ReplicateTab(int datasetIndex, int replicateIndex) {
            layout = new VerticalLayout();

            VerticalLayout enrichedPart = new VerticalLayout();
            enrichedCoding = new TextField("Enriched Coding Strand");
            enrichedTemplate = new TextField("Enriched Template Strand");
            enrichedPart.addComponents(enrichedCoding, enrichedTemplate);
            VerticalLayout normalPart = new VerticalLayout();
            normalCoding = new TextField("Normal Coding Strand");
            normalTemplate = new TextField("Normal Template Strand");
            normalPart.addComponents(normalCoding, normalTemplate);

            graphFileGrid = new Grid<>("Graph Files");
            graphFileGrid.addColumn(GraphFileBean::getName).setCaption("File name");
            graphFileGrid.addColumn(GraphFileBean::getCreationDate).setCaption("Creation Date");
            graphFileGrid.addColumn(GraphFileBean::getSizeInKB).setCaption("Size in KB");

            presenter.updateReplicateID(datasetIndex, replicateIndex, createReplicateID(replicateIndex));
            layout.addComponents(new HorizontalLayout(enrichedPart, normalPart), graphFileGrid);
            setCompositionRoot(layout);

        }

        public TextField getEnrichedCoding() {
            return enrichedCoding;
        }

        public TextField getEnrichedTemplate() {
            return enrichedTemplate;
        }

        public TextField getNormalCoding() {
            return normalCoding;
        }

        public TextField getNormalTemplate() {
            return normalTemplate;
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

    public ComboBox<Integer> getNumberOfReplicatesBox() {
        return numberOfReplicatesBox;
    }

    public TextField getGenomeNameField(int index) {

        return null;
    }

    public Accordion getDatasetAccordion() {
        return datasetAccordion;
    }

    public DatasetTab getDatasetTab(int index) {
        return (DatasetTab) datasetAccordion.getTab(index).getComponent();
    }

}
