package com.example.habi.meteobis.main;

import android.animation.ObjectAnimator;
import android.support.annotation.InterpolatorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by Gabriel Fortin
 */
class FabVisibilityChanger implements ViewPager.OnPageChangeListener {
    private static final String TAG = FabVisibilityChanger.class.getSimpleName();
    private final FloatingActionButton fab;
    private final Changer changer;
    private static float dist = 400f;


    public FabVisibilityChanger(FloatingActionButton fab) {
        this(fab, defaultChanger());
    }

    public FabVisibilityChanger(FloatingActionButton fab, Changer changer) {
        this.fab= fab;
        this.changer = changer;
        fab.postDelayed(() -> fab.setVisibility(View.VISIBLE), 100);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.v(TAG, "selected page: " + position);
        if (position <= Config.TOTAL_PAGES - 2) {
            changer.show(fab);
        } else {
            changer.hide(fab);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public interface Changer {
        void show(FloatingActionButton fab);
        void hide(FloatingActionButton fab);
    }

    private static Changer defaultChanger() {
        return new Changer() {
            @Override
            public void show(FloatingActionButton fab) {
                fab.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(fab, "translationX", 0f);
                @InterpolatorRes int interpolator = android.R.interpolator.decelerate_cubic;
                anim.setInterpolator(AnimationUtils.loadInterpolator(fab.getContext(), interpolator));
                anim.start();
            }

            @Override
            public void hide(FloatingActionButton fab) {
                // TODO: change 'dist' to something more reliable than '400f'
//                int dist = mViewPager.getWidth();
//                int dist = 400f;

                ObjectAnimator anim = ObjectAnimator.ofFloat(fab, "translationX", dist);
                @InterpolatorRes int interpolator = android.R.interpolator.accelerate_decelerate;
                anim.setInterpolator(AnimationUtils.loadInterpolator(fab.getContext(), interpolator));
                anim.start();
            }
        };
    }
}
