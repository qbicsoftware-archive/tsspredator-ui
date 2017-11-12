package view;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import model.Globals;
import presenter.Presenter;
import view.firstImplementation.*;

import java.io.File;

/**
 * The class {@link AccordionLayoutMain} contains the main layout of the GUI. Its core component
 * is an Accordion whose tabs are the parts of the TSSPredator workflow
 *
 * @author jmueller
 */
public class AccordionLayoutMain extends VerticalLayout {
    private Presenter presenter;
    private Accordion contentAccordion;
    private Button createConfigButton, downloadButton, loadConfigButton;
    private FileDownloader downloader;
    private GeneralConfigPanel generalConfigPanel;
    private ConditionDataPanel conditionDataPanel;
    private GenomeDataPanel genomeDataPanel;
    private ParametersPanel parametersPanel;

    public AccordionLayoutMain(Presenter presenter) {
        this.presenter = presenter;
        createContentAccordion();
        createConfigButton = new Button("Create Config File", (Button.ClickListener) clickEvent -> {
            File file = this.presenter.produceConfigFile();
            downloader = new FileDownloader(new FileResource(file));
            downloader.extend(downloadButton);
        });
        downloadButton = new Button("Download Config File");
        loadConfigButton = new Button("Load existing configuration");
        loadConfigButton.addClickListener(e -> {
            //TODO: Tell presenter to start loading procedure
        });
        this.addComponents(contentAccordion, createConfigButton, downloadButton, loadConfigButton);
    }

    private void createContentAccordion() {
        contentAccordion = new Accordion();
        generalConfigPanel = new GeneralConfigPanel(presenter);

        genomeDataPanel = new GenomeDataPanel(presenter);
        conditionDataPanel = new ConditionDataPanel(presenter);

        parametersPanel = new ParametersPanel(presenter);

        contentAccordion.addTab(generalConfigPanel, Globals.GENERAL_PANEL_CAPTION);
        contentAccordion.addTab(genomeDataPanel, Globals.DATA_PANEL_CAPTION);
        contentAccordion.addTab(conditionDataPanel, Globals.DATA_PANEL_CAPTION);
        //Only one of the two data panels is visible
        contentAccordion.getTab(1).setVisible(!Globals.IS_CONDITIONS_INIT);
        contentAccordion.getTab(2).setVisible(Globals.IS_CONDITIONS_INIT);
        contentAccordion.addTab(parametersPanel, Globals.PARAMETERS_PANEL_CAPTION);

    }

    public void updateDataPanelMode(boolean isConditions) {
        //The genomeDataPanel has tab index 1, the conditionDataPanel has tab index 2
        // in the contentAccordion
        contentAccordion.getTab(1).setVisible(!isConditions);
        contentAccordion.getTab(2).setVisible(isConditions);

    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public GeneralConfigPanel getGeneralConfigPanel() {
        return generalConfigPanel;
    }

    public ParametersPanel getParametersPanel() {
        return parametersPanel;
    }

    public GenomeDataPanel getGenomeDataPanel() {
        return genomeDataPanel;
    }

    public ConditionDataPanel getConditionDataPanel() {
        return conditionDataPanel;
    }


}
