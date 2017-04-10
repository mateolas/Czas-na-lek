package com.studio.skyline.wezlek.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studio.skyline.wezlek.R;
import com.studio.skyline.wezlek.beans.Drop;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by aneimat on 10.04.2017.
 */


public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    //inflater object which converts xml file to view object
    private LayoutInflater mInflater;
    //ArrayList to contain 100 numbers
    private RealmResults<Drop> mResults;

    //constructor which excepts context object
    public AdapterDrops(Context context, RealmResults<Drop> results) {
        mInflater = LayoutInflater.from(context);
        update(results);
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
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //layourInflater converts xml file to java View object
        View view = mInflater.inflate(R.layout.row_drop, parent, false);
        DropHolder holder = new DropHolder(view);
        return holder;
    }

    //bind holer to tv_what TextView
    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        //changing to text
        Drop drop = mResults.get(position);
        //seting MtextView to proper drop.getWhat text
        holder.mTextView.setText(drop.getWhat());
    }

    @Override
    public int getItemCount() {
        //returning the number of items which we have in the table
        return mResults.size();
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        //initialising mTextView which is type DropHolder
        TextView mTextView;

        public DropHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_what);
        }
    }


}
