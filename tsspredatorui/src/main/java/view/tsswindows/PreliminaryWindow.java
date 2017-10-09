package view.tsswindows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;

/**
 * In this window, the user decides how much memory he wants to use,
 * and if he wants to use a previously created configuration
 * If he does, the remaining configuration is skipped
 */
public class PreliminaryWindow extends TSSWindow {
    @Override
    Layout designContentLayout() {
        Layout contentLayout = new VerticalLayout();
        RadioButtonGroup<String> allocatedSpaceButtons = new RadioButtonGroup<>("How much space do you want to use?");
        allocatedSpaceButtons.setItems("500MB", "1GB", "2GB", "4GB", "8GB");
        Button loadConfigButton = new Button("Load existing configuration");
        contentLayout.addComponents(allocatedSpaceButtons, loadConfigButton);
        setContentLayout(contentLayout);
        return contentLayout;
    }
}
