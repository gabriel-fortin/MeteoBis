package com.example.habi.meteobis.main;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.habi.meteobis.R;
import com.example.habi.meteobis.meteogram.MeteogramsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    static {
        Log.i(TAG, "======== START =========");
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
        fab.setOnClickListener(view -> {
            mViewPager.setCurrentItem(Config.TOTAL_PAGES - 1);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        });
    }

    private void prepareViewPager() {
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(meteogramsPagerAdapter);
        mViewPager.addOnPageChangeListener(new FabVisibilityChanger(fab));
        mViewPager.setCurrentItem(Config.TOTAL_PAGES - 1);

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
