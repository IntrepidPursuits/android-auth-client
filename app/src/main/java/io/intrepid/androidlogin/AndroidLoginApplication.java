package io.intrepid.androidlogin;

import android.app.Application;

import timber.log.Timber;

public class AndroidLoginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
