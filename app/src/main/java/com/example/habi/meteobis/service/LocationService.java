package com.example.habi.meteobis.service;

import com.example.habi.meteobis.location.LocationConsumer;
import com.example.habi.meteobis.model.LocationParam;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Gabriel Fortin
 */

public class LocationService {
    private final BehaviorSubject<LocationParam> behSub;
    private final LocationConsumer consumer;

    public LocationService() {
        behSub = BehaviorSubject.create();
        consumer = behSub::onNext;
    }

    /** This observable emits an item upon subscribing to it. Unless it contains none */
    public Observable<LocationParam> getObservable() {
        return behSub;
    }

    public LocationConsumer getConsumer() {
        return consumer;
    }
}
