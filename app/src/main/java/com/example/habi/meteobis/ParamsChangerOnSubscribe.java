package com.example.habi.meteobis;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

public class ParamsChangerOnSubscribe implements Observable.OnSubscribe<ImgData> {

    private final Set<Subscriber<? super ImgData>> subscribers = new HashSet<>();
    private ImgData lastData = null;

    @Override
    public void call(Subscriber<? super ImgData> subscriber) {
        subscribers.add(subscriber);
        if (lastData != null) {
            subscriber.onNext(lastData);
        }
    }

    public ParamsChangerOnSubscribe updateData(ImgData data) {
        lastData = data;
        if (data == null) return this;

        for (Subscriber<? super ImgData> sub : subscribers) {
            if (sub.isUnsubscribed()) {
                subscribers.remove(sub);
                continue;
            }
            sub.onNext(lastData);
        }
        return this;
    }
}
