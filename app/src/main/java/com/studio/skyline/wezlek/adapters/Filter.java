package com.studio.skyline.wezlek.adapters;

/**
 * Created by aneimat on 30.04.2017.
 */

public interface Filter  {
    //we will use those values to store them in database as integers
    //later we will use them to compare what user has selected
    int NONE = 0;
    int MOST_TIME_LEFT = 1;
    int LEAST_TIME_LEFT = 2;
    int COMPLETE = 3;
    int INCOMPLETE = 4;

}
