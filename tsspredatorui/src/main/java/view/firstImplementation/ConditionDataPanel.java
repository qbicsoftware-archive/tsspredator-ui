package view.firstImplementation;

import com.vaadin.ui.*;
import presenter.Presenter;

public class ConditionDataPanel extends DataPanel {
    TextField fastaField, gffField;


    public ConditionDataPanel(Presenter presenter) {
        super(presenter);
        numberOfDatasetsBox.setCaption("Select number of Conditions");
        fastaField = new TextField("Genome FASTA");
        gffField = new TextField("Genome Annotation");

        //Setup listeners for the two fields
        //For now, every dataset holds every value, however this makes no sense for the condition variant
        //since there's only one fasta and gff there
        fastaField.addValueChangeListener(vce -> presenter.updateAllGenomeFastas(vce.getValue()));
        gffField.addValueChangeListener(vce -> presenter.updateAllGenomeAnnotations(vce.getValue()));
        contentLayout.addComponents(numberOfDatasetsBox, numberOfReplicatesBox,
                fastaField, gffField, setNumbers, datasetAccordion);
    }

    class ConditionTab extends DatasetTab {
        TextField nameField;

        public ConditionTab(int index) {
            super(index);
            nameField = new TextField("Name");
            this.tab.addComponents(nameField, new Label("RNA-seq graph files:"), replicatesSheet);
            nameField.addValueChangeListener(vce -> presenter.updateDatasetName(index, vce.getValue()));


        }

        public TextField getNameField() {
            return nameField;
        }
    }

    public ConditionTab createConditionTab(int index){
        return new ConditionTab(index);
    }

    public TextField getFastaField() {
        return fastaField;
    }

    public TextField getGffField() {
        return gffField;
    }
}
