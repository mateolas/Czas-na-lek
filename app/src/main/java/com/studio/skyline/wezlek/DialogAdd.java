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

/**
 * Created by aneimat on 04.04.2017.
 */

public class DialogAdd extends DialogFragment {

    private ImageButton mBtnClose;
    private EditText mInputWhat;
    private SeekBar mInputWhen;
    private Button mBtnAdd;

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

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
    }
}
