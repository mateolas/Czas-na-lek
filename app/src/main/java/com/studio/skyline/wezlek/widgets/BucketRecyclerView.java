package com.studio.skyline.wezlek.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.studio.skyline.wezlek.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by aneimat on 13.04.2017.
 */

//Added AdapterDataObserver to switch between view (where are items and there aren't items)
public class BucketRecyclerView extends RecyclerView {

    //to be displayed when adapter will be nonEmpty
    private List<View> mNonEmptyViews = Collections.emptyList();
    //to be displayed when adapter is empty
    private List<View> mEmptyViews = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        //super was removed because it was empty
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {
        if (getAdapter() != null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty()) {
            if (getAdapter().getItemCount() == 0) {
                //show all the empty views
                Util.showViews(mEmptyViews);
                //hide the RecyclerView
                setVisibility(View.GONE);
                //hide all the views which are meant to be hidden
                Util.hideViews(mNonEmptyViews);
            } else {
                //hide all the empty views
                Util.showViews(mNonEmptyViews);

                //show the RecyclerView
                setVisibility(View.VISIBLE);

                //hide all the views which are meant to be hidden
                Util.hideViews(mEmptyViews);
            }
        }
    }

    public BucketRecyclerView(Context context) {
        super(context);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            try {
                adapter.registerAdapterDataObserver(mObserver);
            } catch (IllegalStateException e) {

            }
            mObserver.onChanged();
        }

    }

    //... creates an array
    //changing from array to list
    public void hideIfEmpty(View... views) {
        mNonEmptyViews = Arrays.asList(views);

    }

    public void showIfEmpty(View... emptyViews) {
        mEmptyViews = Arrays.asList(emptyViews);

    }
}
