package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.model.LocationParam;
import com.example.habi.meteobis.meteogram.IndividualPagePresenter;
import com.example.habi.meteobis.mvp.MeteogramPresenter;
import com.example.habi.meteobis.service.ConfiguredUmService;
import com.example.habi.meteobis.service.TimeService;
import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

import org.joda.time.DateTime;

import javax.inject.Singleton;

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
    MeteogramPresenter provideMeteogramPresenter(ConfiguredUmService cs) {
        return new IndividualPagePresenter(cs);
    }

    @Provides
    @Singleton
    ConfiguredUmService provideConfiguredUmService(
            UmMeteogramRetrofitService umService,
            Observable<DateTime> time,
            Observable<LocationParam> locationParams) {

        int interval = 6;
        return new ConfiguredUmService(umService, time, locationParams, interval);
    }

    @Provides
    @Singleton
    Observable<DateTime> provideTimeSticks() {
        return TimeService.getCurrentTimeStick(6, HOURS);
    }

}
