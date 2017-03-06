package com.example.habi.meteobis;

import com.example.habi.meteobis.main.Config;
import com.example.habi.meteobis.main.TimeUtils;
import com.example.habi.meteobis.model.ForecastModel;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.model.LocationParam;
import com.example.habi.meteobis.network.ByteArrayConverterFactory;
import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

import org.joda.time.DateTime;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Gabriel Fortin
 */

public class SimpleDataManager implements DataManager {
	private static final String TAG = SimpleDataManager.class.getSimpleName();

	// TODO: (when persistence implemented) get rid of default value for forecast model
	private BehaviorSubject<ForecastModel> forecastModelStream = BehaviorSubject.create(ForecastModel.UM);

	private BehaviorSubject<LocationParam> locationStream = BehaviorSubject.create();

	private BehaviorSubject<DateTime> timeStream = BehaviorSubject.create();

	private final UmMeteogramRetrofitService umMeteogramRetrofitService;


	public SimpleDataManager() {
		umMeteogramRetrofitService = new Retrofit.Builder()
				.baseUrl(Config.BASE_URL)
				.addConverterFactory(new ByteArrayConverterFactory())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.build()
				.create(UmMeteogramRetrofitService.class);
	}


	@Override
	public Observable<ForecastModel> obtainForecastModel() {
		return forecastModelStream;
	}

	@Override
	public void putForecastModel(ForecastModel model) {
		if (model == null) throw new AssertionError();
		forecastModelStream.onNext(model);
	}

	@Override
	public Observable<LocationParam> obtainLocation() {
		return locationStream;
	}

	@Override
	public void putLocation(LocationParam location) {
		if (location == null) throw new AssertionError();
		locationStream.onNext(location);
	}

	@Override
	public Observable<DateTime> obtainTime() {
		return timeStream;
	}

	@Override
	public void plantTime(Observable<DateTime> time) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void putTime(DateTime time) {
		if (time == null) throw new AssertionError();
		timeStream.onNext(time);
	}

	@Override
	public Observable<byte[]> getImage(FullParams params) {
		if (params == null) throw new AssertionError();
		if (params.model != ForecastModel.UM) {
			throw new RuntimeException("Only UM model supported by this class");
		}

		String date = TimeUtils.formatTime(params.date);
		return umMeteogramRetrofitService
				.getByDate(date, params.col, params.row);
	}
}
