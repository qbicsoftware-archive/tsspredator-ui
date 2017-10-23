package view;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import presenter.Presenter;
import view.firstImplementation.DataPanel;
import view.firstImplementation.GeneralConfigPanel;
import view.firstImplementation.ParametersPanel;
import view.firstImplementation.PreliminaryPanel;

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
    private Button createConfigButton, downloadButton;
    private FileDownloader downloader;
    private PreliminaryPanel preliminaryPanel;
    private GeneralConfigPanel generalConfigPanel;
    private DataPanel dataPanel;
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
        this.addComponents(contentAccordion, createConfigButton, downloadButton);
    }

    private void createContentAccordion() {
        contentAccordion = new Accordion();

        preliminaryPanel = new PreliminaryPanel(presenter);
        generalConfigPanel = new GeneralConfigPanel(presenter);
        dataPanel = new DataPanel(presenter);
        parametersPanel = new ParametersPanel(presenter);

        contentAccordion.addTab(preliminaryPanel, "Preliminary Settings");
        contentAccordion.addTab(generalConfigPanel, "General Configuration");
        contentAccordion.addTab(dataPanel, "Data Settings");
        contentAccordion.addTab(parametersPanel, "Parameters");

    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public PreliminaryPanel getPreliminaryPanel() {
        return preliminaryPanel;
    }

    public GeneralConfigPanel getGeneralConfigPanel() {
        return generalConfigPanel;
    }

    public DataPanel getDataPanel() {
        return dataPanel;
    }

    public ParametersPanel getParametersPanel() {
        return parametersPanel;
    }
}
