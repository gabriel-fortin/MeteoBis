package com.example.habi.meteobis.service;

import android.util.Log;

import com.example.habi.meteobis.main.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Gabriel Fortin
 */
public class TimeService {
    private static final String TAG = TimeService.class.getSimpleName();
    private static int instanceCount = 0;

    /** Emits the first item immediately upon subscribing */
    public static Observable<DateTime> getCurrentTimeStick(int interval, TimeUnit timeUnit) {
        if (timeUnit != TimeUnit.HOURS) {
            String msg = "only 'HOUR' time unit is supported";
            Log.e(TAG, msg);
            return Observable.error(new IllegalArgumentException(msg));
        }

        sanityCheck();

        DateTime beginningTime = TimeUtils.round(DateTime.now(), interval);

        // TODO: implement time updating every 'interval' hours
        //       (can use e.g. 'doOnNext' to publish the next time-item using 'onNext'
        //        and adding 'delay' so it is observed in appropriate time)

        BehaviorSubject<DateTime> behSub = BehaviorSubject.create();
        Observable<DateTime> result = attachLog(behSub);
        behSub.onNext(beginningTime);
        return result;
    }

    private static Observable<DateTime> attachLog(Observable<DateTime> obs) {
        return obs.doOnEach(notif -> {
            DateTimeFormatter formatter = DateTimeFormat.shortDateTime();
            Log.v(TAG, String.format(
                    "%s: %s",
                    notif.getKind(),
                    notif.hasValue() ? formatter.print((DateTime) notif.getValue()) : "--"
            ));
        });
    }

    private static void sanityCheck() {
        instanceCount++;
        if (instanceCount > 1) {
            Log.w(TAG, "sanity: surely it's not a singleton now");
        }
    }
}
