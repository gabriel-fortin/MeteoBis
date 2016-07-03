package com.example.habi.meteobis;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

public class ParamsChangerOnSubscribe implements Observable.OnSubscribe<RequestParams> {

    private final Set<Subscriber<? super RequestParams>> subscribers = new HashSet<>();
    private RequestParams lastData = null;

    @Override
    public void call(Subscriber<? super RequestParams> subscriber) {
        subscribers.add(subscriber);
        if (lastData != null) {
            subscriber.onNext(lastData);
        }
    }

    public ParamsChangerOnSubscribe updateData(RequestParams data) {
        lastData = data;
        if (data == null) return this;

        for (Subscriber<? super RequestParams> sub : subscribers) {
            if (sub.isUnsubscribed()) {
                subscribers.remove(sub);
                continue;
            }
            sub.onNext(lastData);
        }
        return this;
    }
}
