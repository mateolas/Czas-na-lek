package com.studio.skyline.wezlek.adapters;

/**
 * Created by aneimat on 28.04.2017.
 */

public interface MarkListener {
    //position argument because we need to know position of the item
    //if we know the position, then we can set this item as completed
    void onMark(int position);

}
