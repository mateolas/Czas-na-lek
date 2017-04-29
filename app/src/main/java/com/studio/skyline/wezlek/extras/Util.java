package com.studio.skyline.wezlek.extras;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

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

}
