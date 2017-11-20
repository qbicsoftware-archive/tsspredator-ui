package view.firstImplementation;

import com.vaadin.ui.*;
import model.beans.AnnotationFileBean;
import model.beans.FastaFileBean;
import presenter.Presenter;
import view.MyGrid;

import java.util.LinkedList;
import java.util.List;

public class ConditionDataPanel extends DataPanel {
    Grid<FastaFileBean> fastaGrid;
    Grid<AnnotationFileBean> gffGrid;


    public ConditionDataPanel(Presenter presenter) {
        super(presenter);
        numberOfDatasetsBox.setCaption("Select number of Conditions");
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
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox,
                fastaGrid, gffGrid, datasetAccordion);

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

    public class ConditionTab extends DatasetTab {
        TextField nameField;

        public ConditionTab(int index) {
            super(index);
            nameField = new TextField("Name");
            this.tab.addComponents(nameField, new Label("RNA-seq graph files:"), replicatesSheet);


        }

        public TextField getNameField() {
            return nameField;
        }
    }

    public ConditionTab createConditionTab(int index) {
        ConditionTab tab = new ConditionTab(index);
        return tab;
    }

    public Grid<FastaFileBean> getFastaGrid() {
        return fastaGrid;
    }

    public Grid<AnnotationFileBean> getGffGrid() {
        return gffGrid;
    }
}
