package view.firstImplementation;

import com.vaadin.ui.*;
import model.beans.AnnotationFileBean;
import model.beans.FastaFileBean;
import presenter.Presenter;
import view.MyGrid;

import java.util.LinkedList;
import java.util.List;

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
            FormLayout genomeData = new FormLayout();
            nameField = new TextField("Name");
            idField = new TextField("Alignment ID");
            fastaGrid = new MyGrid<>("Genome FASTA");
            fastaGrid.addColumn(FastaFileBean::getName).setCaption("File name");
            fastaGrid.addColumn(FastaFileBean::getCreationDate).setCaption("Creation Date");
            fastaGrid.addColumn(FastaFileBean::getSizeInKB).setCaption("Size (kB)");
            fastaGrid.addStyleName("my-file-grid");
            gffGrid = new MyGrid<>("Genome annotation (GFF)");
            gffGrid.addColumn(AnnotationFileBean::getName).setCaption("File name");
            gffGrid.addColumn(AnnotationFileBean::getCreationDate).setCaption("Creation Date");
            gffGrid.addColumn(AnnotationFileBean::getSizeInKB).setCaption("Size (kB)");
            gffGrid.addStyleName("my-file-grid");
            genomeData.addComponents(new HorizontalLayout(nameField, idField), fastaGrid, gffGrid);
            this.tab.addComponents(new InfoBar("TODO: Add GenomeTab info here!"),genomeData, new Label("RNA-seq graph files:"), replicatesSheet);

            //<-- DEBUG
           List<FastaFileBean> fastaFileBeanList = new LinkedList<>();
            for (int i = 0; i < 10; i++) {
                FastaFileBean bean = new FastaFileBean();
                bean.setName("Test Fasta " + i);
                bean.setCreationDate("01-01-01");
                bean.setSizeInKB(42);
                fastaFileBeanList.add(bean);
            }
            fastaGrid.setItems(fastaFileBeanList);
            List<AnnotationFileBean> annotationFileBeanList = new LinkedList<>();
            for (int i = 0; i < 10; i++) {
                AnnotationFileBean bean = new AnnotationFileBean();
                bean.setName("Test Annotation " + i);
                bean.setCreationDate("01-01-01");
                bean.setSizeInKB(42);
                annotationFileBeanList.add(bean);
            }
            gffGrid.setItems(annotationFileBeanList);
            //DEBUG -->

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
