package com.example.habi.meteobis.mvp;

import java.util.List;

/**
 * Created by Gabriel Fortin
 */

public interface LocationPresenter {

    /** The view allowing to change the current location for meteograms */
    interface LocView {
        void setData(List<LocItem> locations);
    }

    /** Notify presenter of an item being selected */
    // TODO: choose one of the following two versions:
    void onLocationSelected(LocItem e);
    void onLocationSelected(long id);

    /** Register to receive data and other commands */
    void attach(LocView view);

    /** Unregister */
    void detach(LocView view);


    class LocItem {
        int row;
        int col;

        // unique; used to identify entries
        long id;

        // displayed name
        String name;
    }


    // note: either handle location by indexes (as provided to the view) or change the param
    //       to obtain the location from the view (the former seems better - view has no logic)

}
