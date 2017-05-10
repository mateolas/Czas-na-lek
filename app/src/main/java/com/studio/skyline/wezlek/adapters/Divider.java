package com.studio.skyline.wezlek.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.studio.skyline.wezlek.R;

/**
 * Created by aneimat on 25.04.2017.
 */

public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrienation;


    public Divider(Context context, int orientation) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
        if (orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("This item Decoration can be used only with a Recycler View that uses a Linear Layout Manager with vertical orienation");
        }
        mOrienation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrienation == LinearLayoutManager.VERTICAL) {
            drawHorizontalDivider(c, parent, state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left, top, right, bottom;
        left = parent.getPaddingLeft();

        right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            if (AdapterDrops.FOOTER != parent.getAdapter().getItemViewType(i)) {
                    View current = parent.getChildAt(i);
                top = current.getTop();
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrienation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}
