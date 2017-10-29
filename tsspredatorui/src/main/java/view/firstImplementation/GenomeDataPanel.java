package view.firstImplementation;

import com.vaadin.ui.*;
import presenter.Presenter;

public class GenomeDataPanel extends DataPanel {

    public GenomeDataPanel(Presenter presenter) {
        super(presenter);
        numberOfDatasetsBox.setCaption("Select number of Genomes");
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox, setNumbers, datasetAccordion);

    }

    /**
     * Helper method for updateAccordion()
     *
     * @return
     */
    @Override
    Component createAccordionTab(int index) {
        VerticalLayout tab = new VerticalLayout();
        HorizontalLayout genomeData = new HorizontalLayout();
        VerticalLayout nameAndId = new VerticalLayout();
        TextField name = new TextField("Name");
        TextField id = new TextField("Alignment ID");
        nameAndId.addComponents(name, id);
        VerticalLayout fastaAndGff = new VerticalLayout();
        TextField fasta = new TextField("Genome FASTA");
        TextField gff = new TextField("Genome annotation (GFF)");
        setupDatasetTabListeners(index, name, id, fasta, gff);
        fastaAndGff.addComponents(fasta, gff);
        genomeData.addComponents(nameAndId, fastaAndGff);

        TabSheet replicatesSheet = new TabSheet();
        for (int replicateIndex = 0; replicateIndex < numberOfReplicates; replicateIndex++) {
            HorizontalLayout replicateTab = createReplicateTab(index, replicateIndex);
            replicatesSheet.addTab(replicateTab, "Replicate " + (char) (97 + replicateIndex));
        }
        tab.addComponents(genomeData, new Label("RNA-seq graph files:"), replicatesSheet);
        return tab;

    }

    private void setupDatasetTabListeners(int index, TextField name, TextField id, TextField fasta, TextField gff) {
        name.addValueChangeListener(vce -> presenter.updateGenomeName(index, vce.getValue()));
        id.addValueChangeListener(vce -> presenter.updateGenomeAlignmentID(index, vce.getValue()));
        fasta.addValueChangeListener(vce -> presenter.updateGenomeFasta(index, vce.getValue()));
        gff.addValueChangeListener(vce -> presenter.updateGenomeAnnotation(index, vce.getValue()));

    }
}
