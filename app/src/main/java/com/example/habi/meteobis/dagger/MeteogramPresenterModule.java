package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.location.LocationConsumer;
import com.example.habi.meteobis.model.LocationParams;
import com.example.habi.meteobis.meteogram.IndividualPagePresenter;
import com.example.habi.meteobis.mvp.MeteogramPresenter;
import com.example.habi.meteobis.service.ConfiguredUmService;
import com.example.habi.meteobis.service.LocationService;
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
    static MeteogramPresenter provideMeteogramPresenter(ConfiguredUmService cs) {
        return new IndividualPagePresenter(cs);
    }

    @Provides
    static ConfiguredUmService provideConfiguredUmService(
            UmMeteogramRetrofitService umService,
            Observable<DateTime> time,
            Observable<LocationParams> locationParams) {

        int interval = 6;
        return new ConfiguredUmService(umService, time, locationParams, interval);
    }

    @Provides
    static Observable<DateTime> provideTimeSticks() {
        return TimeService.getCurrentTimeStick(6, HOURS);
    }

    // TODO: move location related methods (the 3 one below) to a separate module

    @Provides
    static Observable<LocationParams> provideLocationParams(LocationService locService) {
        return locService.getObservable();
    }

    @Provides
    static LocationConsumer consumeLocationParams(LocationService locService) {
        return locService.getConsumer();
    }

    @Provides
    @Singleton
    static LocationService provideLocationService() {
        return new LocationService();
    }
}
