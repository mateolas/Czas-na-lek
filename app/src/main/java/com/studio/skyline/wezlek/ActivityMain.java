package com.studio.skyline.wezlek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.stetho.Stetho;
import com.studio.skyline.wezlek.adapters.AdapterDrops;
import com.studio.skyline.wezlek.beans.Drop;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    Button mBtnAdd;
    RecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    AdapterDrops mAdapter;

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {

            mAdapter.update(mResults);
        }
    };

    //inner anonymous class --> need to check it !
    private View.OnClickListener mBtnAddListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            showDialogAdd();
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
        //initializing items
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBtnAdd = (Button) findViewById(R.id.btn_dodaj_lek);
        mBtnAdd.setOnClickListener(mBtnAddListener);
        mRecycler = (RecyclerView) findViewById(R.id.rv_drops);
        mRealm = Realm.getDefaultInstance();
        mAdapter = new  AdapterDrops(this,mResults);
        mRecycler.setAdapter(mAdapter);


        //query in Realm. Query is stored in special arraylist RealResult type
        mResults = mRealm.where(Drop.class).findAllAsync();

        //setting an adapter on RecyclerView
        mRecycler.setAdapter(new AdapterDrops(this,mResults));

        //creating LinearLayoutManager that how to display items to RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);

        //setting up a toolbar
        setSupportActionBar(mToolbar);
        initBackgroundImage();




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




        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                        .withFolder(getCacheDir())

                        .withMetaTables()
                        .withDescendingOrder()
                        .withLimit(1000)
                        .databaseNamePattern(Pattern.compile(".+\\.realm"))
                        .build())
                        .build());
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
