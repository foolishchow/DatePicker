package me.foolishchow.android.datepicker;

import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;

import me.foolishchow.android.datepicker.adapters.DateWheelAdapter;
import me.foolishchow.android.datepicker.data.DateWheelVo;

/**
 * 对比原版 WheelTime 主要改变的几个点
 * 1. 数据校验不在此处进行 全部交由{@link me.foolishchow.android.datepicker.options.DatePickerOption} 处理 所有的数据都确保是validated
 * 2. 控件外部可控 你需要展示什么就初始化什么 不需要的就设置为初始null 不再关心布局
 * 3. 时间range校验流程修改
 */
public class WheelTime implements DateTimeValidator.ValidatedListener {


    private final DateTimeValidator mValidator = new DateTimeValidator();

    public WheelTime() {
        mValidator.setValidatedListener(this);
    }

    //region 控件
    @Nullable
    private WheelView mYearWheel;
    @Nullable
    private WheelView mMonthWheel;
    @Nullable
    private WheelView mDayWheel;
    @Nullable
    private WheelView mHourWheel;
    @Nullable
    private WheelView mMinuteWheel;
    @Nullable
    private WheelView mSecondWheel;

    public void setWheels(
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
    private static final boolean[][] DISPLAY_CONFIG = {
            {true, true, true, false, false, false},//STYLE_DATE
            {true, true, true, true, true, true},//STYLE_DATE_TIME
            {true, true, false, false, false, false},//STYLE_YEAR_MONTH
            {true, true, true, true, true, false},// STYLE_DATE_HOUR_MINUTE
            {false, false, false, true, true, false},//STYLE_MOMENT Hour Minute
    };

    public final static int STYLE_DATE = 0;
    public final static int STYLE_DATE_TIME = 1;
    public final static int STYLE_YEAR_MONTH = 2;
    public final static int STYLE_DATE_HOUR_MINUTE = 3;
    public final static int STYLE_MOMENT = 4;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STYLE_DATE, STYLE_DATE_TIME, STYLE_YEAR_MONTH, STYLE_DATE_HOUR_MINUTE, STYLE_MOMENT})
    public @interface DateStyle {
    }

    private void toggleVisible(@Nullable WheelView wheelView, boolean isVisible) {
        if (wheelView != null) {
            wheelView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isYearInVisible() {
        return !DISPLAY_CONFIG[mDateStyle][0];
    }

    private boolean isMonthInVisible() {
        return !DISPLAY_CONFIG[mDateStyle][1];
    }

    private boolean isDayInVisible() {
        return !DISPLAY_CONFIG[mDateStyle][2];
    }

    private boolean isHourInVisible() {
        return !DISPLAY_CONFIG[mDateStyle][3];
    }

    private boolean isMinuteInVisible() {
        return !DISPLAY_CONFIG[mDateStyle][4];
    }

    private boolean isSecondInVisible() {
        return !DISPLAY_CONFIG[mDateStyle][5];
    }


    @DateStyle
    private int mDateStyle = STYLE_DATE;

    public void setStyle(@DateStyle int style) {
        mDateStyle = style;
        boolean[] displayConfig = DISPLAY_CONFIG[mDateStyle];
        toggleVisible(mYearWheel, displayConfig[0]);
        initYearWheel();
        toggleVisible(mMonthWheel, displayConfig[1]);
        initMonthWheel();
        toggleVisible(mDayWheel, displayConfig[2]);
        initDayWheel();
        toggleVisible(mHourWheel, displayConfig[3]);
        initHourWheel();
        toggleVisible(mMinuteWheel, displayConfig[4]);
        initMinuteWheel();
        toggleVisible(mSecondWheel, displayConfig[5]);
        initSecondWheel();
    }

    private final DateWheelAdapter mYearAdapter = new DateWheelAdapter();
    private final DateWheelAdapter mMonthAdapter = new DateWheelAdapter();
    private final DateWheelAdapter mDayAdapter = new DateWheelAdapter();
    private final DateWheelAdapter mHourAdapter = new DateWheelAdapter();
    private final DateWheelAdapter mMinuteAdapter = new DateWheelAdapter();
    private final DateWheelAdapter mSecondAdapter = new DateWheelAdapter();

    private void initYearWheel() {
        if (isYearInVisible() || mYearWheel == null) return;
        mYearAdapter.reRange(1970, 2100);
        // 年
        mYearWheel.setAdapter(mYearAdapter);// 设置"年"的显示数据
        mYearWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DateWheelVo item = mYearAdapter.getItem(index);
                mValidator.YearChange(item.value);
            }
        });
    }

    private void initMonthWheel() {
        if (isMonthInVisible() || mMonthWheel == null) return;
        mMonthAdapter.reRange(1, 12);
        mMonthWheel.setAdapter(mMonthAdapter);
        // 添加"月"监听
        mMonthWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DateWheelVo item = mMonthAdapter.getItem(index);
                mValidator.MonthChange(item.value);
            }
        });
    }

    private void initDayWheel() {
        if (isDayInVisible() || mDayWheel == null) return;
        mDayAdapter.reRange(1, 31);
        mDayWheel.setAdapter(mDayAdapter);
        mDayWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DateWheelVo item = mDayAdapter.getItem(index);
                mValidator.DayChange(item.value);
            }
        });
    }

    private void initHourWheel() {
        if (isHourInVisible() || mHourWheel == null) return;
        mHourAdapter.reRange(1, 24);
        mHourWheel.setAdapter(mHourAdapter);
        mHourWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DateWheelVo item = mHourAdapter.getItem(index);
                mValidator.HourChange(item.value);
            }
        });
    }

    private void initMinuteWheel() {
        if (isMinuteInVisible() || mMinuteWheel == null) return;
        mMinuteAdapter.reRange(1, 60);
        mMinuteWheel.setAdapter(mMinuteAdapter);
        mMinuteWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DateWheelVo item = mMinuteAdapter.getItem(index);
                mValidator.MinuteChange(item.value);
            }
        });
    }

    private void initSecondWheel() {
        if (isSecondInVisible() || mSecondWheel == null) return;
        mSecondAdapter.reRange(1, 60);
        mSecondWheel.setAdapter(mSecondAdapter);
        mSecondWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DateWheelVo item = mSecondAdapter.getItem(index);
                mValidator.SecondChange(item.value);
            }
        });
    }
    //endregion


    //region 数据
    public void setSelected(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        mValidator.setSelected(year, month, day, hour, minute, second);
    }
    //endregion

    //region 事件监听

    //endregion

    @Override
    public void onDateTimeValidated(
            @NonNull DateTimeValidator.ValidateResult year,
            @NonNull DateTimeValidator.ValidateResult month,
            @NonNull DateTimeValidator.ValidateResult dayOfMonth,
            @NonNull DateTimeValidator.ValidateResult hourOfDay,
            @NonNull DateTimeValidator.ValidateResult minute,
            @NonNull DateTimeValidator.ValidateResult second
    ) {
        if(!isYearInVisible() && mYearWheel != null){
            mYearAdapter.reRange(year.rangeStart,year.rangeEnd);
            mYearWheel.setCurrentItem(mYearAdapter.getItemIndex(year.current));
        }
        if(!isMonthInVisible() && mMonthWheel != null){
            mMonthAdapter.reRange(month.rangeStart,month.rangeEnd);
            mMonthWheel.setCurrentItem(mMonthAdapter.getItemIndex(month.current));
        }
        if(!isDayInVisible() && mDayWheel != null){
            mDayAdapter.reRange(dayOfMonth.rangeStart,dayOfMonth.rangeEnd);
            mDayWheel.setCurrentItem(mDayAdapter.getItemIndex(dayOfMonth.current));
        }
        if(!isHourInVisible() && mHourWheel != null){
            mHourAdapter.reRange(hourOfDay.rangeStart,hourOfDay.rangeEnd);
            mHourWheel.setCurrentItem(mHourAdapter.getItemIndex(hourOfDay.current));
        }
        if(!isMinuteInVisible() && mMinuteWheel != null){
            mMinuteAdapter.reRange(minute.rangeStart,minute.rangeEnd);
            mMinuteWheel.setCurrentItem(mMinuteAdapter.getItemIndex(minute.current));
        }
        if(!isSecondInVisible() && mSecondWheel != null){
            mSecondAdapter.reRange(second.rangeStart,second.rangeEnd);
            mSecondWheel.setCurrentItem(mSecondAdapter.getItemIndex(second.current));
        }
        emitTimeChanged();
    }


    public void setRangDate(@NonNull Calendar startDate, @NonNull Calendar endDate) {
        mValidator.setRangDate(startDate, endDate);
    }

    //region callback
    private OnDateTimeSelectListener mSelectChangeCallback;

    public void setSelectChangeCallback(OnDateTimeSelectListener mSelectChangeCallback) {
        this.mSelectChangeCallback = mSelectChangeCallback;
    }

    private void emitTimeChanged() {
        if (mSelectChangeCallback != null) {
            mSelectChangeCallback.onTimeSelectChanged();
        }
    }
    //endregion

    public Date getTime() {
        return mValidator.getTime();
    }


}
