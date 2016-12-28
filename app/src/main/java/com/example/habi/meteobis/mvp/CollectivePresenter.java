package com.example.habi.meteobis.mvp;

import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Gabriel Fortin
 */

/** This class is currently not used. It's not even in a usable state. */
public class CollectivePresenter implements MeteogramPresenter {

    UmMeteogramRetrofitService umMeteogramService;

    List<Entry> views = new ArrayList<>();

    @Inject
    public CollectivePresenter(UmMeteogramRetrofitService umMeteogramService) {
        this.umMeteogramService = umMeteogramService;
    }

    @Override
    public void attach(ItemView itemView, int position) {
        views.add(new Entry(position, itemView));
        itemView.meteogramLoading();

//        umMeteogramService.getByDate()...


//        PublishSubject subject = PublishSubject.create();
//        subject.
    }

    @Override
    public void detach(ItemView itemView) {

    }

//    @Override
    public void notifyLocationChanged(int locationIndex) {

    }


    static class Entry {
        final int position;
        final ItemView view;

        public Entry(int position, ItemView view) {
            this.position = position;
            this.view = view;
        }
    }
}
