package me.foolishchow.android.datepicker.options;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

import me.foolishchow.android.datepicker.OnDatePickerSelectListener;
import me.foolishchow.android.datepicker.Utils;
import me.foolishchow.android.datepicker.WheelTime;

/**
 * Description:
 * Author: foolishchow
 * Date: 18/12/2020 3:42 PM
 */
public class DatePickerOption {

    @NonNull
    protected Calendar mRangeStart = sMinRangeStart;
    @NonNull
    protected Calendar mRangeEnd = sMaxRangeEnd;

    @WheelTime.DateStyle
    protected int mStyle = WheelTime.STYLE_DATE;

    @NonNull
    protected Calendar mSelected = Calendar.getInstance();

    @Nullable
    protected OnDatePickerSelectListener mOnDatePickerChangeListener;

    @NonNull
    public Calendar getRangeStart() {
        return mRangeStart;
    }

    public void setRangeStart(@Nullable Date rangeStart) {
        setRangeStart(rangeStart == null ? null : Utils.asCalendar(rangeStart));
    }

    public void setRangeStart(int year, int month, int dayOfMonth,
                              int hourOfDay, int minute, int second) {
        setRangeStart(Utils.asCalendar(year, month, dayOfMonth, hourOfDay, minute, second));
    }

    public void setRangeStart(int year, int month, int dayOfMonth) {
        setRangeStart(Utils.asCalendar(year, month, dayOfMonth, 0, 0, 0));
    }

    public void setRangeStart(@Nullable Calendar rangeStart) {
        if (rangeStart == null) {
            rangeStart = sMinRangeStart;
        } else if (rangeStart.getTimeInMillis() < sMinRangeStart.getTimeInMillis()) {
            rangeStart = sMinRangeStart;
        }
        if (mRangeEnd.getTimeInMillis() < rangeStart.getTimeInMillis()) {
            throw new IllegalStateException("range start is later than range end !");
        }
        mRangeStart = rangeStart;
    }


    @NonNull
    public Calendar getRangeEnd() {
        return mRangeEnd;
    }

    public void setRangeEnd(int year, int month, int dayOfMonth) {
        setRangeEnd(Utils.asCalendar(year, month, dayOfMonth, 0, 0, 0));
    }

    public void setRangeEnd(int year, int month, int dayOfMonth,
                            int hourOfDay, int minute, int second
    ) {
        setRangeEnd(Utils.asCalendar(year, month, dayOfMonth, hourOfDay, minute, second));
    }


    public void setRangeEnd(@Nullable Date rangeEnd) {
        setRangeEnd(rangeEnd == null ? null : Utils.asCalendar(rangeEnd));
    }

    public void setRangeEnd(@Nullable Calendar rangeEnd) {
        if (rangeEnd == null) {
            rangeEnd = sMaxRangeEnd;
        } else if (rangeEnd.getTimeInMillis() > sMaxRangeEnd.getTimeInMillis()) {
            rangeEnd = sMaxRangeEnd;
        }
        if (mRangeStart.getTimeInMillis() > rangeEnd.getTimeInMillis()) {
            throw new IllegalStateException("range start is later than range end !");
        }
        mRangeEnd = rangeEnd;
    }

    @NonNull
    public Calendar getSelected() {
        return mSelected;
    }


    public void setSelected(int year, int month, int dayOfMonth) {
        setSelected(Utils.asCalendar(year, month, dayOfMonth, 0, 0, 0));
    }

    public void setSelected(int year, int month, int dayOfMonth,
                            int hourOfDay, int minute, int second) {
        setSelected(Utils.asCalendar(year, month, dayOfMonth, hourOfDay, minute, second));
    }

    public void setSelected(@Nullable Date selected) {
        mSelected = selected == null ? Calendar.getInstance() : Utils.asCalendar(selected);
    }

    public void setSelected(@Nullable Calendar selected) {
        mSelected = selected == null ? Calendar.getInstance() : selected;
    }

    @WheelTime.DateStyle
    public int getStyle() {
        return mStyle;
    }

    public DatePickerOption setStyle(@WheelTime.DateStyle int style) {
        mStyle = style;
        return this;
    }


    @NonNull
    public OnDatePickerSelectListener getOnDatePickerChangeListener() {
        return mOnDatePickerChangeListener;
    }

    public DatePickerOption setOnDatePickerSelectListener(@NonNull OnDatePickerSelectListener listener) {
        mOnDatePickerChangeListener = listener;
        return this;
    }


    private static final Calendar sMinRangeStart = Utils.asCalendar(1900, 1, 1);
    private static final Calendar sMaxRangeEnd = Utils.asCalendar(2100, 12, 31);
}
