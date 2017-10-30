package view.firstImplementation;

import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.ui.*;
import presenter.Presenter;
import view.GeneralConfigView;

/**
 * This component has a panel where the user chooses a name for his project, selects the type of study,
 * and uploads an alignment file (if he selected "Strain or species")
 *
 * @author jmueller
 */
public class GeneralConfigPanel extends CustomComponent implements GeneralConfigView {
    private Presenter presenter;
    private Panel generalConfigPanel;
    private Layout contentLayout;
    private TextField projectName;
    private TextField alignmentFileUpload;
    RadioButtonGroup<String> projectTypeButtonGroup;

    public GeneralConfigPanel(Presenter presenter) {
        generalConfigPanel = designPanel();
        setCompositionRoot(generalConfigPanel);
        this.presenter = presenter;

    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new FormLayout();
        projectName = new TextField("Enter a name for your project");

        projectTypeButtonGroup = new RadioButtonGroup<>("Select type of study");
        String strainOrSpecies = "Compare Strain/Species";
        String conditions = "Compare Conditions";
        projectTypeButtonGroup.setItems(strainOrSpecies, conditions);
        projectTypeButtonGroup.setSelectedItem(strainOrSpecies);

        alignmentFileUpload = new TextField("Upload alignment file");
        //The uploader should only be visible if strains/species are to be compared
        //(which is the initial case)
        alignmentFileUpload.setVisible(true);
        projectTypeButtonGroup.addSelectionListener((SingleSelectionListener<String>) e -> {
            if (e.getSelectedItem().get().equals(strainOrSpecies))
                alignmentFileUpload.setVisible(true);
            else
                alignmentFileUpload.setVisible(false);
        });

        setupListeners();
        contentLayout.addComponents(projectName, projectTypeButtonGroup, alignmentFileUpload);
        panel.setContent(contentLayout);
        return panel;
    }

    private void setupListeners() {
        projectName.addValueChangeListener(vce -> presenter.updateProjectName(vce.getValue()));
        projectTypeButtonGroup.addValueChangeListener(vce -> presenter.updateMode(vce.getValue().equals("Compare Conditions")));
        alignmentFileUpload.addValueChangeListener(vce -> presenter.updateAlignmentFile(vce.getValue()));
    }
}
