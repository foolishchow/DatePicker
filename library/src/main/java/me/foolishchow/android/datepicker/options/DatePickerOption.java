package me.foolishchow.android.datepicker.options;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

import me.foolishchow.android.datepicker.OnDatePickerChangeListener;
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
    protected OnDatePickerChangeListener mOnDatePickerChangeListener;

    @Nullable
    public Calendar getRangeStart() {
        return mRangeStart;
    }

    public void setRangeStart(@Nullable Date rangeStart) {
        setRangeStart(rangeStart == null ? null : asCalendar(rangeStart));
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


    @Nullable
    public Calendar getRangeEnd() {
        return mRangeEnd;
    }

    public void setRangeEnd(@Nullable Date rangeEnd) {
        setRangeEnd(rangeEnd == null ? null : asCalendar(rangeEnd));
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

    public void setSelected(@Nullable Date selected) {
        mSelected = selected == null ? Calendar.getInstance() : asCalendar(selected);
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
    public OnDatePickerChangeListener getOnDatePickerChangeListener() {
        return mOnDatePickerChangeListener;
    }

    public DatePickerOption setOnDatePickerChangeListener(@NonNull OnDatePickerChangeListener onDatePickerChangeListener) {
        mOnDatePickerChangeListener = onDatePickerChangeListener;
        return this;
    }


    public static Calendar asCalendar(int year, int month, int date) {
        Calendar result = Calendar.getInstance();
        result.set(year, month - 1, date);
        return result;
    }

    public static Calendar asCalendar(Date date) {
        Calendar result = Calendar.getInstance();
        result.setTime(date);
        return result;
    }

    public static Date asDate(int year, int month, int date) {
        return new Date(asCalendar(year, month, date).getTimeInMillis());
    }

    private static final Calendar sMinRangeStart = asCalendar(1900, 1, 1);
    private static final Calendar sMaxRangeEnd = asCalendar(2100, 12, 31);
}
