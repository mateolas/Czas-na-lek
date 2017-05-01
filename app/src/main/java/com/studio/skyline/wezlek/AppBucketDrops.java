package com.studio.skyline.wezlek;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.studio.skyline.wezlek.adapters.Filter;

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


    }

    //method to save info in the Shared Preferences file
    public static void save(Context context, int filterOption) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("filter", filterOption);
        editor.apply();
    }

    //method to load info from the Shared Preferences file
    public static int load(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int filterOption = pref.getInt("filter", Filter.NONE);
        return filterOption;
    }
}
