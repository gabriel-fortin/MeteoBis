package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.model.LocationParams;
import com.example.habi.meteobis.mvp.IndividualPagePresenter;
import com.example.habi.meteobis.mvp.MeteogramPresenter;
import com.example.habi.meteobis.network.ConfiguredUmService;
import com.example.habi.meteobis.network.TimeService;
import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

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

    @Provides
    static Observable<LocationParams> provideLocationParams() {
        return Observable
                .just(new LocationParams(466, 232))
                .cache(1)
                ;
    }
}
