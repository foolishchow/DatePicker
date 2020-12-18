package me.foolishchow.android.datepicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.contrarywind.view.WheelView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description:
 * Author: foolishchow
 * Date: 18/12/2020 3:45 PM
 */
public class DateTimeWheelView extends LinearLayout {
    public DateTimeWheelView(Context context) {
        super(context);
    }

    public DateTimeWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DateTimeWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateTimeWheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected DatePickerOption mPickerOptions;

    //region views
    @Nullable
    private DateWheelView mYearWheel;
    @Nullable
    private DateWheelView mMonthWheel;
    @Nullable
    private DateWheelView mDayWheel;
    @Nullable
    private DateWheelView mHourWheel;
    @Nullable
    private DateWheelView mMinuteWheel;
    @Nullable
    private DateWheelView mSecondWheel;

    public void adapterViews(
            @Nullable DateWheelView year, @Nullable DateWheelView month, @Nullable DateWheelView day,
            @Nullable DateWheelView hour, @Nullable DateWheelView minute, @Nullable DateWheelView second
    ) {
        mYearWheel = year;
        mMonthWheel = month;
        mDayWheel = day;
        mHourWheel = hour;
        mMinuteWheel = minute;
        mSecondWheel = second;
    }
    //endregion

    //region 展示类型
    public final static int STYLE_DATE = 0;
    public final static int STYLE_DATE_TIME = 1;
    public final static int STYLE_YEAR_MONTH = 2;
    public final static int STYLE_DATE_HOUR_MINUTE = 3;
    public final static int STYLE_MOMENT = 4;
    private static final boolean[][] styles = {
            {true, true, true, false, false, false},
            {true, true, true, true, true, true},
            {true, true, false, false, false, false},
            {true, true, true, true, true, false},
            {false, false, false, true, true, false},
    };

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STYLE_DATE, STYLE_DATE_TIME, STYLE_YEAR_MONTH, STYLE_DATE_HOUR_MINUTE, STYLE_MOMENT})
    public @interface DateStyle {
    }
    @DateStyle
    private int mDateStyle = STYLE_DATE;
    public void setStyle(@DateStyle int style){
        mDateStyle = style;
    }
    //endregion



}
