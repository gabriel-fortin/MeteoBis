package com.example.habi.meteobis.service;

/**
 * Created by Gabriel Fortin
 */

import android.util.Log;

import com.example.habi.meteobis.model.ForecastModel;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.model.LocationParam;

import org.joda.time.DateTime;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func3;

public class ParamsMerger {
    private static final String TAG = ParamsMerger.class.getSimpleName();
    private static int instanceCount = 0;

    private static final Func3<DateTime, LocationParam, ForecastModel, FullParams>
            COMBINING_FUNCTION =
            (time, loc, model) -> new FullParams(loc.row, loc.col, time, model);

    /* output observable */
    private final Observable<FullParams> fullParamsObs;

    @Inject
    public ParamsMerger(Observable<DateTime> timeParam,
                        Observable<LocationParam> locationParam,
                        Observable<ForecastModel> forecastModelParam) {
        fullParamsObs = Observable
                .combineLatest(timeParam, locationParam, forecastModelParam, COMBINING_FUNCTION)
                .doOnNext(tp -> Log.v(TAG, "emitting: " + tp))
                // do source observables cache their emissions?
//                .cache(1)
                .replay(1).autoConnect()
                ;

        sanityCheck();
    }

    public Observable<FullParams> obtainParams() {
        Log.v(TAG, "obtainParams()");
        return fullParamsObs;
    }

    private static void sanityCheck() {
        instanceCount++;
        if (instanceCount > 1) {
            Log.w(TAG, "sanity: surely it's not a singleton now");
        }
    }

}
