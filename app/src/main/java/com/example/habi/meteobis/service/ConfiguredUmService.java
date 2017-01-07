package com.example.habi.meteobis.service;

/**
 * Created by Gabriel Fortin
 */

import android.util.Log;

import com.example.habi.meteobis.main.Util;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.model.LocationParam;
import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/** Wraps an UmMeteogramRetrofitService
 *  Takes care of data and location params – observes their changes
 *  Provides an API in which only the positions is needed
 *      (position := ordinal number of a meteogram in chronological order, where current is 0)
 */
public class ConfiguredUmService {
    private static final String TAG = ConfiguredUmService.class.getSimpleName();

    // TODO: ensure that all observables are unsubscribed when no longer needed

    private final UmMeteogramRetrofitService umService;

    // input observables
    private final Observable<Integer> interval;
    private final Observable<DateTime> timeObs;
    private final Observable<LocationParam> locationObs;

    //output observable
    private final Observable<FullParams> fullParamsObs;

    @Inject
    public ConfiguredUmService(UmMeteogramRetrofitService umService,
                               Observable<DateTime> timeParam,
                               Observable<LocationParam> locationParam,
                               int intervalParam) {
        this.umService = umService;
        this.timeObs = timeParam;
        this.locationObs = locationParam;
        this.interval = Observable.just(intervalParam).cache(1);
            // TODO: either use a plain 'int' or accept an 'Observable' as param

        final Func2<DateTime, LocationParam, FullParams> combiningFunction
                = (time, loc) -> new FullParams(loc.row, loc.col, time);

        fullParamsObs = Observable
                .combineLatest(timeObs, locationObs, combiningFunction)
                .doOnNext(tp -> Log.v(TAG, "emitting TP: " + tp))
                // no need to cache as source observables do it already
//                .cache(1)
                ;

        // TODO: make possible to GC this object (unsubscribe? something else?)

    }

    public Observable<FullParams> get() {
        Log.v(TAG, "get()");
        return fullParamsObs
                .doOnEach(notif -> Log.v(TAG, "FP-obs → " + notif.toString()))
                ;
    }

    public Observable<byte[]> get(int position) {
        Log.v(TAG, String.format(Locale.UK, "get(%d)", position));
        if (position > 0) {
            String msg = "position should be non-positive";
            RuntimeException problem = new IllegalArgumentException(msg);
            return Observable.error(problem);
        }

        return fullParamsObs
                .flatMap(tp -> {
                    int interval = this.interval.take(1).toBlocking().first();
                    Period timeAdjustment = Period.hours(-position * interval);
                    DateTime adjustedDate = tp.date.minus(timeAdjustment);
                    String formattedDate = Util.formatTime(adjustedDate);
                    return umService
                            .getByDate(formattedDate, tp.col, tp.row)
                            // prevent from downloading on UI thread
                            .subscribeOn(Schedulers.io());
                })
                .doOnEach(notif ->
                        Log.v(TAG, "FP-obs(" + position + ") → " + notif.toString()))
                ;
    }


}
