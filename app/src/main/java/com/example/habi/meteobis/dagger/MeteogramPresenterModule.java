package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.model.LocationRequestParams;
import com.example.habi.meteobis.mvp.IndividualPagePresenter;
import com.example.habi.meteobis.mvp.MeteogramPresenter;
import com.example.habi.meteobis.network.ConfiguredUmService;
import com.example.habi.meteobis.network.TimeService;
import com.example.habi.meteobis.network.UmMeteogramService;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

import static java.util.concurrent.TimeUnit.HOURS;

/**
 * Created by Gabriel Fortin
 */

@Module
public class MeteogramPresenterModule {

    @Provides
    static MeteogramPresenter provideMeteogramPresenter(ConfiguredUmService cs) {
        return new IndividualPagePresenter(cs);
    }

    @Provides
    static ConfiguredUmService provideConfiguredUmService(UmMeteogramService umService) {
        Observable<DateTime> time = TimeService.getCurrentTimeStick(6, HOURS);
//        Observable<DateTime> time = Observable
//                .just(DateTime.now())
//                .cache(1);
        Observable<LocationRequestParams> locationParams = Observable
                .just(new LocationRequestParams(466, 232))
                .cache(1);
        int interval = 6;

        return new ConfiguredUmService(umService, time, locationParams, interval);
    }
}
