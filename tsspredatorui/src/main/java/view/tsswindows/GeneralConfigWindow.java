package view.tsswindows;


import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.ui.*;

/**
 * In this window, the user chooses a name for his project, selects the type of study,
 * and uploads an alignment file (if he selected "Strain or species")
 */
public class GeneralConfigWindow extends TSSWindow {
    @Override
    void designContentLayout() {
        Layout contentLayout = new FormLayout();
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
        setContentLayout(contentLayout);
    }
}
