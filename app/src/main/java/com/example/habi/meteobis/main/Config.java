package com.example.habi.meteobis.main;

import android.util.Log;

import com.example.habi.meteobis.dagger.DaggerMeteogramComponent;
import com.example.habi.meteobis.dagger.MeteogramComponent;

/**
 * Created by Gabriel Fortin
 */

public class Config {
    public static final String TAG = Config.class.getSimpleName();

    // http://www.meteo.pl/um/php/meteorogram_list.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl&cname=Krak%F3w
    // http://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&fdate=2016061212&row=466&col=232&lang=pl

    public static final int TOTAL_PAGES = 1000;
    public static final String BASE_URL = "http://www.meteo.pl/";

    private static MeteogramComponent meteogramComponent;
    synchronized public static MeteogramComponent getMeteogramComponent() {
        if (meteogramComponent == null) {
            Log.d(TAG, "creating 'MeteogramComponent'");
            meteogramComponent = DaggerMeteogramComponent.create();
        }
        return meteogramComponent;
    }
}
