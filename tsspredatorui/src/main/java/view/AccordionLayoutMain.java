package view;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import presenter.Presenter;
import view.firstImplementation.*;

import java.io.File;

/**
 * TODO: Make this an interface
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

        contentAccordion.addTab(generalConfigPanel, "General Configuration");
        contentAccordion.addTab(genomeDataPanel, "Data Settings");
        contentAccordion.addTab(conditionDataPanel, "Data Settings");
        //Initially, the "Data Settings"-panel is a GenomeDataPanel,
        // since "Genome" is the initial selection in the respective checkbox.
        //We thus set conditionDataPanel to invisible
        contentAccordion.getTab(2).setVisible(false);
        contentAccordion.addTab(parametersPanel, "Parameters");

    }

    public void updateDataPanelMode(boolean isConditions){
        //The genomeDataPanel has tab index 1, the conditionDataPanel has tab index 2
        // in the contentAccordion
        if(isConditions){
            contentAccordion.getTab(2).setVisible(true);
            contentAccordion.getTab(1).setVisible(false);
        }else{
            contentAccordion.getTab(1).setVisible(true);
            contentAccordion.getTab(2).setVisible(false);
        }

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
