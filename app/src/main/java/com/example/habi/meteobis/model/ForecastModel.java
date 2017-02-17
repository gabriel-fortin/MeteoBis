package com.example.habi.meteobis.model;

/**
 * Created by Gabriel Fortin
 */

public enum ForecastModel {

	UM(6),
	COAMPS(8),
	;

	public final int interval;

	ForecastModel(int interval) {
		this.interval = interval;
	}
}
