package view;

import com.vaadin.ui.*;

/**
 * This component has a Panel in which the user decides how much memory he wants to use,
 * and if he wants to use a previously created configuration
 * If he does, the remaining configuration should be skipped.
 *
 * @author jmueller
 */
public class PreliminaryPanel extends CustomComponent{
    private Panel preliminaryPanel;
    private Layout contentLayout;

    public PreliminaryPanel(){
        preliminaryPanel = designPanel();
        setCompositionRoot(preliminaryPanel);
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new VerticalLayout();
        RadioButtonGroup<String> allocatedSpaceButtons = new RadioButtonGroup<>("How much space do you want to use?");
        allocatedSpaceButtons.setItems("500MB", "1GB", "2GB", "4GB", "8GB");
        Button loadConfigButton = new Button("Load existing configuration");
        contentLayout.addComponents(allocatedSpaceButtons, loadConfigButton);
        panel.setContent(contentLayout);
        return panel;
    }
}
