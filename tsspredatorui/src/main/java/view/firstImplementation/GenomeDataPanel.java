package view.firstImplementation;

import com.vaadin.ui.*;
import model.beans.AnnotationFileBean;
import model.beans.FastaFileBean;
import presenter.Presenter;

public class GenomeDataPanel extends DataPanel {

    public GenomeDataPanel(Presenter presenter) {
        super(presenter);
        numberOfDatasetsBox.setCaption("Select number of Genomes");
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox, datasetAccordion);

    }

    public class GenomeTab extends DatasetTab {
        TextField nameField, idField;
        Grid<FastaFileBean> fastaGrid;
        Grid<AnnotationFileBean> gffGrid;

        public GenomeTab(int index) {
            super(index);
            HorizontalLayout genomeData = new HorizontalLayout();
            VerticalLayout nameAndId = new VerticalLayout();
            nameField = new TextField("Name");
            idField = new TextField("Alignment ID");
            nameAndId.addComponents(nameField, idField);
            HorizontalLayout fastaAndGff = new HorizontalLayout();
            fastaGrid = new Grid<>("Genome FASTA");
            fastaGrid.addColumn(FastaFileBean::getName).setCaption("File name");
            fastaGrid.addColumn(FastaFileBean::getCreationDate).setCaption("Creation Date");
            fastaGrid.addColumn(FastaFileBean::getSizeInKB).setCaption("Size in KB");
            gffGrid = new Grid<>("Genome annotation (GFF)");
            gffGrid.addColumn(AnnotationFileBean::getName).setCaption("File name");
            gffGrid.addColumn(AnnotationFileBean::getCreationDate).setCaption("Creation Date");
            gffGrid.addColumn(AnnotationFileBean::getSizeInKB).setCaption("Size in KB");
            fastaAndGff.addComponents(fastaGrid, gffGrid);
            genomeData.addComponents(nameAndId, fastaAndGff);
            this.tab.addComponents(genomeData, new Label("RNA-seq graph files:"), replicatesSheet);

        }

        public TextField getNameField() {
            return nameField;
        }

        public TextField getIdField() {
            return idField;
        }

        public Grid<FastaFileBean> getFastaGrid() {
            return fastaGrid;
        }

        public Grid<AnnotationFileBean> getGffGrid() {
            return gffGrid;
        }
    }

    public GenomeTab createGenomeTab(int index) {
        return new GenomeTab(index);
    }


}
