package view.firstImplementation;

import com.vaadin.ui.*;
import presenter.Presenter;

public class GenomeDataPanel extends DataPanel {

    public GenomeDataPanel(Presenter presenter) {
        super(presenter);
        numberOfDatasetsBox.setCaption("Select number of Genomes");
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox, setNumbers, datasetAccordion);

    }

    class GenomeTab extends DatasetTab {
        TextField nameField, idField, fasta, gff;

        public GenomeTab(int index) {
            super(index);
            HorizontalLayout genomeData = new HorizontalLayout();
            VerticalLayout nameAndId = new VerticalLayout();
            nameField = new TextField("Name");
            idField = new TextField("Alignment ID");
            nameAndId.addComponents(nameField, idField);
            VerticalLayout fastaAndGff = new VerticalLayout();
            fasta = new TextField("Genome FASTA");
            gff = new TextField("Genome annotation (GFF)");
            setupDatasetTabListeners(index, nameField, idField, fasta, gff);
            fastaAndGff.addComponents(fasta, gff);
            genomeData.addComponents(nameAndId, fastaAndGff);
            this.tab.addComponents(genomeData, new Label("RNA-seq graph files:"), replicatesSheet);

        }

        public TextField getNameField() {
            return nameField;
        }

        public TextField getIdField() {
            return idField;
        }

        public TextField getFasta() {
            return fasta;
        }

        public TextField getGff() {
            return gff;
        }
    }

    public GenomeTab createGenomeTab(int index){
        return new GenomeTab(index);
    }


    private void setupDatasetTabListeners(int index, TextField name, TextField id, TextField fasta, TextField gff) {
        name.addValueChangeListener(vce -> presenter.updateDatasetName(index, vce.getValue()));
        id.addValueChangeListener(vce -> presenter.updateGenomeAlignmentID(index, vce.getValue()));
        fasta.addValueChangeListener(vce -> presenter.updateGenomeFasta(index, vce.getValue()));
        gff.addValueChangeListener(vce -> presenter.updateGenomeAnnotation(index, vce.getValue()));

    }
}
