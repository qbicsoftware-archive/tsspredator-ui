package view.firstImplementation;

import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.ui.*;
import model.beans.AlignmentFileBean;
import model.beans.ProjectBean;
import presenter.Presenter;

/**
 * This component has a panel where the user chooses a nameField for his project, selects the type of study,
 * and uploads an alignment file (if he selected "Strain or species")
 *
 * @author jmueller
 */
public class GeneralConfigPanel extends CustomComponent{
    private Presenter presenter;
    private Panel generalConfigPanel;
    private Layout contentLayout;
    private Grid<ProjectBean> projectGrid;
    private Grid<AlignmentFileBean> alignmentFileGrid;
    RadioButtonGroup<String> projectTypeButtonGroup;

    public GeneralConfigPanel(Presenter presenter) {
        generalConfigPanel = designPanel();
        setCompositionRoot(generalConfigPanel);
        this.presenter = presenter;

    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new FormLayout();
        projectGrid = new Grid<>("Select your project");
        projectGrid.addColumn(ProjectBean::getName).setCaption("Project Name");
        projectGrid.addColumn(ProjectBean::getRegistrationDate).setCaption("Registration Date");

        projectTypeButtonGroup = new RadioButtonGroup<>("Select type of study");
        //TODO: Replace hard-coded strings by global variables (and also replace in Presenter!)
        String strainOrSpecies = "Compare Strain/Species";
        String conditions = "Compare Conditions";
        projectTypeButtonGroup.setItems(strainOrSpecies, conditions);


        projectTypeButtonGroup.setSelectedItem(strainOrSpecies);

        alignmentFileGrid = new Grid<>("Select alignment file");
        alignmentFileGrid.addColumn(AlignmentFileBean::getName).setCaption("File name");
        alignmentFileGrid.addColumn(AlignmentFileBean::getCreationDate).setCaption("Creation Date");
        alignmentFileGrid.addColumn(AlignmentFileBean::getSizeInKB).setCaption("Size in KB");
        //The alignment file selection should only be visible if strains/species are to be compared
        //(which is the initial case)
        alignmentFileGrid.setVisible(true);
        contentLayout.addComponents(projectTypeButtonGroup, new HorizontalLayout(projectGrid, alignmentFileGrid));
        panel.setContent(contentLayout);
        return panel;
    }


    public Grid<ProjectBean> getProjectGrid() {
        return projectGrid;
    }

    public Grid<AlignmentFileBean> getAlignmentFileGrid() {
        return alignmentFileGrid;
    }

    public RadioButtonGroup<String> getProjectTypeButtonGroup() {
        return projectTypeButtonGroup;
    }
}
