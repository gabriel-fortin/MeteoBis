package com.example.habi.meteobis.mvp;


import android.util.Log;

import com.example.habi.meteobis.network.ConfiguredUmService;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Gabriel Fortin
 */

public class IndividualPagePresenter implements MeteogramPresenter {
    private String TAG = IndividualPagePresenter.class.getSimpleName();

    private MeteogramItemView view;
    private int pos;

    private final ConfiguredUmService configuredService;

    private Subscription subscription;

    @Inject
    public IndividualPagePresenter(ConfiguredUmService cs) {
        this.configuredService = cs;
    }

    @Override
    public void attach(MeteogramItemView itemView, int position) {
        TAG = String.format(Locale.UK,
                "%s#%s(%d)",
                IndividualPagePresenter.class.getSimpleName(),
                toString().substring(toString().length()-2),
                position);
        Log.d(TAG, "attach()");

        if (view != null) {
            Log.w(TAG, "another view still attached! Its position: " + position);
            detach(view);
        }

        view = itemView;
        pos = position;

        view.meteogramLoading();

        subscription = Observable
                .merge(configuredService.get(), configuredService.get(pos))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                        String msg = "observable from ConfiguredUmService completed";
                        view.meteogramError(msg);
//                        throw new RuntimeException(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError");
                        String msg = "observable from ConfiguredUmService erred: " + e;
                        view.meteogramError(msg);
//                        throw new RuntimeException(msg, e);
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onNext: " + o);
                        if (o instanceof ConfiguredUmService.ThreeParams) {
                            ConfiguredUmService.ThreeParams threeParams
                                    = (ConfiguredUmService.ThreeParams) o;
                            view.meteogramLoading();
                            return;
                        }

                        if (o instanceof byte[]) {
                            byte[] imageBytes = (byte[]) o;
                            if (imageBytes.length < 1000) {
                                view.meteogramNotAvailable();
                            } else {
                                view.meteogramImage(imageBytes);
                            }
                            return;
                        }

                        String msg = "unrecognised object in merged observable";
                        view.meteogramError(msg);
//                        throw new RuntimeException(msg);
                    }
                });


    }

    @Override
    public void detach(MeteogramItemView itemView) {  // TODO: może usunąć argument? po co on?
        Log.d(TAG, "detach(),  position = " + pos);
        if (itemView != view) throw new RuntimeException("trying to detach a wrong view");

        subscription.unsubscribe();
        view.meteogramLoading();

        // TODO: unsubscribe and the like

        view = null;
    }

}
