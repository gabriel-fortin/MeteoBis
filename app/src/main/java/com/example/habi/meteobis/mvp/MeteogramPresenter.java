package com.example.habi.meteobis.mvp;

/**
 * Created by Gabriel Fortin
 */
public interface MeteogramPresenter {

    /** A view showing a single meteogram */
    interface ItemView {
        void meteogramLoading();
        void meteogramImage(byte[] imageBytes);
        void meteogramNotAvailable();
        void meteogramError(String text);
    }

    /** Register to receive data and other commands */
    void attach(ItemView itemView, int position);

    /** Unregister */
    void detach(ItemView itemView);

}
