package com.studio.skyline.wezlek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.studio.skyline.wezlek.beans.Drop;

import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by aneimat on 04.04.2017.
 */

public class DialogAdd extends DialogFragment {

    private ImageButton mBtnClose;
    private EditText mInputWhat;
    private DatePicker mInputWhen;
    private SeekBar mTimeLeft;
    private Button mBtnAdd;


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
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //Toast.makeText(getActivity(),"TEST",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //Toast.makeText(getActivity(),"START",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //Toast.makeText(getActivity(),"STOP",Toast.LENGTH_SHORT).show();
        }
    };

    private void addAction() {
        String what = mInputWhat.getText().toString();
        String date = mInputWhen.getDayOfMonth() + "/" + mInputWhen.getMonth() + "/" + mInputWhen.getYear();
        Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
        //changing date with Day/Month/Year into miliseconds
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, mInputWhen.getDayOfMonth());
        calendar.set(Calendar.MONTH, mInputWhen.getMonth());
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);



        long now = System.currentTimeMillis();
        Realm.init(getActivity());
        //default configuration
        Realm realm = Realm.getDefaultInstance();

        Drop drop = new Drop(what, now, calendar.getTimeInMillis() , false);


        //copying to table, so we need to make a transaction
        realm.beginTransaction();
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
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        mInputWhat = (EditText) view.findViewById(R.id.et_nazwa_leku);
        mInputWhen = (DatePicker) view.findViewById(R.id.bpv_date);
        mTimeLeft = (SeekBar) view.findViewById(R.id.sb_hours_interval);
        mBtnAdd = (Button) view.findViewById(R.id.btn_dodaj_lek);
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnAdd.setOnClickListener(mBtnClickListener);
        mTimeLeft.setOnSeekBarChangeListener(mBarListener);
    }
}
