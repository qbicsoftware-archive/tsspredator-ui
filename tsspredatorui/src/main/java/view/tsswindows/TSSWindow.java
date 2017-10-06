package view.tsswindows;

import com.vaadin.ui.*;

/**
 * Template for the windows that lead through the TSSPredator workflow
 * Consists of a variable upper Layout and a static lower HorizontalLayout.
 * The latter contains two buttons to jump to the next or to the previous step (might be disabled or invisible)
 * as well as an infobar on where we are in the process
 */
public abstract class TSSWindow extends Window {
    private VerticalLayout tssWindowLayout;
    private Layout contentLayout;
    private HorizontalLayout navigationLayout;
    private Button nextButton, previousButton;
    private Label infoLabel;

    public TSSWindow() {
        this.tssWindowLayout = new VerticalLayout();
        this.navigationLayout = new HorizontalLayout();

        //The contentLayout gets initialized individually by the classes inheriting this one by implementing this method
        designContentLayout();

        designInfobar();

        tssWindowLayout.addComponents(contentLayout, navigationLayout);
        tssWindowLayout.setSizeUndefined();
        this.setContent(tssWindowLayout);
        this.setHeight("80%");
        this.setWidth("80%");
        this.center();
    }

    abstract void designContentLayout();

    private void designInfobar() {
        previousButton = new Button("Previous Step");
        nextButton = new Button("Next Step");
        infoLabel = new Label("Info comes here");
        navigationLayout.addComponents(previousButton, infoLabel, nextButton);


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
