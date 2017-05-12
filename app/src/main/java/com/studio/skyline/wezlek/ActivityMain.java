package com.studio.skyline.wezlek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.studio.skyline.wezlek.adapters.AdapterDrops;
import com.studio.skyline.wezlek.adapters.AddListener;
import com.studio.skyline.wezlek.adapters.CompleteListener;
import com.studio.skyline.wezlek.adapters.Divider;
import com.studio.skyline.wezlek.adapters.Filter;
import com.studio.skyline.wezlek.adapters.MarkListener;
import com.studio.skyline.wezlek.adapters.ResetListener;
import com.studio.skyline.wezlek.adapters.ResetTimerListener;
import com.studio.skyline.wezlek.adapters.SimpleTouchCallback;
import com.studio.skyline.wezlek.beans.Drop;
import com.studio.skyline.wezlek.extras.Util;
import com.studio.skyline.wezlek.widgets.BucketRecyclerView;
import com.studio.skyline.wezlek.widgets.DialogMark;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

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

    //anonymous interface
    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            //Toast.makeText(ActivityMain.this, "position in activity" + position, Toast.LENGTH_SHORT).show();
            mAdapter.markComplete(position);
        }
    };

    private ResetTimerListener onResetTimerListener = new ResetTimerListener() {
        @Override
        public void onResetTimer(int position) {
            mAdapter.resetTimer(position, (mResults.get(position).getTimeSet()+System.currentTimeMillis()), mResults.get(position).getQuantity());
            //Toast.makeText(ActivityMain.this, "getTimer value: " + mResults.get(position).getTimer(), Toast.LENGTH_SHORT).show();

        }
    };


    private ResetListener mResetListener = new ResetListener() {
        @Override
        public void onReset() {
            AppBucketDrops.save(ActivityMain.this,Filter.NONE);
            loadResults(Filter.NONE);
        }
    };
    private void showDialogAdd() {
        //DialogAdd - class where name and frequency of taking pills are located
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    private void showDialogMark(int position) {
        DialogMark dialog = new DialogMark();
        //we need to past position to constructor in special way
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialog.setArguments(bundle);
        dialog.setCompleteListener(mCompleteListener);
        dialog.setResetTimeListener(onResetTimerListener);
        dialog.show(getSupportFragmentManager(), "Mark");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializng Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setting up a Toolbar
        setSupportActionBar(mToolbar);
        //initializng button to add medicines
        mBtnAdd = (Button) findViewById(R.id.btn_dodaj_lek);
        //Button "Dodaj lek" listener
        mBtnAdd.setOnClickListener(mBtnAddListener);
        //initializing Realm Database
        mRealm = Realm.getDefaultInstance();
        int filterOption = AppBucketDrops.load(this);
        loadResults(filterOption);
        //initializng Empty view (where no medecines were added)
        mEmptyView = findViewById(R.id.empty_drops);
        //initializing Recycler View
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        //adding a Divider
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        //setting an animation
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        //hiding Toolbar when no items are in Recycler View
        mRecycler.hideIfEmpty(mToolbar);
        //show Empty view when no items are in Recycler View
        mRecycler.showIfEmpty(mEmptyView);
        //adding an Adapter view
        mAdapter = new AdapterDrops(this, mRealm, mResults, mAddListener, mMarkListener, mResetListener);
        mAdapter.setHasStableIds(true);
        //setting an adapter to Recycler
        //Recycler and Adapter are indepentent. We need to link one to each other.
        //Adapter is responsible for showing particular items.
        mRecycler.setAdapter(mAdapter);
        //query in Realm. Query is stored in special arraylist RealResult type
        //objects which are responsible for swiping and removing items in Recycle View
        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycler);
        //initializing background with Glide
        initBackgroundImage();
        Util.scheduleAlarm(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;
        switch (id) {
            case R.id.action_none:
                filterOption = Filter.NONE;
                break;
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.action_sort_ascending_date:
                filterOption = Filter.LEAST_TIME_LEFT;
                break;
            case R.id.action_sort_descending_date:
                filterOption = Filter.MOST_TIME_LEFT;
                break;
            case R.id.action_show_complete:
                filterOption = Filter.COMPLETE;
                break;
            case R.id.action_show_incomplete:
                filterOption = Filter.INCOMPLETE;
                break;
            default:
                handled = false;
                break;
        }
        AppBucketDrops.save(this,filterOption);
        loadResults(filterOption);
        return handled;
    }

    //loading proper option into database
    private void loadResults(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("timer");
                break;
            case Filter.MOST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("timer", Sort.DESCENDING);
                break;
            case Filter.COMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
        }
        mResults.addChangeListener(mChangeListener);
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
