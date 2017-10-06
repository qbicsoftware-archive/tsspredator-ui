package view.tsswindows;

import com.vaadin.ui.*;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Fourth of four windows where the TSSPredator parameters are set.
 * Here, it's the allowed shifts, minimal number of matching replicates and the UTR lengths.
 * Additionally, the user decides if he wants to write the RNA-seq graphs.
 */
public class ParametersWindow4 extends TSSWindow {
    @Override
    void designContentLayout() {
        Layout contentLayout = new VerticalLayout();

        HorizontalLayout allowedShifts = new HorizontalLayout();
        //TODO: Change depending on user choice: Genomes vs. Conditions
        Slider crossGenomeOrConditionShift = new Slider("Allowed Cross-Condition Shift");
        crossGenomeOrConditionShift.setMin(0);
        crossGenomeOrConditionShift.setMax(100);
        Slider crossReplicationShift = new Slider("Allowed Cross-Replication Shift");
        crossReplicationShift.setMin(0);
        crossReplicationShift.setMax(100);
        allowedShifts.addComponents(crossGenomeOrConditionShift, crossReplicationShift);

        ComboBox<Integer> matchingReplicates = new ComboBox<>("Matching Replicates");
        //TODO: Is this the most elegant way to do this (cf. DataWindow.java)
        //TODO: Replace numReplicates with the actual number of replicates
        int numReplicates = 42;
        Collection<Integer> replicateList = new LinkedList<>();
        for (int i = 0; i < numReplicates; i++) {
            replicateList.add(i + 1);
        }
        matchingReplicates.setItems(replicateList);

        HorizontalLayout utrLengths = new HorizontalLayout();
        Slider utrLength = new Slider("UTR length");
        utrLength.setMin(0);
        utrLength.setMax(100);
        Slider antisenseUtrLength = new Slider("Antisense UTR length");
        antisenseUtrLength.setMin(0);
        antisenseUtrLength.setMax(100);
        utrLengths.addComponents(utrLength,antisenseUtrLength);

        CheckBox writeGraphs = new CheckBox("Write RNA-Seq graphs");


        contentLayout.addComponents(allowedShifts, matchingReplicates,utrLengths, writeGraphs);
        setContentLayout(contentLayout);
    }
}
