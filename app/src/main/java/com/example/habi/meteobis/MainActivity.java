package com.example.habi.meteobis;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.ReadableDuration;

public class MainActivity extends AppCompatActivity {

    // http://www.meteo.pl/um/php/meteorogram_list.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl&cname=Krak%F3w
    // http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int TOTAL_PAGES = 1000;
    private static DateTime newestDate;

    static {

        Log.i(TAG, "======== START =========");

        DateTime now = DateTime.now();
        int year = now.year().get();
        int month = now.monthOfYear().get();
        int day = now.dayOfMonth().get();
        int hour = (now.hourOfDay().get() / 6 - 1) * 6;
        String rounded = String.format("%d-%02d-%02dT%02d:00", year, month, day, hour);
        Log.i(TAG, "rounded time: " + rounded);
        newestDate = DateTime.parse(rounded);
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(TOTAL_PAGES - 1);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(TOTAL_PAGES - 1);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static  DateTime getNewestDate() {
        DateTime result = new DateTime(newestDate);
        Log.v(TAG, "newest date: " + result);
        return result;
    }

    public static String formatTime(DateTime dateTime) {
        int year = dateTime.year().get();
        int month = dateTime.monthOfYear().get();
        int day = dateTime.dayOfMonth().get();
        int hour = dateTime.hourOfDay().get();
        String result = String.format("%d%02d%02d%02d", year, month, day, hour);
        Log.v(TAG, "formatted time: " + result);
        return result;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int pageNum = getArguments().getInt(ARG_SECTION_NUMBER);

            int hoursToShift = 6 * (TOTAL_PAGES - pageNum);
//            DateTime dateTime = getNewestDate().minusHours(hoursToShift);
            DateTime dateTime = getNewestDate().minus(Period.hours(hoursToShift));
            String formattedTime = formatTime(dateTime);
            Log.d(TAG, "pageNum = " + pageNum + "    " + hoursToShift + "  =>  " + dateTime);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, pageNum) + "   " + formattedTime);

            ImageView img = (ImageView) rootView.findViewById(R.id.meteoImg);
            final String url = "http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&fdate=" + formattedTime + "&row=466&col=232&lang=pl";
            Log.v(TAG, "url:  " + url);
            Glide.with(this)
//            Picasso.with(getActivity())
                    .load(url)
                    .fitCenter()
//                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_help)
                    .crossFade()
                    .into(img);
//                    .into(img, new Callback() {
//                        @Override public void onSuccess() { Log.i(TAG, "img loading: success   url: " + url); }
//                        @Override public void onError() { Log.w(TAG, "img loading: error   url: " + url); }
//                    });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.v(TAG, "getItem: " + position);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
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
}
