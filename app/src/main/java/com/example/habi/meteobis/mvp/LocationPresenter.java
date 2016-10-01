package com.example.habi.meteobis.mvp;

import java.util.List;

/**
 * Created by Gabriel Fortin
 */

public interface LocationPresenter {

    class SomeItem {
        // TODO
    }

    /** The view allowing to change the current location for meteograms */
    interface LocationView {
        void setItems(List<SomeItem> items);
    }

    /** Method called by the view */
    void notifyLocationChanged(int locationIndex);
    // TODO: either handle location by indexes (as provided to the view) or change the param
    //       to obtain the location from the view (the former seems better - view has no logic)

}
