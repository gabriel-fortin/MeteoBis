package com.example.habi.meteobis.network;

import android.util.Log;

import com.example.habi.meteobis.Util;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Gabriel Fortin
 */
public class TimeService {
    private static final String TAG = TimeService.class.getSimpleName();

    /** Emits the first item immediately upon subscribing */
    public static Observable<DateTime> getCurrentTimeStick(int interval, TimeUnit timeUnit) {
        if (timeUnit != TimeUnit.HOURS) {
            String msg = "only 'HOUR' time unit is supported";
            Log.e(TAG, msg);
            return Observable.error(new IllegalArgumentException(msg));
        }

        DateTime beginningTime = Util.round(DateTime.now(), interval);

        // TODO: implement time updating every 'interval' hours
        //       (can use e.g. 'doOnNext' to publish the next time-item using 'onNext'
        //        and adding 'delay' so it is observed in appropriate time)

        return BehaviorSubject.create(beginningTime);

    }
}
