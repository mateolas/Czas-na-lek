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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.studio.skyline.wezlek.adapters.AdapterDrops;
import com.studio.skyline.wezlek.adapters.AddListener;
import com.studio.skyline.wezlek.adapters.CompleteListener;
import com.studio.skyline.wezlek.adapters.Divider;
import com.studio.skyline.wezlek.adapters.Filter;
import com.studio.skyline.wezlek.adapters.MarkListener;
import com.studio.skyline.wezlek.adapters.PauseListener;
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
    public static final String TAG = "SKYLINE";
    Toolbar mToolbar;
    Button mBtnAdd;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    View mEmptyView;
    AdapterDrops mAdapter;
    TextView mFilter;

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

    private PauseListener mPauseListener = new PauseListener() {
        @Override
        public void onPause(int position) {
            //Toast.makeText(ActivityMain.this, "ONPAUSE PAUSE STATUS" + position + mResults.get(position).isPaused(), Toast.LENGTH_SHORT).show();
            mAdapter.markAsPaused(position);
        }
    };

    //anonymous interface
    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            //Toast.makeText(ActivityMain.this, "position in activity" + position, Toast.LENGTH_SHORT).show();
            mAdapter.markComplete(position,System.currentTimeMillis());

        }
    };

    private ResetTimerListener onResetTimerListener = new ResetTimerListener() {
        @Override
        public void onResetTimer(int position) {
            mAdapter.resetTimer(position, (mResults.get(position).getTimeSet() + System.currentTimeMillis()), mResults.get(position).getQuantity());
            //Toast.makeText(ActivityMain.this, "RESET PAUSE STATUS " + mResults.get(position).isPaused(), Toast.LENGTH_SHORT).show();

        }
    };

    private ResetListener mResetListener = new ResetListener() {
        @Override
        public void onReset() {
            AppBucketDrops.save(ActivityMain.this, Filter.NONE, "Filtr: Wszystko");
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
        dialog.setPauseListener(mPauseListener);
        dialog.show(getSupportFragmentManager(), "Mark");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilter = (TextView) findViewById(R.id.tv_filter);
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
        //loading last filter used from SharedPreference file
        int filterOptionInt = AppBucketDrops.load(this);
        String filterOptionString = AppBucketDrops.loadString(this);
        loadResults(filterOptionInt);
        mFilter.setText(filterOptionString);
        //initializng Empty view (where no medecines were added)
        mEmptyView = findViewById(R.id.empty_drops);
        //initializing Recycler View
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        //adding a Divider
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        //setting an animation
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        //hiding Toolbar when no items are in Recycler View
        mRecycler.hideIfEmpty(mToolbar, mFilter);
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
        String filterOptionString = "Filtr: Wszystko";
        switch (id) {
            case R.id.action_none:
                filterOption = Filter.NONE;
                filterOptionString = "Filtr: Wszystko";
                break;
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.action_sort_ascending_date:
                filterOption = Filter.LEAST_TIME_LEFT;
                filterOptionString ="Filtr: Pozostało najmniej czasu ";
                break;
            case R.id.action_sort_descending_date:
                filterOption = Filter.MOST_TIME_LEFT;
                filterOptionString = "Filtr: Pozostało najwięcej czasu";
                break;
            case R.id.action_show_complete:
                filterOption = Filter.COMPLETE;
                filterOptionString = "Filtr: Zakończone";
                break;
            case R.id.action_show_incomplete:
                filterOption = Filter.INCOMPLETE;
                filterOptionString = "Filtr: Niezakończone";
                break;
            case R.id.action_show_pause:
                filterOption = Filter.PAUSE;
                filterOptionString = "Filtr: Wstrzymane";
                break;
            default:
                handled = false;
                filterOptionString = "Filtr: Wszystko";
                break;
        }
        AppBucketDrops.save(this, filterOption,filterOptionString);
        loadResults(filterOption);
        mFilter.setText(filterOptionString);
        return handled;
    }

    //loading proper option into database depending on the filter value
    private void loadResults(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                mResults = mRealm.where(Drop.class)
                        .equalTo("completed", false)
                        .equalTo("paused",false)
                        .findAllSortedAsync("timer");
                break;
            case Filter.MOST_TIME_LEFT:
                mResults = mRealm.where(Drop.class)
                        .equalTo("completed", false)
                        .equalTo("paused",false)
                        .findAllSortedAsync("timer", Sort.DESCENDING);
                break;
            case Filter.COMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
            case Filter.PAUSE:
                mResults = mRealm.where(Drop.class).equalTo("paused", true).findAllAsync();
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
