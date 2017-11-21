package view.firstImplementation;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import model.beans.GraphFileBean;
import presenter.Presenter;
import view.MyGraphFileGrid;
import view.MyGrid;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * In this abstract class, the parts of the DataPanel which are common to both
 * the genome and the condition variant of the workflow are stored.
 * The variable parts are in classes GenomeDataPanel or ConditionDataPanel, respectively.
 *
 * @author jmueller
 */
public abstract class DataPanel extends CustomComponent {
    Presenter presenter;
    Panel dataPanel;
    Layout contentLayout;
    Accordion datasetAccordion;
    ComboBox<Integer> numberOfDatasetsBox;
    ComboBox<Integer> numberOfReplicatesBox;
    //Button setNumbers;

    public DataPanel(Presenter presenter) {
        dataPanel = designPanel();
        setCompositionRoot(dataPanel);
        this.presenter = presenter;
    }

    private Panel designPanel() {
        Panel panel = new Panel();
        contentLayout = new FormLayout();
        numberOfDatasetsBox = new ComboBox<>();
        numberOfReplicatesBox = new ComboBox<>("Select number of Replicates");


        List<Integer> possibleDatasetNumber =
                IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());
        numberOfDatasetsBox.setItems(possibleDatasetNumber);

        List<Integer> possibleReplicateNumber =
                IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());
        numberOfReplicatesBox.setItems(possibleReplicateNumber);

        datasetAccordion = new Accordion();
        datasetAccordion.setWidth("100%");

        panel.setContent(contentLayout);
        return panel;
    }

    /**
     * The accordion is initialized with one dataset (genome/condition) and one replicate
     */
    public void initAccordion() {
        Component initialTab = this instanceof GenomeDataPanel
                ? ((GenomeDataPanel) this).createGenomeTab(0)
                : ((ConditionDataPanel) this).createConditionTab(0);
        //Tell presenter to set up bindings
        datasetAccordion.addTab(initialTab, "Genome " + 1);
        presenter.initDatasetBindings(0);
        presenter.initReplicateBindings(0, 0);
    }


    /**
     * The genomes/conditions are organized as tabs in an Accordion. When the user changes
     * the number of genomes/conditions or replicates, this Accordion has to be updated,
     * which happens here
     */
    public void updateAccordion(int oldDatasetCount, int oldReplicateCount) {
        //Adjust number of replicate tabs for each dataset tab
        int replicateDelta = presenter.getNumberOfReplicates() - oldReplicateCount;
        for (int datasetIndex = 0; datasetIndex < oldDatasetCount; datasetIndex++) {
            datasetAccordion.getTab(datasetIndex);
            TabSheet.Tab tab = datasetAccordion.getTab(datasetIndex);
            //Access the replicate sheet of the current tab
            //It's the last of its three components, so its index is 2
            TabSheet currentReplicateSheet = ((DatasetTab) tab.getComponent()).replicatesSheet;
            if (replicateDelta > 0) {
                //Add new replicate tabs
                for (int replicateIndex = oldReplicateCount;
                     replicateIndex < presenter.getNumberOfReplicates();
                     replicateIndex++) {
                    Component replicateTab = new ReplicateTab(datasetIndex, replicateIndex);
                    currentReplicateSheet.addTab(replicateTab, "Replicate " + createReplicateID(replicateIndex));
                    presenter.initReplicateBindings(datasetIndex, replicateIndex);
                }
            } else {
                //Remove excess replicate tabs
                for (int replicateIndex = oldReplicateCount; replicateIndex > presenter.getNumberOfReplicates(); replicateIndex--) {
                    currentReplicateSheet.removeTab(currentReplicateSheet.getTab(replicateIndex - 1));
                }
            }
        }


        int datasetDelta = presenter.getNumberOfDatasets() - oldDatasetCount;
        if (datasetDelta > 0) {
            //Add new dataset tabs
            for (int datasetIndex = oldDatasetCount; datasetIndex < presenter.getNumberOfDatasets(); datasetIndex++) {
                Component currentTab = this instanceof GenomeDataPanel
                        ? ((GenomeDataPanel) this).createGenomeTab(datasetIndex)
                        : ((ConditionDataPanel) this).createConditionTab(datasetIndex);
                //Tell presenter to set up bindings
                datasetAccordion.addTab(currentTab, "Genome " + (datasetIndex + 1));
                presenter.initDatasetBindings(datasetIndex);
                for (int replicateIndex = 0;
                     replicateIndex < presenter.getNumberOfReplicates();
                     replicateIndex++) {
                    presenter.initReplicateBindings(datasetIndex, replicateIndex);
                }
            }
        } else {
            //Remove excess dataset tabs
            for (int i = oldDatasetCount; i > presenter.getNumberOfDatasets(); i--) {
                datasetAccordion.removeTab(datasetAccordion.getTab(i - 1));
            }
        }

    }

    /**
     * This class is extended by GenomeTab in GenomeDataPanel and ConditionTab in ConditionDataPanel.
     */
    public abstract class DatasetTab extends CustomComponent {
        VerticalLayout tab;
        TabSheet replicatesSheet;

        public DatasetTab(int index) {
            tab = new VerticalLayout();
            replicatesSheet = new TabSheet();
            for (int replicateIndex = 0;
                 replicateIndex < presenter.getNumberOfReplicates();
                 replicateIndex++) {
                ReplicateTab replicateTab = new ReplicateTab(index, replicateIndex);
                replicatesSheet.addTab(replicateTab, "Replicate " + createReplicateID(replicateIndex));
            }
            setCompositionRoot(tab);
        }

        public ReplicateTab getReplicateTab(int index) {
            return (ReplicateTab) replicatesSheet.getTab(index).getComponent();
        }

    }

    /**
     * This inner class represents a replicate tab in the dataset accordion.
     * It works for both the genome and the condition variants.
     */
    public class ReplicateTab extends CustomComponent {
        VerticalLayout layout;
        MyGraphFileGrid enrichedCoding, enrichedTemplate, normalCoding, normalTemplate;
        Grid<GraphFileBean> graphFileGrid;
        private GraphFileBean draggedItem;

        public ReplicateTab(int datasetIndex, int replicateIndex) {
            layout = new VerticalLayout();

            VerticalLayout enrichedPart = new VerticalLayout();
            enrichedCoding = new MyGraphFileGrid("Treated Coding Strand");
            enrichedTemplate = new MyGraphFileGrid("Treated Template Strand");
            enrichedPart.addComponents(enrichedCoding, enrichedTemplate);
            VerticalLayout normalPart = new VerticalLayout();
            normalCoding = new MyGraphFileGrid("Normal Coding Strand");
            normalTemplate = new MyGraphFileGrid("Normal Template Strand");
            normalPart.addComponents(normalCoding, normalTemplate);

            graphFileGrid = new Grid<>("Available Graph Files");
            float graphFileGridWidth = 600;
            graphFileGrid.setWidth(graphFileGridWidth, Unit.PIXELS);
            graphFileGrid.addColumn(GraphFileBean::getName)
                    .setCaption("File name")
                    .setWidth(graphFileGridWidth / 2);
            graphFileGrid.addColumn(GraphFileBean::getCreationDate)
                    .setCaption("Creation Date")
                    .setWidth(graphFileGridWidth / 3.5);
            graphFileGrid.addColumn(GraphFileBean::getSizeInKB)
                    .setCaption("Size (kB)")
                    .setWidthUndefined(); //Column takes up remaining space

            graphFileGrid.addStyleName("my-file-grid");

            setupDragSource(graphFileGrid);
            setupDragSource(enrichedCoding);
            setupDragSource(enrichedTemplate);
            setupDragSource(normalCoding);
            setupDragSource(normalTemplate);

            //Setup drop target for the graph file grid
            GridDropTarget dropTarget = new GridDropTarget<>(graphFileGrid, DropMode.ON_TOP_OR_BETWEEN);
            dropTarget.setDropEffect(DropEffect.MOVE);
            dropTarget.addGridDropListener(event -> {
                //Remove dragged item from source
                Grid<GraphFileBean> dragSourceGrid = (Grid<GraphFileBean>) event.getDragSourceComponent().get();
                ListDataProvider<GraphFileBean> sourceProvider = (ListDataProvider<GraphFileBean>) dragSourceGrid.getDataProvider();
                sourceProvider.getItems().remove(draggedItem);
                dragSourceGrid.deselectAll();
                sourceProvider.refreshAll();
                Collection<GraphFileBean> items = ((ListDataProvider<GraphFileBean>) graphFileGrid.getDataProvider()).getItems();
                if (!items.contains(draggedItem))
                    items.add(draggedItem);
                graphFileGrid.getDataProvider().refreshAll();
            });




            presenter.updateReplicateID(datasetIndex, replicateIndex, createReplicateID(replicateIndex));
            layout.addComponents(new HorizontalLayout(enrichedPart, normalPart), graphFileGrid);
            layout.setComponentAlignment(graphFileGrid, Alignment.BOTTOM_CENTER);
            setCompositionRoot(layout);

            //<-- DEBUG
            List<GraphFileBean> graphFileBeanList = new LinkedList<>();
            for (int i = 0; i < 10; i++) {
                GraphFileBean gfb = new GraphFileBean();
                gfb.setName("Test Graph File " + i);
                gfb.setCreationDate("01-01-01");
                gfb.setSizeInKB(42);
                graphFileBeanList.add(gfb);
            }
            graphFileGrid.setItems(graphFileBeanList);
            //DEBUG -->

        }

        private void setupDragSource(Grid<GraphFileBean> grid) {
            GridDragSource<GraphFileBean> dragSource = new GridDragSource<>(grid);
            dragSource.setEffectAllowed(EffectAllowed.MOVE);
            dragSource.addGridDragStartListener(event ->
                    setDraggedItemInGraphFileGrids((GraphFileBean) event.getDraggedItems().toArray()[0]));
        }

        private void setDraggedItemInGraphFileGrids(GraphFileBean draggedItem) {
            this.draggedItem = draggedItem;
            enrichedCoding.setDraggedItem(draggedItem);
            enrichedTemplate.setDraggedItem(draggedItem);
            normalCoding.setDraggedItem(draggedItem);
            normalTemplate.setDraggedItem(draggedItem);
        }

        public MyGraphFileGrid getEnrichedCoding() {
            return enrichedCoding;
        }

        public MyGraphFileGrid getEnrichedTemplate() {
            return enrichedTemplate;
        }

        public MyGraphFileGrid getNormalCoding() {
            return normalCoding;
        }

        public MyGraphFileGrid getNormalTemplate() {
            return normalTemplate;
        }
    }

    /**
     * Converts the numerical replicateIndex to an 'abc-value' as follows:
     * 0 -> a
     * 1 -> b
     * ...
     * 25 -> z
     * 26 -> aa
     * 27 -> ab
     * ...
     */
    String createReplicateID(int replicateIndex) {
        StringBuilder builder = new StringBuilder();
        while (replicateIndex >= 26) {
            builder.append((char) (97 + (replicateIndex % 26)));
            replicateIndex /= 26;
            replicateIndex--;
        }
        builder.append((char) (97 + replicateIndex));
        return builder.reverse().toString();

    }

    public ComboBox<Integer> getNumberOfDatasetsBox() {
        return numberOfDatasetsBox;
    }

    public ComboBox<Integer> getNumberOfReplicatesBox() {
        return numberOfReplicatesBox;
    }

    public TextField getGenomeNameField(int index) {

        return null;
    }

    public Accordion getDatasetAccordion() {
        return datasetAccordion;
    }

    public DatasetTab getDatasetTab(int index) {
        return (DatasetTab) datasetAccordion.getTab(index).getComponent();
    }

}
