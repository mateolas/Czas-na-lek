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
import android.widget.TextView;

import com.studio.skyline.wezlek.beans.Drop;
import com.studio.skyline.wezlek.widgets.BucketPickerView;

import io.realm.Realm;

/**
 * Created by aneimat on 04.04.2017.
 */

public class DialogAdd extends DialogFragment {

    private ImageButton mBtnClose;
    private EditText mInputWhat;
    private BucketPickerView mInputWhen;
    private SeekBar mTimeLeft;
    private Button mBtnAdd;
    public  TextView mHoursLeft;
    private static final int MAX_INTERVAL = 48;

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

    private SeekBar.OnSeekBarChangeListener mBarListener = new SeekBar.OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
            progress = progressValue;
            //Toast.makeText(getActivity(),"TEST",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //Toast.makeText(getActivity(),"START",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHoursLeft.setText(progress + "");
            //Toast.makeText(getActivity(),"STOP",Toast.LENGTH_SHORT).show();
        }
    };

    private void addAction() {
        String what = mInputWhat.getText().toString();
        String timer = mHoursLeft.getText().toString();
        long now = System.currentTimeMillis();
        Realm.init(getActivity());
        //default configuration
        Realm realm = Realm.getDefaultInstance();
        Drop drop = new Drop(what, now, mInputWhen.getTime() ,timer, false);
        //copying to table, so we need to make a transaction
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
    }

    public String getTimer(){
        return mHoursLeft.getText().toString();
    }

    public DialogAdd() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //we need to use view, because we're working on fragment not an activity
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        mInputWhat = (EditText) view.findViewById(R.id.et_nazwa_leku);
        mInputWhen = (BucketPickerView) view.findViewById(R.id.bpv_date);
        mTimeLeft = (SeekBar) view.findViewById(R.id.sb_hours_interval);
        mBtnAdd = (Button) view.findViewById(R.id.btn_dodaj_lek);
        mHoursLeft = (TextView) view.findViewById(R.id.tv_hours_left);
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnAdd.setOnClickListener(mBtnClickListener);
        mTimeLeft.setOnSeekBarChangeListener(mBarListener);
        mTimeLeft.setMax(MAX_INTERVAL);
        //initializing seekBar with 0
        mHoursLeft.setText(mTimeLeft.getProgress() + " h");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);


    }

    @Nullable
    @Override
    //this method takes dialog_add xml file and converts it into View object (thanks to inflater)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add, container, false);
    }


}