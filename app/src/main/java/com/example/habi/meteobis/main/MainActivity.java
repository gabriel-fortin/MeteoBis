package com.example.habi.meteobis.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    {
        Log.i(TAG, "     new activity       ");
    }


    // elements of this activity
    private Toolbar toolbar;
    private MeteogramsPagerAdapter adapter;
    private ViewPager viewPager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // switch from launcher theme to proper app theme
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create or retrieve all used objects
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.container);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        adapter = new MeteogramsPagerAdapter(getSupportFragmentManager());

        // set toolbar
        setSupportActionBar(toolbar);

        // set view pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new FabVisibilityChanger(fab));

        // set floating action button
        fab.setOnClickListener(view -> viewPager.setCurrentItem(Config.TOTAL_PAGES - 1));
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
