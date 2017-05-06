package com.studio.skyline.wezlek.services;

import android.app.IntentService;
import android.content.Intent;

import com.studio.skyline.wezlek.beans.Drop;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationService extends IntentService {
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //code if we want to use Realm in background threat
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            //query incomplete drops
            RealmResults<Drop> results = realm.where(Drop.class).equalTo("completed", false).findAll();
            for (Drop current : results) {
                if (isNotificationNeeded(current.getAdded(), current.getWhen())) {

                }

            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    //checking that 90% of time has elapsed
    private boolean isNotificationNeeded(long added, long when) {
        long now = System.currentTimeMillis();
        if(now>when){
            return false;
        } else {
            long difference90 = (long) (0.9 * (when - added));
            return (now>(added + difference90)) ? true:false;
        }
    }

}


