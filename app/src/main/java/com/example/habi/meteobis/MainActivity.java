package com.example.habi.meteobis;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    // http://www.meteo.pl/um/php/meteorogram_list.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl&cname=Krak%F3w
    // http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int TOTAL_PAGES = 1000;
    public static final String BASE_URL = "http://www.meteo.pl/";

    private static DateTime newestDate;

    public ParamsChangerOnSubscribe paramsChanger;

    public static Observable<RequestParams> paramsObservable;

    static {
        Log.i(TAG, "======== START =========");

        DateTime now = DateTime.now();
        newestDate = Util.round(now, 6);
    }

    {
        paramsChanger = new ParamsChangerOnSubscribe();
        paramsChanger.updateData(new RequestParams(466, 232));
        paramsObservable = Observable.create(paramsChanger);
    }

    private MeteogramsPagerAdapter meteogramsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareFab();

        meteogramsPagerAdapter = new MeteogramsPagerAdapter(getSupportFragmentManager());
        prepareViewPager();
    }

    private void prepareFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setTranslationX(400f);
        fab.setOnClickListener(view -> {
            mViewPager.setCurrentItem(TOTAL_PAGES - 1);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        });
    }

    private void prepareViewPager() {
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(meteogramsPagerAdapter);
        mViewPager.setCurrentItem(TOTAL_PAGES - 1);
        mViewPager.addOnPageChangeListener(new FabVisibilityChanger(fab));

        // TODO: move margin to dimens and adjust with linked margins and paddings
        mViewPager.setPageMargin(-60);
        mViewPager.setOffscreenPageLimit(1);  // may want to change because of the margin
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

    public static DateTime getNewestDate() {
        DateTime result = new DateTime(newestDate);
        Log.v(TAG, "newest date: " + result);
        return result;
    }

    public static String formatTime(DateTime dateTime) {
        int year = dateTime.year().get();
        int month = dateTime.monthOfYear().get();
        int day = dateTime.dayOfMonth().get();
        int hour = dateTime.hourOfDay().get();
        String result = String.format(Locale.UK, "%d%02d%02d%02d", year, month, day, hour);
        Log.v(TAG, "formatted time: " + result);
        return result;
    }

    public static class MeteogramFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        @Inject
        UmMeteogramService umMeteogramService;

        public MeteogramFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MeteogramFragment newInstance(int sectionNumber) {
            MeteogramFragment fragment = new MeteogramFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            DaggerAllDaggeryThingys
                    .builder()
                    .build()
                    .inject(this);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int pageNum = getArguments().getInt(ARG_SECTION_NUMBER);

            int hoursToShift = 6 * (TOTAL_PAGES - pageNum);
            DateTime dateTime = getNewestDate().minus(Period.hours(hoursToShift));
            final String formattedTime = formatTime(dateTime);
            Log.v(TAG, "pageNum = " + pageNum + "    " + hoursToShift + "  =>  " + dateTime);

            // show dev info (only visible in dev_noDex flavour
            TextView sectionLabel = (TextView) rootView.findViewById(R.id.section_label);
            sectionLabel.setText(getString(R.string.section_format, pageNum) + "   " + formattedTime);

            final ImageView img = (ImageView) rootView.findViewById(R.id.meteoImg);

            paramsObservable
                    .flatMap(reqParams -> umMeteogramService
                            .getByDate(formattedTime, reqParams.col, reqParams.row))
                    .subscribeOn(Schedulers.io())  // download on io thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imgBytes -> {
                        Log.d(TAG, "onNext -- bytes: " + imgBytes.length);
                        animateCardIn(img);

                        if (imgBytes.length < 1000) {
                            displayUnavailableImage(img);
                            return;
                        }

                        displayImageFromBytes(img, imgBytes);

                        // TODO: show progress indicator

                    }, error -> {
                        Log.w(TAG, "download error?  pageNum: " + pageNum);
                        Toast.makeText(getContext(), "img download error", Toast.LENGTH_SHORT).show();
                    });

            return rootView;
        }

        private void animateCardIn(ImageView img) {
            ViewParent card = img.getParent();
            int interpolator = android.R.interpolator.linear;

            ObjectAnimator anim = ObjectAnimator.ofFloat(card, "alpha", 1f);
            anim.setInterpolator(AnimationUtils.loadInterpolator(getContext(), interpolator));
            anim.start();
        }

        private static void displayUnavailableImage(ImageView img) {
            img.setImageResource(android.R.drawable.ic_delete);

            CardView card = (CardView) img.getParent();
            ViewGroup.LayoutParams cardLP = card.getLayoutParams();

            card.setBackgroundColor(Color.TRANSPARENT);
            cardLP.height = 300;  // for vertical positioning
        }

        private static void displayImageFromBytes(ImageView img, byte[] imgBytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            img.setImageBitmap(bitmap);

            int width = img.getWidth();
            int intrinsicWidth = img.getDrawable().getIntrinsicWidth();
            int intrinsicHeight = img.getDrawable().getIntrinsicHeight();

            CardView card = (CardView) img.getParent();
            ViewGroup.LayoutParams cardLP = card.getLayoutParams();

            // adjust card height to match the image
            double scaleX = (double)width / intrinsicWidth;
            cardLP.height = (int) (intrinsicHeight * scaleX);
        }

        @Override
        public void onDestroyView() {
            paramsObservable.unsubscribeOn(AndroidSchedulers.mainThread());
            super.onDestroyView();
        }
    }

}
