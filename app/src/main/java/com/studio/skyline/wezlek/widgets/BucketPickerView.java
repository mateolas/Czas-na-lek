package com.studio.skyline.wezlek.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studio.skyline.wezlek.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aneimat on 03.05.2017.
 */

public class BucketPickerView extends LinearLayout {
    private Calendar mCalendar;
    private TextView mTextDate;
    private TextView mTextMonth;
    private TextView mTextYear;
    private SimpleDateFormat mFormatter;

    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view, this);
        mCalendar = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextDate = (TextView) this.findViewById(R.id.tv_date);
        mTextMonth = (TextView) this.findViewById(R.id.tv_month);
        mTextYear = (TextView) this.findViewById(R.id.tv_year);
        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        update(date, month, year, 0, 0, 0);

    }

    private void update(int date, int month, int year, int hour, int minute, int second) {
        String result = mFormatter.format(mCalendar.getTime());

        mCalendar.set(Calendar.DATE, date);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);
        mTextYear.setText(year + "");
        mTextDate.setText(date + "");
        switch (result) {

            case "January":
                result = "Sty";
                break;
            case "February":
                result = "Lut";
                break;
            case "March":
                result = "Mar";
                break;
            case "April":
                result = "Kwi";
                break;
            case "May":
                result = "Maj";
                break;
            case "June":
                result = "Cze";
                break;
            case "July":
                result = "Lip";
                break;
            case "August":
                result = "Sie";
                break;
            case "September":
                result = "Wrz";
                break;
            case "October":
                result = "Paź";
                break;
            case "November":
                result = "List";
                break;
            case "December":
                result = "Gru";
                break;
        }

        mTextMonth.setText(result);

    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }

}
