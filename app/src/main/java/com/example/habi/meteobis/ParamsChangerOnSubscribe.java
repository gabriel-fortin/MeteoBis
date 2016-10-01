package com.example.habi.meteobis;

import com.example.habi.meteobis.model.LocationRequestParams;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

public class ParamsChangerOnSubscribe implements Observable.OnSubscribe<LocationRequestParams> {

    private final Set<Subscriber<? super LocationRequestParams>> subscribers = new HashSet<>();
    private LocationRequestParams lastData = null;

    @Override
    public void call(Subscriber<? super LocationRequestParams> subscriber) {
        subscribers.add(subscriber);
        if (lastData != null) {
            subscriber.onNext(lastData);
        }
    }

    public ParamsChangerOnSubscribe updateData(LocationRequestParams data) {
        lastData = data;
        if (data == null) return this;

        for (Subscriber<? super LocationRequestParams> sub : subscribers) {
            if (sub.isUnsubscribed()) {
                subscribers.remove(sub);
                continue;
            }
            sub.onNext(lastData);
        }
        return this;
    }
}
