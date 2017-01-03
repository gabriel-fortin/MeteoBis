package com.example.habi.meteobis.network;

import android.util.Log;

import com.example.habi.meteobis.main.Util;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.model.LocationParams;
import com.example.habi.meteobis.service.ConfiguredUmService;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Gabriel Fortin
 */
public class ConfiguredUmServiceTest {
    public static final String TAG = ConfiguredUmServiceTest.class.getSimpleName();

    @Before
    public void setUp() {
//        DaggerMeteogramComponent
//                .create()
//                .inject(this);
    }

    @Test
    public void get() throws Exception {
        UmMeteogramRetrofitService umMeteogramService = mock(UmMeteogramRetrofitService.class);
        DateTime someTime = DateTime.now();
        Observable<DateTime> time = Observable.just(someTime).cache(1);
        BehaviorSubject<LocationParams> locationParams = BehaviorSubject.create();
        final int interval = 6;

        Queue<FullParams> expectedValues = new LinkedList<>(Arrays.asList(
                new FullParams(102, 302, someTime),
                new FullParams(103, 303, someTime)));

        ConfiguredUmService confUmService
                = new ConfiguredUmService(umMeteogramService, time, locationParams, interval);

        locationParams.onNext(new LocationParams(101, 301));
        locationParams.onNext(new LocationParams(102, 302));

        Log.d(TAG, "will 'get()'");
        Subscription subscription = confUmService.get()
                .subscribe(
                        item -> {
                            Log.d(TAG, "onNext: " + item);
                            assertThat(item, is(equalTo(expectedValues.poll())));
                        },
                        thr -> {
                            Log.d(TAG, "onError: " + thr);
                            fail();
                        },
                        () -> {
                            Log.d(TAG, "onCompleted");
                            assertThat(expectedValues.size(), is(0));
                        }
                );

        locationParams.onNext(new LocationParams(103, 303));

        Log.d(TAG, "subscribed: " + subscription.isUnsubscribed());

    }

    @Inject
    ConfiguredUmService injected_confUmService;

    int onNextCounter;

//    @Test
    public void get_injected() throws Exception {
        onNextCounter = 0;
        FullParams expectedVal
                = new FullParams(466, 232, Util.round(DateTime.now(), 6));
//        DaggerMeteogramComponent
//                .builder()
//                .build()
//                .inject(this);

        Log.d(TAG, "will 'get()'");
        Subscription subscription = injected_confUmService.get()
                .subscribe(
                        item -> {
                            Log.d(TAG, "onNext: " + item);
                            assertThat(item.row, is(equalTo(expectedVal.row)));
                            assertThat(item.col, is(equalTo(expectedVal.col)));
                            assertTrue(item.date.isEqual(expectedVal.date));
//                            assertThat(item, is(equalTo(expectedVal)));
                            onNextCounter++;
                        },
                        thr -> {
                            Log.d(TAG, "onError: " + thr);
                            fail();
                        },
                        () -> {
                            Log.d(TAG, "onCompleted");
                            assertThat(onNextCounter, is(1));
                        }
                );
    }

}
