package view;

import view.tsswindows.TSSWindow;

import java.util.LinkedList;

/**
 * A TSSWindowChain holds a list of windows that should be displayed as a sequence.
 * The required logic is implemented in TSSWindowChainPresenter
 */
public class TSSWindowChain {
    private LinkedList<TSSWindow> windowList;

    public TSSWindowChain() {
        windowList = new LinkedList<>();
    }

    /**
     * Puts 'i/n' on every window's label, where 'i' is the index of the window and 'n' is the total window count
     */
    public void createInfoLabels() {
        for (int i = 0; i < windowList.size(); i++) {
            windowList.get(i).getInfoLabel().setValue(windowList.get(i).getInfoLabel().getValue()
            + " (" + (i+1) + "/" + windowList.size() + ")");
        }

    }

    public LinkedList<TSSWindow> getWindowList() {
        return windowList;
    }
}
