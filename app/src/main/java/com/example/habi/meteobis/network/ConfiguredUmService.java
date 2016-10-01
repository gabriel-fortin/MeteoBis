package com.example.habi.meteobis.network;

/**
 * Created by Gabriel Fortin
 */

import android.util.Log;

import com.example.habi.meteobis.Util;
import com.example.habi.meteobis.model.LocationRequestParams;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;

/** Wraps an UmMeteogramService
 *  Takes care of data and location params – observes their changes
 *  Provides an API in which only the positions is needed
 *      (position := ordinal number of a meteogram in chronological order, where current is 0)
 */
public class ConfiguredUmService {
    public static final String TAG = ConfiguredUmService.class.getSimpleName();

    // TODO: ensure that all observables are unsubscribed when no longer needed

    private final UmMeteogramService umService;

    // input observables
    private final Observable<Integer> interval;
    private final Observable<DateTime> timeObs;
    private final Observable<LocationRequestParams> locationObs;

    //output observable
    private final Observable<ThreeParams> threeParamsObs;

    @Inject
    public ConfiguredUmService(UmMeteogramService umService,
                               Observable<DateTime> time,
                               Observable<LocationRequestParams> params,
                               int interval) {
        this.umService = umService;
        this.timeObs = time;
        this.locationObs = params;
        this.interval = Observable.just(interval).cache(1);

        threeParamsObs = Observable.combineLatest(
                timeObs, locationObs, (t, p) -> new ThreeParams(p.row, p.col, t))
                .doOnNext(tp -> Log.d(TAG, "emitting TP: " + tp))
//                .cache(1)  // so every subscriber obtains the first item immediately
                .replay(1).autoConnect()
//                .publish()
//                .refCount()???
//                .doOnUnsubscribe(() -> { threeParamsObs. })
                ;

        // TODO: make possible to GC this object (unsubscribe? something else?)

    }

    public Observable<ThreeParams> get() {
        Log.d(TAG, "get()");
        return threeParamsObs
                .doOnEach(notification -> Log.v(TAG, "TP-obs → " + notification.toString()))
                ;
    }

    public Observable<byte[]> get(int position) {
        Log.d(TAG, String.format(Locale.UK, "get(%d)", position));
        if (position > 0) {
            String msg = "position should be non-positive";
            RuntimeException problem = new IllegalArgumentException(msg);
//            return Observable.error(problem);
            throw problem;
        }

        return threeParamsObs
                .flatMap(tp -> {
                    int interval = this.interval.take(1).toBlocking().first();
                    Period timeAdjustment = Period.hours(-position * interval);
                    DateTime adjustedDate = tp.date.minus(timeAdjustment);
                    String formattedDate = Util.formatTime(adjustedDate);
                    return umService.getByDate(formattedDate, tp.col, tp.row);
                })
                .doOnEach(notif -> Log.v(TAG, "TP-obs(" + position + ") → " + notif.toString()))
                ;
    }


    // TODO: extract and rename class
    public static class ThreeParams {
        int row;
        int col;
        DateTime date;
        int interval;
        String baseUrl;  // OR different meteogram service

        ThreeParams() {}

        ThreeParams(ThreeParams tp) {
            this.row = tp.row;
            this.col = tp.col;
            this.date = tp.date;
            this.interval = tp.interval;
            this.baseUrl = tp.baseUrl;
        }

        ThreeParams(int row, int col, DateTime date) {
            this.row = row;
            this.col = col;
            this.date = date;
        }

        @Override
        public String toString() {
            return String.format(Locale.UK, "(%d, %d, %s)", row, col, date.toString());
        }
    }
}
