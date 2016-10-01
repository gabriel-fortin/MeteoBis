package com.example.habi.meteobis.mvp;

/**
 * Created by Gabriel Fortin
 */
public interface MeteogramPresenter {

//    /** The view showing the actual meteogram images */
//    interface MeteogramView {
//        /** Tell the view that it needs to re-read data, reload, … */
//        void refresh();
//    }

    /** A view showing a single meteogram */
    interface MeteogramItemView {
        void meteogramLoading();
        void meteogramImage(byte[] imageBytes);
        void meteogramNotAvailable();
        void meteogramError(String text);
    }

    /** Register to receive data and other commands */
    void attach(MeteogramItemView itemView, int position);

    /** Unregister */
    void detach(MeteogramItemView itemView);

//    /**
//     * @param position chronological position where 'now' (or 'current') is represented by 0
//     *                 and previous values are negative numbers
//     * @return image bytes for the requested position
//     */
//    byte[] imageFor(int position);

        // TODO: make the view change its content to match the new location
}
