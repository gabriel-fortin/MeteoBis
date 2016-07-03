package com.example.habi.meteobis;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class MeteogramsPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = MeteogramsPagerAdapter.class.getSimpleName();

    public MeteogramsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.v(TAG, "getItem: " + position);
        // getItem is called to instantiate the fragment for the given page.
        // Return a MeteogramFragment (defined as a static inner class below).
        return MainActivity.MeteogramFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return MainActivity.TOTAL_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO: implement this method correctly
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }
}
