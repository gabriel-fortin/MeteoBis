package com.example.habi.meteobis;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;
import org.joda.time.Period;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    // http://www.meteo.pl/um/php/meteorogram_list.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl&cname=Krak%F3w
    // http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int TOTAL_PAGES = 1000;
    public static final String BASE_URL = "http://www.meteo.pl/";

    private static DateTime newestDate;
    public static UmMeteogramService umMeteogramService;
    public static ParamsChangerOnSubscribe paramsChanger;
    public static Observable<RequestParams> paramsObservable;

    static {
        Log.i(TAG, "======== START =========");

        DateTime now = DateTime.now();
        newestDate = Util.round(now, 6);

        umMeteogramService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new ToByteArrayConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(UmMeteogramService.class);

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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setTranslationX(400f);
        fab.setOnClickListener(view -> {
            mViewPager.setCurrentItem(TOTAL_PAGES - 1);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        });

        meteogramsPagerAdapter = new MeteogramsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(meteogramsPagerAdapter);
        mViewPager.setCurrentItem(TOTAL_PAGES - 1);
        mViewPager.addOnPageChangeListener(new FabVisibilityChanger(fab));
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

    public static class MeteogramFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int pageNum = getArguments().getInt(ARG_SECTION_NUMBER);

            int hoursToShift = 6 * (TOTAL_PAGES - pageNum);
            DateTime dateTime = getNewestDate().minus(Period.hours(hoursToShift));
            final String formattedTime = formatTime(dateTime);
            Log.v(TAG, "pageNum = " + pageNum + "    " + hoursToShift + "  =>  " + dateTime);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, pageNum) + "   " + formattedTime);

            final ImageView img = (ImageView) rootView.findViewById(R.id.meteoImg);

            paramsObservable
                    .map(imgData -> umMeteogramService.getByDate(formattedTime, imgData.col, imgData.row))
                    .flatMap(a -> a)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<byte[]>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(byte[] imgBytes) {
                            Log.d(TAG, "onNext - bytes: " + imgBytes.length);
                            if (imgBytes.length < 1000) {
                                img.setImageResource(android.R.drawable.ic_delete);
                                return;
                            }
                            // TODO: show progress indicator
                            Glide.with(MeteogramFragment.this)
                                    .load(imgBytes)
                                    .fitCenter()
                                    .placeholder(android.R.drawable.ic_menu_help)
                                    .crossFade()
                                    .into(img);
                        }
                    });

            return rootView;
        }

        @Override
        public void onDestroyView() {
            paramsObservable.unsubscribeOn(AndroidSchedulers.mainThread());
            super.onDestroyView();
        }
    }

}
