package view.myfirstview;

import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.ui.*;
import view.GeneralConfigView;

/**
 * This component has a panel where the user chooses a name for his project, selects the type of study,
 * and uploads an alignment file (if he selected "Strain or species")
 * @author jmueller
 */
public class GeneralConfigPanel extends CustomComponent implements GeneralConfigView{
    private Panel generalConfigPanel;
    private Layout contentLayout;

    public GeneralConfigPanel(){
        generalConfigPanel = designPanel();
        setCompositionRoot(generalConfigPanel);

    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new FormLayout();
        TextField projectName = new TextField("Enter a name for your project");

        RadioButtonGroup<String> projectTypeButtonGroup = new RadioButtonGroup<>("Select type of study");
        String strainOrSpecies = "Compare Strain/Species";
        String conditions = "Compare Conditions";
        projectTypeButtonGroup.setItems(strainOrSpecies, conditions);

        Upload alignmentFileUpload = new Upload("Upload alignment file", (Upload.Receiver) (s, s1) -> null);
        //The uploader should only be visible if strains/species are to be compared
        alignmentFileUpload.setVisible(false);
        projectTypeButtonGroup.addSelectionListener((SingleSelectionListener<String>) e -> {
            if (e.getSelectedItem().get().equals(strainOrSpecies))
                alignmentFileUpload.setVisible(true);
            else
                alignmentFileUpload.setVisible(false);
        });


        contentLayout.addComponents(projectName, projectTypeButtonGroup, alignmentFileUpload);
        panel.setContent(contentLayout);
        return panel;
    }
}
