package com.example.habi.meteobis.meteogram;

import android.util.Log;

import com.example.habi.meteobis.ParamsProvider;
import com.example.habi.meteobis.main.Util;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.mvp.Meteogram;
import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

import org.joda.time.DateTime;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Gabriel Fortin
 */

public class IndividualPagePresenter implements Meteogram.Presenter {
    private String TAG = IndividualPagePresenter.class.getSimpleName();

    /* state */
    private Meteogram.View view;
    private int pos;

    /* providers */
    private final ParamsProvider paramsProvider;
    private final UmMeteogramRetrofitService umService;

    /* subscriptions */
    private Subscription paramsSubscription;
    private Subscription lastDataSubscription;

    @Inject
    public IndividualPagePresenter(ParamsProvider paramsProvider, UmMeteogramRetrofitService umService) {
        this.paramsProvider = paramsProvider;
        this.umService = umService;
    }

    @Override
    public void attach(Meteogram.View incomingView, int position) {
        TAG = String.format(Locale.UK,
                "%s#%s(%d)",
                IndividualPagePresenter.class.getSimpleName(),
                toString().substring(toString().length()-4),
                position);
        Log.d(TAG, "attach()");

        if (view != null) {
            Log.w(TAG, "another view still attached! Its position: " + position);
            detach(view);
        }

        if (incomingView == null) {
            Log.e(TAG, "ItemView provided in 'attach' cannot be null");
            return;
        }

        /* set state */
        view = incomingView;
        pos = position;

        /* update view */
        view.meteogramLoading();

        /* begin obtaining data */
        startObserving();
    }

    private void startObserving() {
        paramsSubscription = paramsProvider.obtainParams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FullParams>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "'onCompleted' where the stream is expected to last forever");
                        String msg = "observable from ParamsProvider completed";

                        view.meteogramError(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(TAG, "'onError': " + e);
                        e.printStackTrace();
                        String msg = "observable from ParamsProvider erred: " + e;

                        view.meteogramError(msg);
                    }

                    @Override
                    public void onNext(FullParams params) {
                        Log.d(TAG, "onNext: " + params + " Will retrieve image...");
                        String formattedDate = getFormattedDate(params, pos);

                        view.meteogramLoading();

                        /* loose interest in the previous image retrieval */
                        if (lastDataSubscription != null) {
                            lastDataSubscription.unsubscribe();
                        }

                        lastDataSubscription = umService
                                .getByDate(formattedDate, params.col, params.row)
                                // prevent from downloading on UI thread
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        (byte[] image) -> {
                                            if (isValid(image)) {
                                                Log.d(TAG, "got image");
                                                view.meteogramImage(image);
                                            } else {
                                                Log.d(TAG, "got invalid image");
                                                view.meteogramNotAvailable();
                                            }
                                        },
                                        thr -> {
                                            Log.w(TAG, "Image retrieval error: " + thr.getMessage());
                                            view.meteogramError(thr.getMessage());
                                        }
                                );
                    }
                });
    }

    @Override
    public void detach(Meteogram.View aView) {  // TODO: może usunąć argument? po co on?
        Log.d(TAG, "detach()");
        if (aView != view) throw new RuntimeException("trying to detach a wrong view");

        /* unsubscribe */
        if (lastDataSubscription != null) {
            lastDataSubscription.unsubscribe();
        }
        paramsSubscription.unsubscribe();

        /* reset view state */
        view.meteogramNotAvailable();

        /* clean references */
        lastDataSubscription = null;
        paramsSubscription = null;
        view = null;
    }

    private String getFormattedDate(FullParams params, int pos) {
        // TODO: this logic should be a technical detail of a networking class
        DateTime adjustedDate = Util.calculateShiftedDate(params, pos);
        String formattedDate = Util.formatTime(adjustedDate);

        Log.v(TAG, String.format("will request for params: %s %d %d",
                formattedDate,
                params.row,
                params.col));

        return formattedDate;
    }

    private static boolean isValid(byte[] image) {
        // if an image is not yet ready then something very small is returned
        return image.length > 1000;
    }

}
