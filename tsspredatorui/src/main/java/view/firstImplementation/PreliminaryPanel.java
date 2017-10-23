package view.firstImplementation;

import com.vaadin.data.HasValue;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.ui.*;
import presenter.Presenter;
import view.PreliminaryView;

/**
 * This component has a Panel in which the user decides how much memory he wants to use,
 * and if he wants to use a previously created configuration
 * If he does, the remaining configuration should be skipped.
 *
 * @author jmueller
 */
public class PreliminaryPanel extends CustomComponent implements PreliminaryView {
    private Presenter presenter;
    private Panel preliminaryPanel;
    private Layout contentLayout;
    private RadioButtonGroup<String> allocatedSpaceButtons;
    private TextField customValue;
    private CheckBox useCustomValue;
    private Button loadConfigButton;
    private CheckBox chooseConfigExtensive;

    public PreliminaryPanel(Presenter presenter) {
        preliminaryPanel = designPanel();
        setCompositionRoot(preliminaryPanel);
        this.presenter = presenter;
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new VerticalLayout();
        allocatedSpaceButtons = new RadioButtonGroup<>("How much space do you want to use?");
        allocatedSpaceButtons.setItems("500 MB", "1000 MB", "1500 MB", "2000 MB", "3000 MB", "4000 MB", "6000 MB");
        //TODO: Should these values be provided by the model or is it okay this way?

        HorizontalLayout customValueLayout = new HorizontalLayout();
        customValue = new TextField("Custom: ");
        useCustomValue = new CheckBox("Run with custom value");
        customValueLayout.addComponents(customValue, useCustomValue);

        loadConfigButton = new Button("Load existing configuration");

        chooseConfigExtensive = new CheckBox("Enable extensive configuration");
        chooseConfigExtensive.setValue(true);
        //TODO: Implement basic configuration workflow
        chooseConfigExtensive.setEnabled(false);

        setupListeners();
        contentLayout.addComponents(allocatedSpaceButtons, customValueLayout, loadConfigButton, chooseConfigExtensive);
        panel.setContent(contentLayout);
        return panel;
    }

    private void setupListeners() {
        allocatedSpaceButtons.addValueChangeListener(e -> {
            useCustomValue.setValue(false);
            String value_raw = allocatedSpaceButtons.getValue().split(" ")[0];
            presenter.setMemory(Integer.parseInt(value_raw));
        });

        customValue.addValueChangeListener(e -> {
            useCustomValue.setValue(true);
            presenter.setMemory(Integer.parseInt(customValue.getValue()));
        });

        useCustomValue.addValueChangeListener(e -> {
            if (useCustomValue.getValue())
                presenter.setMemory(Integer.parseInt(customValue.getValue()));
            else {
                String value_raw = allocatedSpaceButtons.getValue().split(" ")[0];
                presenter.setMemory(Integer.parseInt(value_raw));
            }

        });

        loadConfigButton.addClickListener(e -> {
            //TODO: Tell presenter to start loading procedure
        });

        chooseConfigExtensive.addValueChangeListener(vce -> presenter.setConfigExtensive(vce.getValue()));
    }
}
