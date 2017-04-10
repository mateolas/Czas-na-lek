package com.studio.skyline.wezlek;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by aneimat on 10.04.2017.
 */

//we created this class to run configuration of Realm database only once
//Application class is first called when adroid decides to run an app
//Application will never be destroyed (during working of an app)
public class AppBucketDrops extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        //Realm.setDefaultConfiguration(configuration);

    }
}
