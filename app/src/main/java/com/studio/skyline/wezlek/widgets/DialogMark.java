package com.studio.skyline.wezlek.widgets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.studio.skyline.wezlek.R;
import com.studio.skyline.wezlek.adapters.CompleteListener;
import com.studio.skyline.wezlek.adapters.ResetTimerListener;

/**
 * Created by aneimat on 28.04.2017.
 */

public class DialogMark extends DialogFragment {

    private ImageButton mBtnClose;
    private Button mBtnCompleted;
    private Button mBtnRestart;

    //to make buttons work we need to create onClickListeners
    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_completed:
                        markAsComplete();
                    break;
                case R.id.btn_restart:
                    restart();
                    
                    break;
            }
            dismiss();
        }
    };

    private ResetTimerListener mTimeListener;

    private void restart() {
        Bundle arguments = getArguments();
        //we need to comunicate with adapter to mark item as completed
        if (mTimeListener != null && arguments != null) {
            //position - position of the clicked item
            int position = arguments.getInt("POSITION");
            mTimeListener.onResetTimer(position);
        }
    }

    private CompleteListener mListener;

    private void markAsComplete() {
        Bundle arguments = getArguments();
        //we need to comunicate with adapter to mark item as completed
        if (mListener != null && arguments != null) {
            int position = arguments.getInt("POSITION");
            mListener.onComplete(position);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        mBtnCompleted = (Button) view.findViewById(R.id.btn_completed);
        mBtnRestart = (Button) view.findViewById(R.id.btn_restart);
        //setting listener to button
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnCompleted.setOnClickListener(mBtnClickListener);
        mBtnRestart.setOnClickListener(mBtnClickListener);

    }

    public void setCompleteListener(CompleteListener mCompleteListener) {
        mListener = mCompleteListener;
    }

    public void setResetTimeListener(ResetTimerListener mResetTimeListener) {
        mTimeListener = mResetTimeListener;
    }
}

