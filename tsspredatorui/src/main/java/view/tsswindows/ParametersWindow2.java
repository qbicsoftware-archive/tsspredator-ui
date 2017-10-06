package view.tsswindows;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

public class ParametersWindow2 extends TSSWindow{
    @Override
    void designContentLayout() {
        Layout contentLayout = new VerticalLayout();
        Slider enrichmentFactor = new Slider("Enrichment Factor");
        enrichmentFactor.setMin(0);
        enrichmentFactor.setMax(10);
        enrichmentFactor.setResolution(1);

        Slider processingSiteFactor = new Slider("Processing Site Factor");
        processingSiteFactor.setMin(0);
        processingSiteFactor.setMax(10);
        processingSiteFactor.setResolution(1);

        Slider stepLength = new Slider("Step Length");
        stepLength.setMin(0);
        stepLength.setMax(100);
        stepLength.setResolution(0);

        Slider baseHeight = new Slider("Base Height (disabled by default)");
        baseHeight.setEnabled(false);

        contentLayout.addComponents(enrichmentFactor,processingSiteFactor,stepLength,baseHeight);
        setContentLayout(contentLayout);



    }
}
