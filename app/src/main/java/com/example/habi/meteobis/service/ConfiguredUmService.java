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
    private static int instanceCount = 0;

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
                .doOnNext(tp -> Log.v(TAG, "emitting: " + tp))
                // do source observables cache their emissions?
//                .cache(1)
                .replay(1).autoConnect()
                ;

        sanityCheck();
    }

    public Observable<FullParams> get() {
        Log.v(TAG, "get()");
        return fullParamsObs
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
                    Log.v(TAG, String.format("will request for params: %s %d %d",
                            formattedDate, tp.row, tp.col));
                    return umService
                            .getByDate(formattedDate, tp.col, tp.row)
                            // prevent from downloading on UI thread
                            .subscribeOn(Schedulers.io());
                })
                .doOnEach(notif ->
                        Log.v(TAG, "FP-obs(" + position + ") → byte array " + notif.toString()))
                ;
    }

    private static void sanityCheck() {
        instanceCount++;
        if (instanceCount > 1) {
            Log.w(TAG, "sanity: surely it's not a singleton now");
        }
    }


}
