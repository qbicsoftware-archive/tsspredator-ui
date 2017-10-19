package view.myfirstview;

import com.vaadin.ui.*;
import view.PreliminaryView;

/**
 * This component has a Panel in which the user decides how much memory he wants to use,
 * and if he wants to use a previously created configuration
 * If he does, the remaining configuration should be skipped.
 *
 * @author jmueller
 */
public class PreliminaryPanel extends CustomComponent implements PreliminaryView {
    private Panel preliminaryPanel;
    private Layout contentLayout;
    private RadioButtonGroup<String> allocatedSpaceButtons;
    private TextField customValue;
    private CheckBox useCustomValue;


    public PreliminaryPanel() {
        preliminaryPanel = designPanel();
        setCompositionRoot(preliminaryPanel);
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new VerticalLayout();
        allocatedSpaceButtons = new RadioButtonGroup<>("How much space do you want to use?");
        allocatedSpaceButtons.setItems("500 MB", "1000 MB", "1500 MB", "2000 MB", "3000 MB", "4000 MB", "6000 MB");

        HorizontalLayout customValueLayout = new HorizontalLayout();
        customValue = new TextField("Custom: ");
        useCustomValue = new CheckBox("Run with custom value");
        customValueLayout.addComponents(customValue, useCustomValue);

        Button loadConfigButton = new Button("Load existing configuration");

        contentLayout.addComponents(allocatedSpaceButtons, customValueLayout, loadConfigButton);

        panel.setContent(contentLayout);
        return panel;
    }

    @Override
    public int chooseSystemMemory() {
        int value;
        if (useCustomValue.getValue())
            value = Integer.parseInt(customValue.getValue());
        else{
            String value_raw = allocatedSpaceButtons.getValue().split(" ")[0];
            value = Integer.parseInt(value_raw);
        }
        return value;
    }
}
