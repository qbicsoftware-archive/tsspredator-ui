package view.firstImplementation;

import com.vaadin.ui.*;
import presenter.Presenter;
import view.DataView;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This component has a Panel where the user first chooses the number of replicates,
 * then chooses fasta and gff files to use, as well as the RNA-seq data.
 *
 * @author jmueller
 */
public class DataPanel extends CustomComponent implements DataView {
    private Presenter presenter;
    private Panel dataPanel;
    //TODO: isConditions must be stored globally - it depends on the user's choice in the GenConfigWindow
    private Boolean isConditions = false;
    private Layout contentLayout;
    int numberOfDatasets, numberOfReplicates;
    Accordion datasetAccordion;
    ComboBox<Integer> numberOfDatasetsBox;
    ComboBox<Integer> numberOfReplicatesBox;

    public DataPanel(Presenter presenter) {
        dataPanel = designPanel();
        setCompositionRoot(dataPanel);
        this.presenter = presenter;
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new FormLayout();
        isConditions = false;
        numberOfDatasetsBox = new ComboBox<>("Select number of " + (isConditions ? "Conditions" : "Genomes"));
        numberOfReplicatesBox = new ComboBox<>("Select number of Replicates");

        //TODO: Is this the most elegant way to do this?
        Collection<Integer> possibleGenomesOrConditions = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            possibleGenomesOrConditions.add(i + 1);
        }
        numberOfDatasetsBox.setItems(possibleGenomesOrConditions);
        Collection<Integer> possibleReplicates = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            possibleReplicates.add(i + 1);
        }
        numberOfReplicatesBox.setItems(possibleReplicates);
        Button setNumbers = new Button("Set selection", e -> {
            int oldDatasetCount = numberOfDatasets;
            int oldReplicateCount = numberOfReplicates;
            numberOfDatasets = numberOfDatasetsBox.getValue();
            numberOfReplicates = numberOfReplicatesBox.getValue();
            updateAccordion(oldDatasetCount, oldReplicateCount);

        });


        datasetAccordion = new Accordion();
        datasetAccordion.setWidth("100%");
        datasetAccordion.setVisible(false);
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox, setNumbers, datasetAccordion);

        setupListeners();
        panel.setContent(contentLayout);
        return panel;
    }


    /**
     * The genomes/conditions are organized as tabs in an Accordion. When the user changes
     * the number of genomes/conditions or replicates, this Accordion has to be updated,
     * which happens here
     */
    private void updateAccordion(int oldDatasetCount, int oldReplicateCount) {
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
                    ((TabSheet) currentReplicateSheet).addTab(replicateTab, "Replicate " + (char) (97 + replicateIndex));
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
                datasetAccordion.addTab(currentTab, isConditions ? "Condition " : "Genome " + (datasetIndex + 1));
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

    private Component createAccordionTab(int index) {
        VerticalLayout tab = new VerticalLayout();
        HorizontalLayout genomeOrConditionData = new HorizontalLayout();
        VerticalLayout nameAndId = new VerticalLayout();
        TextField name = new TextField("Name");
        TextField id = new TextField("Alignment ID");
        nameAndId.addComponents(name, id);
        VerticalLayout fastaAndGff = new VerticalLayout();
        TextField fasta = new TextField("Genome FASTA");
        TextField gff = new TextField("Genome annotation (GFF)");
        setupDatasetTabListeners(index, name, id, fasta, gff);
        fastaAndGff.addComponents(fasta, gff);
        genomeOrConditionData.addComponents(nameAndId, fastaAndGff);

        TabSheet replicatesSheet = new TabSheet();
        for (int replicateIndex = 0; replicateIndex < numberOfReplicates; replicateIndex++) {
            HorizontalLayout replicateTab = createReplicateTab(index, replicateIndex);
            replicatesSheet.addTab(replicateTab, "Replicate " + (char) (97 + replicateIndex));
        }
        tab.addComponents(genomeOrConditionData, new Label("RNA-seq graph files:"), replicatesSheet);
        return tab;

    }

    /**
     * Helper method for createAccordionTab()
     *
     * @param datasetIndex
     * @param replicateIndex
     * @return
     */
    private HorizontalLayout createReplicateTab(int datasetIndex, int replicateIndex) {
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
        presenter.updateReplicateID(datasetIndex, replicateIndex, String.valueOf((char) (97 + replicateIndex)));
        replicateTab.addComponents(enrichedPart, normalPart);
        return replicateTab;
    }

    private void setupListeners() {
        numberOfDatasetsBox.addValueChangeListener(vce -> presenter.updateNumberOfDatasets(vce.getValue()));
        numberOfReplicatesBox.addValueChangeListener(vce -> presenter.updateNumberOfReplicates(vce.getValue()));
    }


    private void setupDatasetTabListeners(int index, TextField name, TextField id, TextField fasta, TextField gff) {
        name.addValueChangeListener(vce -> presenter.updateGenomeName(index, vce.getValue()));
        id.addValueChangeListener(vce -> presenter.updateGenomeAlignmentID(index, vce.getValue()));
        fasta.addValueChangeListener(vce -> presenter.updateGenomeFasta(index, vce.getValue()));
        gff.addValueChangeListener(vce -> presenter.updateGenomeAnnotation(index, vce.getValue()));

    }

    private void setupReplicateTabListeners(int datasetIndex, int replicateIndex,
                                            TextField eplus, TextField eminus, TextField nplus, TextField nminus) {
        eplus.addValueChangeListener(vce -> presenter.updateEnrichedPlus(datasetIndex, replicateIndex, eplus.getValue()));
        eminus.addValueChangeListener(vce -> presenter.updateEnrichedMinus(datasetIndex, replicateIndex, eminus.getValue()));
        nplus.addValueChangeListener(vce -> presenter.updateNormalPlus(datasetIndex, replicateIndex, nplus.getValue()));
        nminus.addValueChangeListener(vce -> presenter.updateNormalMinus(datasetIndex, replicateIndex, nminus.getValue()));

    }
}
