package presenter;

import view.tsswindows.TSSWindow;
import view.TSSWindowChain;

import java.util.LinkedList;

/**
 * This class manages events concerning the TSS Window chain
 */
public class TSSWindowChainPresenter {
    TSSWindowChain windowChain;

    public void setupWindowButtons() {
        LinkedList<TSSWindow> windowList = windowChain.getWindowList();
        for (int i = 0; i < windowList.size(); i++) {
            TSSWindow currentWindow = windowList.get(i);
            int finalI = i;
            if (i != 0) {
                currentWindow.getPreviousButton().addClickListener(e -> {
                    currentWindow.getUI().addWindow(windowList.get(finalI - 1));
                    currentWindow.close();
                });
            }else{
                currentWindow.getPreviousButton().setEnabled(false);
            }
            if (i != windowList.size() - 1) {
                currentWindow.getNextButton().addClickListener(e ->{
                    currentWindow.getUI().addWindow(windowList.get(finalI + 1));
                    currentWindow.close();
                });
            }else{
                currentWindow.getNextButton().setEnabled(false);
            }
        }
    }

    public void setWindowChain(TSSWindowChain windowChain) {
        this.windowChain = windowChain;
    }
}
