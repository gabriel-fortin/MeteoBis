package com.example.habi.meteobis;

import com.example.habi.meteobis.model.ForecastModel;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.model.LocationParam;

import org.joda.time.DateTime;

import rx.Observable;

/**
 * Created by Gabriel Fortin
 */

public interface DataManager {
	Observable<ForecastModel> obtainForecastModel();
	void putForecastModel(ForecastModel model);

	Observable<LocationParam> obtainLocation();
	void putLocation(LocationParam location);

	Observable<DateTime> obtainTime();
	void plantTime(Observable<DateTime> time);
	void putTime(DateTime time);

	byte[] getImage(FullParams params);
}
