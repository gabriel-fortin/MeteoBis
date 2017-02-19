package com.example.habi.meteobis.mvp;

/**
 * Created by Gabriel Fortin
 */

public interface Meteogram {

    /** A view showing a single meteogram */
    interface View {
        void meteogramLoading();
        void meteogramImage(byte[] imageBytes);
        void meteogramNotAvailable();
        void meteogramError(String text);
    }

    interface Presenter {
        /** Register to receive data and other commands */
        void attach(View itemView, int position);

        /** Unregister */
        void detach(View itemView);
    }

}
