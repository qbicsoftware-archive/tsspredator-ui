package view;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropEvent;
import com.vaadin.ui.components.grid.GridDropListener;
import com.vaadin.ui.components.grid.GridDropTarget;
import model.beans.GraphFileBean;

import java.util.*;

/**
 * @author jmueller
 * Four of these grids appear in each replicate sheet.
 * Objects of this class mostly serve as drop targets/drag sources for items from the grid of available graph files
 */
public class MyGraphFileGrid extends Grid<GraphFileBean> {

    //Every time a drag event occurs in the grid of available graph files, this variable is set to the dragged item.
    //Note that it doesn't matter whether the item is actually dropped in this grid, in another one or in none at all
    private GraphFileBean draggedItem;
    private GraphFileBean selectedItem;


    public MyGraphFileGrid() {
        new MyGraphFileGrid("");
    }

    public MyGraphFileGrid(String caption) {
        super(caption);
        float gridWidth = 400;
        setWidth(gridWidth, Unit.PIXELS);
        addColumn(GraphFileBean::getName)
                .setCaption("File name")
                .setWidth(gridWidth / 2.25);
        addColumn(GraphFileBean::getCreationDate)
                .setCaption("Creation Date")
                .setWidth(gridWidth / 3.5);
        addColumn(GraphFileBean::getSizeInKB)
                .setCaption("Size (kB)")
                .setWidthUndefined();
        addStyleName("my-graph-file-grid");

        addSelectionListener(new SelectionListener<GraphFileBean>() {
            @Override
            public void selectionChange(SelectionEvent<GraphFileBean> selectionEvent) {
                //If it's a de-selection event, revert it if it's user-generated, else clear selection
                if (selectionEvent.getAllSelectedItems().isEmpty()) {
                    if (selectionEvent.isUserOriginated())
                        select(selectedItem);
                    else
                        selectedItem = null;
                }
            }
        });


        GridDropTarget dropTarget = new GridDropTarget<>(this, DropMode.ON_TOP);
        dropTarget.setDropEffect(DropEffect.MOVE);

        dropTarget.addGridDropListener(event ->

        {
            Grid<GraphFileBean> dragSourceGrid = (Grid<GraphFileBean>) event.getDragSourceComponent().get();
            ListDataProvider<GraphFileBean> sourceProvider = (ListDataProvider<GraphFileBean>) dragSourceGrid.getDataProvider();
            //Remove dragged item from source
            sourceProvider.getItems().remove(draggedItem);
            dragSourceGrid.deselectAll();
            //Check if there's already an item in the target grid and put it into the source grid
            if (selectedItem != null) {
                sourceProvider.getItems().add(selectedItem);
                dragSourceGrid.select(selectedItem);
                if (dragSourceGrid instanceof MyGraphFileGrid)
                    ((MyGraphFileGrid) dragSourceGrid).selectedItem = selectedItem;
            }
            //Put dragged item into grid and select it so the binder for the config file is triggered
            List<GraphFileBean> draggedItemList = new LinkedList<>();
            draggedItemList.add(draggedItem);
            this.setItems(draggedItemList);
            this.select(draggedItem);
            this.selectedItem = draggedItem;

            sourceProvider.refreshAll();
            this.getDataProvider().refreshAll();
        });

        //The grid is also a drag source
        GridDragSource<GraphFileBean> dragSource = new GridDragSource<>(this);
        dragSource.setEffectAllowed(EffectAllowed.MOVE);
    }

    public void setDraggedItem(GraphFileBean draggedItem) {
        this.draggedItem = draggedItem;
    }

}
