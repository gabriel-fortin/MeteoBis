package com.example.habi.meteobis.meteogram;

import android.util.Log;

import com.example.habi.meteobis.DataManager;
import com.example.habi.meteobis.main.TimeUtils;
import com.example.habi.meteobis.model.FullParams;
import com.example.habi.meteobis.mvp.Meteogram;

import org.joda.time.DateTime;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by Gabriel Fortin
 */

public class IndividualPagePresenter implements Meteogram.Presenter {
    private String TAG = IndividualPagePresenter.class.getSimpleName();

    /* state */
    private Meteogram.View view;
    private int pos;

    /* providers */
    private final DataManager dataManager;
    private final Observable<FullParams> paramsSource;

    /* subscriptions */
    private Subscription paramsSubscription;
    private Subscription lastDataSubscription;

    @Inject
    public IndividualPagePresenter(Observable<FullParams> paramsSource, DataManager dataManager) {
        this.paramsSource = paramsSource;
        this.dataManager = dataManager;
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

        /* prevent NPE */
        lastDataSubscription = Subscriptions.unsubscribed();

        /* update view */
        view.meteogramLoading();

        /* begin obtaining data */
        startObserving();
    }

    private void startObserving() {
        paramsSubscription = paramsSource
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

                        view.meteogramLoading();

                        /* loose interest in any previous image retrieval */
                        lastDataSubscription.unsubscribe();

                        DateTime shifted = TimeUtils.calculateShiftedDate(params, pos);
                        lastDataSubscription = dataManager.getImage(params.withDate(shifted))
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
        paramsSubscription.unsubscribe();
        lastDataSubscription.unsubscribe();

        /* clean references */
        lastDataSubscription = null;
        paramsSubscription = null;
        view = null;
    }

    private static boolean isValid(byte[] image) {
        // if an image is not yet ready then something very small is returned
        return image.length > 1000;
    }

}
