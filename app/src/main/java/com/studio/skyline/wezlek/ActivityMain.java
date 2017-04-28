package com.studio.skyline.wezlek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.studio.skyline.wezlek.adapters.AdapterDrops;
import com.studio.skyline.wezlek.adapters.AddListener;
import com.studio.skyline.wezlek.adapters.Divider;
import com.studio.skyline.wezlek.adapters.MarkListener;
import com.studio.skyline.wezlek.adapters.SimpleTouchCallback;
import com.studio.skyline.wezlek.beans.Drop;
import com.studio.skyline.wezlek.widgets.BucketRecyclerView;
import com.studio.skyline.wezlek.widgets.DialogMark;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {

    //Variables/references
    public static final String TAG = "VIVZ";
    Toolbar mToolbar;
    Button mBtnAdd;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    View mEmptyView;
    AdapterDrops mAdapter;

    //Inner anonymous class - On click listener for button
    private View.OnClickListener mBtnAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //runs Dialog after clicking a button
            showDialogAdd();
        }
    };

    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            //runs Dialog after clicking a footer
            showDialogAdd();
        }
    };

    //notification that Realm database has been updated
    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            //mAdapter.notifyDataSetChanged();
            mAdapter.update(mResults);

        }
    };

    //we can implement MarkListener to the class
    //or we can use variable which stores anonymous implementation

    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };


    private void showDialogAdd() {
        //DialogAdd - class where name and frequency of taking pills are located
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    private void showDialogMark(int position){
        DialogMark dialog = new DialogMark();
        //we need to past position to constructor in special way
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(),"Mark");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing Realm Database
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        //query in Realm. Query is stored in special arraylist RealResult type
        mResults = mRealm.where(Drop.class).findAllAsync();

        //*
        //initializing items
        //*

        //initializng Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //initializng Empty view (where no medecines were added)
        mEmptyView = findViewById(R.id.empty_drops);
        //initializng button to add medicines
        mBtnAdd = (Button) findViewById(R.id.btn_dodaj_lek);
        //initializing Recycler View
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        //adding a Divider
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        //hiding Toolbar when no items are in Recycler View
        mRecycler.hideIfEmpty(mToolbar);
        //show Empty view when no items are in Recycler View
        mRecycler.showIfEmpty(mEmptyView);
        //adding an Adapter view
        mAdapter = new AdapterDrops(this, mRealm, mResults, mAddListener,mMarkListener);
        //setting an adapter to Recycler
        mRecycler.setAdapter(mAdapter);
        //Button "Dodaj lek" listener
        mBtnAdd.setOnClickListener(mBtnAddListener);
        //objects which are responsible for swiping and removing items in Recycle View
        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycler) ;
        //setting up a Toolbar
        setSupportActionBar(mToolbar);
        //initializing background with Glide
        initBackgroundImage();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListener);
    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);

    }


}
