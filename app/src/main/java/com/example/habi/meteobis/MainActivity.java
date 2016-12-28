package com.example.habi.meteobis;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // http://www.meteo.pl/um/php/meteorogram_list.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl&cname=Krak%F3w
    // http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int TOTAL_PAGES = 1000;
    public static final String BASE_URL = "http://www.meteo.pl/";

    static {
        Log.i(TAG, "======== START =========");
    }

    private MeteogramsPagerAdapter meteogramsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;
        // FIXME: fab disappears on screen rotation

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


}
