package com.example.habi.meteobis.meteogram;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.example.habi.meteobis.main.Config;

/**
 * Created by Gabriel Fortin
 */
public class MeteoViewPager extends ViewPager {
    public MeteoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO: move margin to dimens and adjust with linked margins and paddings
        setPageMargin(-60);
        setOffscreenPageLimit(1);  // may want to change because of the margin
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        setCurrentItem(Config.TOTAL_PAGES - 1);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
        listener.onPageSelected(getCurrentItem());
    }
}
