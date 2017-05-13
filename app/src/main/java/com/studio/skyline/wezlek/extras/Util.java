package com.studio.skyline.wezlek.extras;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.studio.skyline.wezlek.services.NotificationService;

import java.util.List;

/**
 * Created by aneimat on 13.04.2017.
 */

public class Util {

    public static void showViews(List<View> views){
        for(View view: views){
            view.setVisibility(View.VISIBLE);
        }
    }


    public static void hideViews(List<View> views){
        for(View view: views){
            view.setVisibility(View.GONE);
        }

    }

    public static boolean moreThanJellyBean(){
        return Build.VERSION.SDK_INT > 15;
    }

    public static void setBackground(View view, Drawable drawable){
            if(Build.VERSION.SDK_INT > 15){
                view.setBackground(drawable);
            } else{
                view.setBackgroundDrawable(drawable);
            }
    }

    public static void scheduleAlarm(Context context){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        //puting an intent into Pending intent enables to receive intent after destroying an app
        PendingIntent pendingIntent = PendingIntent.getService(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,200,2000,pendingIntent);
    }

}
