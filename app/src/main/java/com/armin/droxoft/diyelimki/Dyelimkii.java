package com.armin.droxoft.diyelimki;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Dyelimkii extends Application {

    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BalooThambi-Regular.tff")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}

