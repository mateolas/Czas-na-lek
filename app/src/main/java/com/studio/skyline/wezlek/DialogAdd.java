package com.studio.skyline.wezlek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.studio.skyline.wezlek.beans.Drop;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by aneimat on 04.04.2017.
 */

public class DialogAdd extends DialogFragment {

    private ImageButton mBtnClose;
    private EditText mInputWhat;
    private SeekBar mInputWhen;
    private Button mBtnAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getActivity());
    }

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_dodaj_lek:
                    addAction();

                    break;
            }

            dismiss();
        }
    };

    private void addAction() {
        String what = mInputWhat.getText().toString();
        long now = System.currentTimeMillis();

        Realm.init(getActivity());
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(configuration);
                //default configuration

        Realm realm = Realm.getDefaultInstance();

        Drop drop = new Drop(what, now, 0, false);

        realm.beginTransaction();
        //copying to table, so we need to make a transaction
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();

    }

    public DialogAdd() {
    }

    @Nullable
    @Override

    //this method takes dialog_add xml file and converts it into View object (thanks to inflater)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //we need to use view, because we're working on fragment not an activity
        mInputWhat = (EditText) view.findViewById(R.id.et_nazwa_leku);
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        mInputWhen = (SeekBar) view.findViewById(R.id.sb_hours_interval);
        mBtnAdd = (Button) view.findViewById(R.id.btn_dodaj_lek);

        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnAdd.setOnClickListener(mBtnClickListener);
    }
}
