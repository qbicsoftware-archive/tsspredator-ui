package view;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.components.grid.ItemClickListener;

/**
 * @author jmueller
 * Just a plain old Vaadin Grid with the extension of a double click event
 */
public class MyGrid<T> extends Grid<T>{
    private T selectedItem;

    public MyGrid(){
        new MyGrid("");
    }

    public MyGrid(String caption){
        super(caption);

        setStyleGenerator((StyleGenerator<T>) t -> this.getSelectedItems().contains(t) ? "selected": null);

        addSelectionListener((SelectionListener<T>) selectionEvent -> {
           if(selectionEvent.isUserOriginated())
               select(this.selectedItem);
           else
               selectedItem = selectionEvent.getFirstSelectedItem().get();
        });

        this.addItemClickListener((ItemClickListener<T>) clickEvent -> {
            if(!clickEvent.getMouseEventDetails().isDoubleClick())
                return;
            this.select(clickEvent.getItem());
            Notification.show(clickEvent.getItem().toString() + " has been selected");

        });

    }

}
