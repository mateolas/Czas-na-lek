package com.studio.skyline.wezlek.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.studio.skyline.wezlek.AppBucketDrops;
import com.studio.skyline.wezlek.R;
import com.studio.skyline.wezlek.beans.Drop;
import com.studio.skyline.wezlek.extras.Util;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by aneimat on 10.04.2017.
 */


public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    //integer for footer
    public static final int COUNT_FOOTER = 1;
    public static final int COUNT_NO_ITEMS = 1;
    public static final int ITEM = 0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER = 2;
    private final ResetListener mResetListener;
    private MarkListener mMarkListener;

    //inflater object which converts xml file to view object
    private LayoutInflater mInflater;

    //ArrayList to contain 100 numbers - not used anymore
    private RealmResults<Drop> mResults;

    private AddListener mAddListener;
    private int mFilterOption;
    private Realm mRealm;
    private Context mContext;

    //adding MarkListener to constructur
    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener listener, MarkListener markListener,ResetListener resetListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        update(results);
        mRealm = realm;
        mAddListener = listener;
        mMarkListener = markListener;
        mResetListener = resetListener;

    }

    //method which generates and return ArrayList with 100 values
    public static ArrayList<String> generateValues() {
        ArrayList<String> dummyValues = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            dummyValues.add("Item" + i);
        }
        return dummyValues;

    }

    public void update(RealmResults<Drop> results) {
        mResults = results;
        mFilterOption = AppBucketDrops.load(mContext);
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        if(position<mResults.size()){
            return mResults.get(position).getAdded();
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemViewType(int position) {
        if (!mResults.isEmpty()) {
            if (position < mResults.size()) {
                return ITEM;
            } else {
                return FOOTER;
            }
        } else {
            if (mFilterOption == Filter.COMPLETE ||
                    mFilterOption == Filter.INCOMPLETE) {
                if (position == 0) {
                    return NO_ITEM;
                } else {
                    return FOOTER;
                }
            } else {
                return ITEM;
            }
        }
    }



    //changig row drop (a bar with medicine name) from xml to view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View view = mInflater.inflate(R.layout.footer, parent, false);
            //footerHolder class which we created below
            return new FooterHolder(view);
        } else if (viewType == NO_ITEM) {
            View view = mInflater.inflate(R.layout.no_item, parent, false);
            return new NoItemsHolder(view);
        } else {

            //layourInflater converts xml file to java View object
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view, mMarkListener);

        }
    }

    //bind holder to tv_what TextView
    //this method is called every time we need to show particular item
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            //changing to text
            Drop drop = mResults.get(position);
            //seting MtextView to proper drop.getWhat text
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }

    }

    @Override
    public int getItemCount() {
        if (!mResults.isEmpty()) {
            return mResults.size() + COUNT_FOOTER;
        } else {
            if (mFilterOption == Filter.LEAST_TIME_LEFT
                    || mFilterOption == Filter.MOST_TIME_LEFT
                    || mFilterOption == Filter.NONE) {
                return 0;
            } else {
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
        }

    }


    @Override
    public void onSwipe(int position) {
        //delete item with transaction from database
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).deleteFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }
        resetFilterIfEmpty();
    }

    private void resetFilterIfEmpty() {
        if(mResults.isEmpty() && (mFilterOption == Filter.COMPLETE || mFilterOption == Filter.INCOMPLETE )){
            mResetListener.onReset();
        }
    }

    public void markComplete(int position) {
        //checking that item is not a footer
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }
    }

public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //initialising mTextView which is type DropHolder
    TextView mTextWhat;
    TextView mTextWhen;
    MarkListener mMarkListener;
    Context mContext;
    View mItemView;

    //"one row" of RecyclerView
    //adding MarkListener to constructor
    public DropHolder(View itemView, MarkListener listener) {
        super(itemView);
        mItemView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
        mTextWhen = (TextView) itemView.findViewById(R.id.tv_when);
        mMarkListener = listener;
    }

    public void setWhat(String what) {
        mTextWhat.setText(what);
    }


    @Override
    public void onClick(View v) {
        // we can't use just show method, we need to use FragmentManager
        //to use FragmentManager from MainActivity we need to implement an interface
        //interface name - MarkListener
        mMarkListener.onMark(getAdapterPosition());
    }

    public void setBackground(boolean completed) {
        Drawable drawable;
        if (completed) {
            drawable = ContextCompat.getDrawable(mContext, R.color.colorLightBlueAfterClick);
        } else {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_row_drop);
        }
            /*if(Build.VERSION.SDK_INT > 15){
                mItemView.setBackground(drawable);
            } else{
                mItemView.setBackgroundDrawable(drawable);
            }*/
        Util.setBackground(mItemView, drawable);

    }

    public void setWhen(long when) {
        mTextWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, 0));

    }
}

public static class NoItemsHolder extends RecyclerView.ViewHolder {

    public NoItemsHolder(View itemView) {
        super(itemView);
    }
}


//creating a FooterHolder
public class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //initialising mTextView which is type DropHolder
    Button mBtnAdd;

    public FooterHolder(View itemView) {
        super(itemView);
        mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
        mBtnAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //were using from this place AddListener
        mAddListener.add();
    }
}


        }
