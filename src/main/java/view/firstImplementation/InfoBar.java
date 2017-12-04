package view.firstImplementation;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class InfoBar extends CustomComponent {
    private Label infoLabel;

    public InfoBar(String infoText){
        infoLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml() + "\t" + infoText, ContentMode.HTML);
        addStyleName("my-info-bar");
        infoLabel.setWidth(100,Unit.PERCENTAGE);
        setCompositionRoot(infoLabel);
    }
}
