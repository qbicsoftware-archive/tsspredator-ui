package main;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import presenter.TSSWindowChainPresenter;
import view.tsswindows.*;
import view.TSSWindowChain;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        Button showWindowButton = new Button("Start TSS-Prediction");
        TSSWindowChain windowChain = setupTSSWindowChain();
        layout.addComponent(showWindowButton);
        showWindowButton.addClickListener(e->{
            addWindow(windowChain.getWindowList().peekFirst());
        });


        setContent(layout);
    }

    private TSSWindowChain setupTSSWindowChain() {
        TSSWindowChain windowChain = new TSSWindowChain();

        //Add the windows, one after the other
        windowChain.getWindowList().add(new PreliminaryWindow());
        windowChain.getWindowList().add(new GeneralConfigWindow());
        windowChain.getWindowList().add(new DataWindow());
        windowChain.getWindowList().add(new ParametersWindow1());
        windowChain.getWindowList().add(new ParametersWindow2());
        windowChain.getWindowList().add(new ParametersWindow3());
        windowChain.getWindowList().add(new ParametersWindow4());


        windowChain.createInfoLabels();
        TSSWindowChainPresenter windowChainPresenter = new TSSWindowChainPresenter();
        windowChainPresenter.setWindowChain(windowChain);
        windowChainPresenter.setupWindowButtons();
        return windowChain;

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
