package com.studio.skyline.wezlek.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import com.studio.skyline.wezlek.ActivityMain;
import com.studio.skyline.wezlek.R;
import com.studio.skyline.wezlek.beans.Drop;

import br.com.goncalves.pugnotification.notification.PugNotification;
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
            RealmResults<Drop> results = realm.where(Drop.class)
                    .equalTo("completed", false)
                    .equalTo("paused",false)
                    .findAll();

            for (Drop current : results) {
                if (isNotificationNeeded(current.getTimer(), System.currentTimeMillis())) {
                    fireNotification(current);
                    //Toast.makeText(getApplicationContext(),"TEST",Toast.LENGTH_SHORT).show();
                    }

            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void fireNotification(Drop drop) {
        String message = getString(R.string.notif_message) + " \"" + drop.getWhat() + "\"";
        PugNotification.with(this)
                .load()
                .title(R.string.notif_title)
                .message(message)
                .bigTextStyle(R.string.notif_long_message)
                .smallIcon(R.drawable.ic_pill)
                .largeIcon(R.drawable.ic_pill)
                .flags(Notification.DEFAULT_ALL)
                .autoCancel(true)
                .click(ActivityMain.class)
                .simple()
                .build();
    }


    private boolean isNotificationNeeded(long timer, long now) {

        long difference = timer - now;
        if (difference == 0 || difference < 0) {
            return true;
        } else {
            return false;
        }

    }

}


