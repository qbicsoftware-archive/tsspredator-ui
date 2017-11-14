package view.firstImplementation;

import com.vaadin.ui.*;
import model.Globals;
import model.beans.AlignmentFileBean;
import model.beans.ProjectBean;
import presenter.Presenter;

import java.util.LinkedList;
import java.util.List;

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



        projectGrid.addStyleName("my-file-grid");

        projectTypeButtonGroup = new RadioButtonGroup<>("Select type of study");
        String strainOrSpecies = Globals.COMPARE_GENOMES;
        String conditions = Globals.COMPARE_CONDITIONS;
        projectTypeButtonGroup.setItems(strainOrSpecies, conditions);


        projectTypeButtonGroup.setSelectedItem(strainOrSpecies);

        alignmentFileGrid = new Grid<>("Select alignment file");
        alignmentFileGrid.addColumn(AlignmentFileBean::getName).setCaption("File name");
        alignmentFileGrid.addColumn(AlignmentFileBean::getCreationDate).setCaption("Creation Date");
        alignmentFileGrid.addColumn(AlignmentFileBean::getSizeInKB).setCaption("Size in KB");
        alignmentFileGrid.addStyleName("my-file-grid");
        //The alignment file selection should only be visible if strains/species are to be compared
        alignmentFileGrid.setVisible(!Globals.IS_CONDITIONS_INIT);
        contentLayout.addComponents(projectTypeButtonGroup, projectGrid, alignmentFileGrid);
        panel.setContent(contentLayout);


        //<-- DEBUG
        List<ProjectBean> projectBeanList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            ProjectBean pb = new ProjectBean();
            pb.setName("TestProject " + i);
            pb.setRegistrationDate("01-01-01");
            projectBeanList.add(pb);
        }
        projectGrid.setItems(projectBeanList);

        List<AlignmentFileBean> alignmentFileBeanList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            AlignmentFileBean afb = new AlignmentFileBean();
            afb.setName("TestAlignmentFile " + i);
            afb.setCreationDate("01-01-01");
            afb.setSizeInKB(42);
            alignmentFileBeanList.add(afb);
        }
        alignmentFileGrid.setItems(alignmentFileBeanList);

        //DEBUG -->


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
