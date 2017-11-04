package view.firstImplementation;

import com.vaadin.ui.*;
import presenter.Presenter;

public class GenomeDataPanel extends DataPanel {

    public GenomeDataPanel(Presenter presenter) {
        super(presenter);
        numberOfDatasetsBox.setCaption("Select number of Genomes");
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox, setNumbers, datasetAccordion);

    }

    public class GenomeTab extends DatasetTab {
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

        public TextField getFastaField() {
            return fasta;
        }

        public TextField getGffField() {
            return gff;
        }
    }

    public GenomeTab createGenomeTab(int index) {
        return new GenomeTab(index);
    }


}
