package view;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.VerticalLayout;

/**
 * The class {@link AccordionLayoutMain} contains the main layout of the GUI. Its core component
 * is an Accordion whose tabs are the parts of the TSSPredator workflow
 *
 * @author jmueller
 */
public class AccordionLayoutMain extends VerticalLayout {
    private Accordion contentAccordion;

    public AccordionLayoutMain() {
        createContentAccordion();
        this.addComponents(contentAccordion);
    }

    private void createContentAccordion() {
        contentAccordion = new Accordion();
        contentAccordion.addTab(new PreliminaryPanel(), "Preliminary Settings");
        contentAccordion.addTab(new GeneralConfigPanel(), "General Configuration");
        contentAccordion.addTab(new DataPanel(), "Data Settings");
        contentAccordion.addTab(new ParametersPanel(), "Parameters");

    }
}
