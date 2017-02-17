package com.example.habi.meteobis;

import com.example.habi.meteobis.model.FullParams;

import rx.Observable;

/**
 * Created by Gabriel Fortin
 */

public interface ParamsProvider {
	Observable<FullParams> obtainParams();
}
