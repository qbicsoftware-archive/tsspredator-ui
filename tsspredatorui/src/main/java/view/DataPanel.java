package view;

import com.vaadin.ui.*;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This component has a Panel where the user first chooses the number of replicates,
 * then chooses fasta and gff files to use, as well as the RNA-seq data.
 * @author jmueller
 */
public class DataPanel extends CustomComponent {

    private Panel dataPanel;
    //TODO: isConditions must be stored globally - it depends on the user's choice in the GenConfigWindow
    private Boolean isConditions = false;
    private Layout contentLayout;
    int numOfGenomesOrConditions, numOfReplicates;
    Accordion genomeOrConditionAccordion;

    public DataPanel() {
        dataPanel = designPanel();
        setCompositionRoot(dataPanel);
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new FormLayout();
        isConditions = false;
        ComboBox<Integer> numOfGenomesOrConditionsBox = new ComboBox<>("Select number of " + (isConditions ? "Conditions" : "Genomes"));
        ComboBox<Integer> numOfReplicatesBox = new ComboBox<>("Select number of Replicates");

        //TODO: Is this the most elegant way to do this?
        Collection<Integer> possibleGenomesOrConditions = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            possibleGenomesOrConditions.add(i + 1);
        }
        numOfGenomesOrConditionsBox.setItems(possibleGenomesOrConditions);
        Collection<Integer> possibleReplicates = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            possibleReplicates.add(i + 1);
        }
        numOfReplicatesBox.setItems(possibleReplicates);
        Button setNumbers = new Button("Set selection", e -> {
            //TODO: This is destructive right now, existing data should be kept!
            numOfGenomesOrConditions = numOfGenomesOrConditionsBox.getValue();
            numOfReplicates = numOfReplicatesBox.getValue();
            updateAccordion();

        });


        genomeOrConditionAccordion = new Accordion();
        genomeOrConditionAccordion.setWidth("100%");
        genomeOrConditionAccordion.setVisible(false);
        contentLayout.addComponents(numOfGenomesOrConditionsBox, numOfReplicatesBox, setNumbers, genomeOrConditionAccordion);


        panel.setContent(contentLayout);
        return panel;
    }

    /**
     * The genomes/conditions are organized as tabs in an Accordion. When the user changes
     * the number of genomes/conditions or replicates, this Accordion has to be updated,
     * which happens here
     */
    private void updateAccordion() {
        genomeOrConditionAccordion.removeAllComponents();

        for (int i = 0; i < numOfGenomesOrConditions; i++) {
            Component currentTab = createAccordionTab();
            genomeOrConditionAccordion.addTab(currentTab, isConditions ? "Condition" : "Genome" + (i + 1));
        }

        genomeOrConditionAccordion.setVisible(true);

    }

    /**
     * Helper method for updateAccordion()
     * @return
     */
    private Component createAccordionTab() {
        VerticalLayout tab = new VerticalLayout();
        HorizontalLayout genomeOrConditionData = new HorizontalLayout();
        VerticalLayout nameAndId = new VerticalLayout();
        TextField name = new TextField("Name");
        TextField id = new TextField("Alignment ID");
        nameAndId.addComponents(name, id);
        VerticalLayout fastaAndGff = new VerticalLayout();
        Upload fasta = new Upload("Genome FASTA", (Upload.Receiver) (s, s1) -> null);
        Upload gff = new Upload("Genome annotation (GFF)", (Upload.Receiver) (s, s1) -> null);
        fastaAndGff.addComponents(fasta, gff);
        genomeOrConditionData.addComponents(nameAndId, fastaAndGff);

        TabSheet replicatesSheet = new TabSheet();
        for (int replicateIndex = 0; replicateIndex < numOfReplicates; replicateIndex++) {
            HorizontalLayout replicateTab = createReplicateTab();
            replicatesSheet.addTab(replicateTab, "Replicate " + (char) (97 + replicateIndex));
        }
        tab.addComponents(genomeOrConditionData, new Label("RNA-seq graph files:"), replicatesSheet);
        return tab;

    }

    /**
     * Helper method for createAccordionTab()
     * @return
     */
    private HorizontalLayout createReplicateTab() {
        HorizontalLayout replicateTab = new HorizontalLayout();
        VerticalLayout enrichedPart = new VerticalLayout();
        Upload eplus = new Upload("Enriched Plus", (Upload.Receiver) (s, s1) -> null);
        Upload eminus = new Upload("Enriched Minus", (Upload.Receiver) (s, s1) -> null);
        enrichedPart.addComponents(eplus, eminus);
        VerticalLayout normalPart = new VerticalLayout();
        Upload nplus = new Upload("Normal Plus", (Upload.Receiver) (s, s1) -> null);
        Upload nminus = new Upload("Normal Minus", (Upload.Receiver) (s, s1) -> null);
        normalPart.addComponents(nplus, nminus);
        replicateTab.addComponents(enrichedPart, normalPart);
        return replicateTab;
    }
}
