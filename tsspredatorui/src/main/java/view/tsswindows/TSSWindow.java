package view.tsswindows;

import com.vaadin.ui.*;

/**
 * Template for the windows that lead through the TSSPredator workflow.
 * Consists of a variable upper Layout and a static lower HorizontalLayout.
 * The latter contains two buttons to jump to the next or to the previous step
 * as well as a bar with info about the current state of the process.
 */
public abstract class TSSWindow extends Window {
    private VerticalLayout tssWindowLayout;
    private Layout contentLayout;
    private HorizontalLayout navigationLayout;
    private Button nextButton, previousButton;
    private Label infoLabel;

    public TSSWindow() {
        tssWindowLayout = new VerticalLayout();

        //The contentLayout gets initialized individually by the classes inheriting this one by implementing this method
        contentLayout = designContentLayout();

        navigationLayout = designNavigationLayout();

        tssWindowLayout.addComponents(contentLayout, navigationLayout);
        setContent(tssWindowLayout);
        addStyleName("my-tsswindow");
        center();

    }

    /**
     * This is the method that has to be implemented by every inheriting class.
     */
    abstract Layout designContentLayout();

    /**
     * Generic part of every TSSWindow, shouldn't be overridden
     */
    private HorizontalLayout designNavigationLayout() {
        navigationLayout = new HorizontalLayout();
        previousButton = new Button("Previous Step");
        nextButton = new Button("Next Step");
        previousButton.addStyleName("my-navigation-button");
        nextButton.addStyleName("my-navigation-button");
        nextButton.setStyleName("my-navigation-button");
        infoLabel = new Label("Info comes here");
        navigationLayout.addComponents(previousButton, infoLabel, nextButton);
        navigationLayout.setStyleName("my-navigation-layout");


        return navigationLayout;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getPreviousButton() {
        return previousButton;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public void setContentLayout(Layout contentLayout) {
        this.contentLayout = contentLayout;
    }

    public void setInfoLabel(Label infoLabel) {
        this.infoLabel = infoLabel;
    }
}
