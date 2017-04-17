package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.DataManager;
import com.example.habi.meteobis.SimpleDataManager;
import com.example.habi.meteobis.model.ForecastModel;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.model.LocationParam;
import com.example.habi.meteobis.meteogram.IndividualPagePresenter;
import com.example.habi.meteobis.mvp.Meteogram;
import com.example.habi.meteobis.service.ParamsMerger;
import com.example.habi.meteobis.service.TimeService;

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
    Meteogram.Presenter provideMeteogramPresenter(Observable<FullParams> fp, DataManager dm) {
        return new IndividualPagePresenter(fp, dm);
    }

    @Provides
    @Singleton
    Observable<FullParams> provideParamsMerger(
            Observable<DateTime> time,
            Observable<LocationParam> locationParams,
            Observable<ForecastModel> forecastModel) {

        return new ParamsMerger(time, locationParams, forecastModel).obtainParams();
    }

    @Provides
    @Singleton
    Observable<DateTime> provideTimeSticks(DataManager dm) {
        return dm.obtainForecastModel()
                // extract interval value
                .map(fm -> fm.interval)
                // do not emit a new 'DateTime' if the interval did not change
                .distinctUntilChanged()
                // if interval changes then switch to a new Observable from TimeService
                .switchMap(interval ->
                        TimeService.getCurrentTimeStick(interval, HOURS))
                ;
    }

    @Provides
    @Singleton
    Observable<ForecastModel> provideForecastModel() {
        // TODO: implement forecast model changes
        return Observable.just(ForecastModel.UM);
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return new SimpleDataManager();
    }

}
