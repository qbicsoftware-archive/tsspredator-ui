package view.firstImplementation;

import com.vaadin.ui.*;
import model.beans.AnnotationFileBean;
import model.beans.FastaFileBean;
import presenter.Presenter;

public class ConditionDataPanel extends DataPanel {
    Grid<FastaFileBean> fastaGrid;
    Grid<AnnotationFileBean> gffGrid;


    public ConditionDataPanel(Presenter presenter) {
        super(presenter);
        numberOfDatasetsBox.setCaption("Select number of Conditions");
        fastaGrid = new Grid<>("Genome FASTA");
        fastaGrid.addColumn(FastaFileBean::getName).setCaption("File name");
        fastaGrid.addColumn(FastaFileBean::getCreationDate).setCaption("Creation Date");
        fastaGrid.addColumn(FastaFileBean::getSizeInKB).setCaption("Size in KB");
        gffGrid = new Grid<>("Genome annotation (GFF)");
        gffGrid.addColumn(AnnotationFileBean::getName).setCaption("File name");
        gffGrid.addColumn(AnnotationFileBean::getCreationDate).setCaption("Creation Date");
        gffGrid.addColumn(AnnotationFileBean::getSizeInKB).setCaption("Size in KB");
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox,
                new HorizontalLayout(fastaGrid, gffGrid), setNumbers, datasetAccordion);
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
