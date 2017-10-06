package view.tsswindows;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

public class ParametersWindow1 extends TSSWindow {
    @Override
    void designContentLayout() {
        Layout contentLayout = new VerticalLayout();
        //TODO: Bind the max value of the reduction sliders to the other sliders
        HorizontalLayout heightParams = new HorizontalLayout();
        Slider stepHeight = new Slider("Step Height");
        stepHeight.setMin(0);
        stepHeight.setMax(1);
        stepHeight.setResolution(1);
        Slider stepHeightReduction = new Slider("Step Height Reduction");
        stepHeightReduction.setMin(0);
        stepHeightReduction.setMax(1);
        stepHeightReduction.setResolution(1);
        heightParams.addComponents(stepHeight,stepHeightReduction);

        HorizontalLayout factorParams = new HorizontalLayout();
        Slider stepFactor = new Slider("Step Factor");
        stepFactor.setMin(1);
        stepFactor.setMax(2);
        stepFactor.setResolution(1);
        Slider stepFactorReduction = new Slider("Step Factor Reduction");
        stepFactorReduction.setMin(0);
        stepFactorReduction.setMax(2);
        stepFactorReduction.setResolution(1);
        factorParams.addComponents(stepFactor,stepFactorReduction);

        contentLayout.addComponents(heightParams,factorParams);
        setContentLayout(contentLayout);
    }
}
