package main;


import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import presenter.Presenter;
import view.AccordionLayoutMain;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    protected void init(VaadinRequest vaadinRequest) {
        Presenter presenter = new Presenter();
        AccordionLayoutMain layout = new AccordionLayoutMain(presenter);
        presenter.setView(layout);
        presenter.initFields();
        presenter.initBindings();
        setContent(layout);
    }
}
