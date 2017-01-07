package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.location.LocationConsumer;
import com.example.habi.meteobis.model.LocationParam;
import com.example.habi.meteobis.service.LocationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Observable;

/**
 * Created by Gabriel Fortin
 */

@Module
public class LocationModule {

    @Provides
    @Singleton
    LocationService provideLocationService() {
        return new LocationService();
    }

    @Provides
    Observable<LocationParam> provideLocationParams(LocationService locService) {
        // TODO: delete defaulting location to "Kraków" when UI for locations is ready
        locService.getConsumer().consume(LocationParam.KRAKOW);

        return locService.getObservable();
    }

    @Provides
    LocationConsumer consumeLocationParams(LocationService locService) {
        return locService.getConsumer();
    }
}
