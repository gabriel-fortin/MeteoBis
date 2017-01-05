package com.example.habi.meteobis.location;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.habi.meteobis.model.LocationParams;
import com.example.habi.meteobis.mvp.LocationPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Gabriel Fortin
 */

public class SpinnerLocationPresenter implements LocationPresenter {
    public static final String TAG = SpinnerLocationPresenter.class.getSimpleName();
    public static final String LOCATIONS_LIST_ENDED = "there will be no more updates of the locations' list";

    private LocView view;
    private final Observable<List<LocItem>> locationsListObs;
    private final LocationConsumer locationConsumer;
    private Subscription dataSubscription;

    @Inject
    public SpinnerLocationPresenter(@NonNull Observable<List<LocItem>> data,
                                    @NonNull LocationConsumer locConsumer) {
        locationsListObs = data;
        locationConsumer = locConsumer;
    }

    @Override
    public void onLocationSelected(LocItem e) {
        // TODO: either keep this method and remove 'onLocationSelected(long)' or the inverse
        LocationParams newLocation = getLocationFrom(e);
        locationConsumer.consume(newLocation);
    }

    private LocationParams getLocationFrom(LocItem locItem) {
        // TODO: implement 'getLocationFrom(LocItem)'
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void onLocationSelected(long id) {
        // TODO: either keep this method and remove 'onLocationSelected(LocItem)' or the inverse
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void attach(@NonNull LocView pView) {
        if (pView == null) throw new NullPointerException();
        if (pView == view) {
            Log.w(TAG, "trying to re-attach the same view again; ignoring request");
            return;
        }
        if (view != null) {
            Log.w(TAG, "attaching while another view is already attached");
        }
        view = pView;
        realAttach();
    }

    @Override
    public void detach(@NonNull LocView pView) {
        if (pView == null) throw new NullPointerException();
        if (view == null) {
            Log.w(TAG, "trying to detach while no view is set; ignoring request");
            return;
        }
        if (view != pView) {
            Log.e(TAG, "trying to detach a view which is not attached; ignoring request");
            return;
        }

        realDetach();
        view = null;
    }

    private void realAttach() {
        dataSubscription = locationsListObs.subscribe(
                locItems -> view.setData(locItems),
                thr -> Log.e(TAG, "onError - " + LOCATIONS_LIST_ENDED),
                () -> Log.e(TAG, "onCompleted - " + LOCATIONS_LIST_ENDED)
        );

        throw new RuntimeException("NOT YET fully IMPLEMENTED?");
        // TODO: perform real stuff regarding attaching
    }

    private void realDetach() {
        dataSubscription.unsubscribe();

        throw new RuntimeException("NOT YET fully IMPLEMENTED?");
        // TODO: perform real stuff regarding detaching
    }
}


// TODO: 'PublishSubject' do  komunikowania lokalizacji

// TODO: do powyższego 'PublishSubject' wrzucać wybraną lokalizację
//       natomiast na początku – ostatnią poprzednio używaną lokalizację (SharedPrefs?)

// TODO: jeśli nic się nie pojawi w tym obserwablu (np. w ciągu 200ms?),
//       to wyświetlić dialog proszący o wybranie lokalizacji
