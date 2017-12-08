package main;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
//import org.slf4j.bridge.SLF4JBridgeHandler;
import presenter.Presenter;
import view.AccordionLayoutMain;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Widgetset("main.AppWidgetSet")
@Theme("mytheme")
public class MyUI extends UI {
    static {
        //Never actually worked and suddenly showed an error out of nowhere, so I'm commenting it out for now
        //SLF4JBridgeHandler.install();
    }


    protected void init(VaadinRequest vaadinRequest) {
        Presenter presenter = new Presenter();
        AccordionLayoutMain layout = new AccordionLayoutMain(presenter);
        presenter.setView(layout);
        presenter.initFields();
        presenter.initBindings();
        setContent(layout);
    }
}
