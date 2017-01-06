package com.example.habi.meteobis.service;

import android.util.Log;

import com.example.habi.meteobis.location.LocationConsumer;
import com.example.habi.meteobis.model.LocationParam;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Gabriel Fortin
 */

public class LocationService {
    private static final String TAG = LocationService.class.getSimpleName();
    private static int instanceCount = 0;


    private final BehaviorSubject<LocationParam> behSub;

    private final LocationConsumer consumer;
    private final Observable<LocationParam> producer;


    public LocationService() {
        behSub = BehaviorSubject.create();
        consumer = behSub::onNext;
        producer = attachLog(behSub);

        sanityCheck();
    }

    /** This observable emits an item (if it has one) immediately upon subscribing */
    public Observable<LocationParam> getObservable() {
        return producer;
    }

    public LocationConsumer getConsumer() {
        return consumer;
    }

    private static Observable<LocationParam> attachLog(Observable<LocationParam> arg) {
        return arg.doOnEach(notif -> Log.v(TAG,
                String.format(
                        "%s: %s",
                        notif.getKind(),
                        notif.hasValue() ? notif.getValue() : "--"
                )));
    }

    private static void sanityCheck() {
        instanceCount++;
        if (instanceCount > 1) {
            Log.w(TAG, "sanity: surely it's not a singleton now");
        }
    }
}
