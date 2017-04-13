package com.studio.skyline.wezlek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.studio.skyline.wezlek.adapters.AdapterDrops;
import com.studio.skyline.wezlek.beans.Drop;
import com.studio.skyline.wezlek.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {

    public static final String TAG = "VIVZ";
    Toolbar mToolbar;
    Button mBtnAdd;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    View mEmptyView;
    AdapterDrops mAdapter;


    //inner anonymous class --> need to check it !
    private View.OnClickListener mBtnAddListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };


    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            //mAdapter.notifyDataSetChanged();
            Log.d(TAG, "onChange: was called");
            mAdapter.update(mResults);

        }
    };

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(),"Add");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        //query in Realm. Query is stored in special arraylist RealResult type
        mResults = mRealm.where(Drop.class).findAllAsync();

        //initializing items
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmptyView = findViewById(R.id.empty_drops);
        mBtnAdd = (Button) findViewById(R.id.btn_dodaj_lek);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mAdapter = new  AdapterDrops(this,mResults);
        mRecycler.setAdapter(mAdapter);
        mBtnAdd.setOnClickListener(mBtnAddListener);
        setSupportActionBar(mToolbar);
        //setting up a toolbar
        initBackgroundImage();

        //setting an adapter on RecyclerView
        mRecycler.setAdapter(mAdapter);

        //creating LinearLayoutManager that how to display items to RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);





       //Create an InitializerBuilder
       //Stetho.InitializerBuilder initializerBuilder =
        //        Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        //initializerBuilder.enableWebKitInspector(
        //       Stetho.defaultInspectorModulesProvider(this)
        //);

        // Enable command line interface
        //  initializerBuilder.enableDumpapp(
        //        Stetho.defaultDumperPluginsProvider(this)
        //);

        // Use the InitializerBuilder to generate an Initializer
         //       Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        //        Stetho.initialize(initializer);




        /*Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                        .withFolder(getCacheDir())

                        .withMetaTables()
                        .withDescendingOrder()
                        .withLimit(1000)
                        .databaseNamePattern(Pattern.compile(".+\\.realm"))
                        .build())
                        .build());*/
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
