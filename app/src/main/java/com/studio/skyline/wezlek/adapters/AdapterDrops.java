package com.studio.skyline.wezlek.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.studio.skyline.wezlek.R;
import com.studio.skyline.wezlek.beans.Drop;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by aneimat on 10.04.2017.
 */


public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    //integer for footer
    public static final int ITEM = 0;
    public static final int FOOTER = 1;


    //inflater object which converts xml file to view object
    private LayoutInflater mInflater;

    //ArrayList to contain 100 numbers
    private RealmResults<Drop> mResults;

    private AddListener mAddListener;
    private Realm mRealm;

    //constructor which excepts context object and Realm results
    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results) {
        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
    }


    public AdapterDrops(Context context, Realm realm,  RealmResults<Drop> results, AddListener listener) {
        mInflater = LayoutInflater.from(context);
        update(results);
        mRealm = realm;
        mAddListener = listener;
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
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        //if statement if we have mResults NULL - no items in database
        if (mResults == null || position < mResults.size()) {
            return ITEM;
        } else {
            return FOOTER;
        }
        //we're returning a viewType int and using it in DropHolder viewType
    }

    //changig row drop (a bar with medicine name) from xml to view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View view = mInflater.inflate(R.layout.footer, parent, false);
            //footerHolder class which we created below
            return new FooterHolder(view);
        } else {

            //layourInflater converts xml file to java View object
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view);

        }
    }

    //bind holder to tv_what TextView
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            //changing to text
            Drop drop = mResults.get(position);
            //seting MtextView to proper drop.getWhat text
            dropHolder.mTextView.setText(drop.getWhat());
        }

    }

    @Override
    public int getItemCount() {
        //if no results in the Recyvler View, return 0, so show first screen
        if (mResults == null || mResults.isEmpty()) {
            return 0;
        } else {
            //returning the number of items which we have in the table
            return mResults.size() + 1;
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
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        //initialising mTextView which is type DropHolder
        TextView mTextView;

        public DropHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_what);
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
