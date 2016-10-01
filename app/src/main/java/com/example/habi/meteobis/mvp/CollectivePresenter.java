package com.example.habi.meteobis.mvp;

import com.example.habi.meteobis.mvp.MeteogramPresenter;
import com.example.habi.meteobis.network.UmMeteogramService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Gabriel Fortin
 */

public class CollectivePresenter implements MeteogramPresenter {

    UmMeteogramService umMeteogramService;

    List<Entry> views = new ArrayList<>();

    @Inject
    public CollectivePresenter(UmMeteogramService umMeteogramService) {
        this.umMeteogramService = umMeteogramService;
    }

    @Override
    public void attach(MeteogramItemView itemView, int position) {
        views.add(new Entry(position, itemView));
        itemView.meteogramLoading();

//        umMeteogramService.getByDate()...


//        PublishSubject subject = PublishSubject.create();
//        subject.
    }

    @Override
    public void detach(MeteogramItemView itemView) {

    }

//    @Override
    public void notifyLocationChanged(int locationIndex) {

    }


    static class Entry {
        final int position;
        final MeteogramItemView view;

        public Entry(int position, MeteogramItemView view) {
            this.position = position;
            this.view = view;
        }
    }
}
