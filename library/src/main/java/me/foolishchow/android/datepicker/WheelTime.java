package me.foolishchow.android.datepicker;

import android.util.Log;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.bigkoo.pickerview.listener.ISelectTimeCallback;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.foolishchow.android.datepicker.adapters.DateWheelAdapter;
import me.foolishchow.android.datepicker.data.DateWheelVo;

/**
 * 对比原版 WheelTime 主要改变的几个点
 * 1. 数据校验不在此处进行 全部交由{@link me.foolishchow.android.datepicker.options.DatePickerOption} 处理 所有的数据都确保是validated
 * 2. 控件外部可控 你需要展示什么就初始化什么 不需要的就设置为初始null 不再关心布局
 * 3. 时间range校验流程修改
 */
public class WheelTime {
    // 添加大小月月份并将其转换为list,方便之后的判断
    //String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    //String[] months_little = {"4", "6", "9", "11"};
    private static final List<String> FULL_MONTH_LIST = Arrays.asList("1", "3", "5", "7", "8", "10", "12");
    private static final List<String> LESS_MONTH_List = Arrays.asList("4", "6", "9", "11");


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

    private boolean isYearVisible() {
        return DISPLAY_CONFIG[mDateStyle][0];
    }

    private boolean isMonthVisible() {
        return DISPLAY_CONFIG[mDateStyle][1];
    }

    private boolean isDayVisible() {
        return DISPLAY_CONFIG[mDateStyle][2];
    }

    private boolean isHourVisible() {
        return DISPLAY_CONFIG[mDateStyle][3];
    }

    private boolean isMinuteVisible() {
        return DISPLAY_CONFIG[mDateStyle][4];
    }

    private boolean isSecondVisible() {
        return DISPLAY_CONFIG[mDateStyle][5];
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

    private DateWheelAdapter mYearAdapter = new DateWheelAdapter();
    private DateWheelAdapter mMonthAdapter = new DateWheelAdapter();
    private DateWheelAdapter mDayAdapter = new DateWheelAdapter();
    private DateWheelAdapter mHourAdapter = new DateWheelAdapter();
    private DateWheelAdapter mMinuteAdapter = new DateWheelAdapter();
    private DateWheelAdapter mSecondAdapter = new DateWheelAdapter();

    private void initYearWheel() {
        if (!isYearVisible() || mYearWheel == null) return;
        mYearAdapter.reRange(mRangeStart[0], mRangeEnd[0]);
        // 年
        mYearWheel.setAdapter(mYearAdapter);// 设置"年"的显示数据
        mYearWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DateWheelVo item = mYearAdapter.getItem(index);
                mSelected[0] = item.value;
                emitTimeChanged();
            }
        });
    }

    private void initMonthWheel() {
        if (!isMonthVisible() || mMonthWheel == null) return;
        mMonthAdapter.reRange(1, 12);
        mMonthWheel.setAdapter(mMonthAdapter);
        // 添加"月"监听
        mMonthWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mSelected[1] = mMonthAdapter.getItem(index).value;
                emitTimeChanged();
            }
        });
    }

    private void initDayWheel() {
        if (!isDayVisible() || mDayWheel == null) return;
        mDayAdapter.reRange(1, 31);
        mDayWheel.setAdapter(mDayAdapter);
        mDayWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mSelected[2] = mDayAdapter.getItem(index).value;
                emitTimeChanged();
            }
        });
    }

    private void initHourWheel() {
        if (!isHourVisible() || mHourWheel == null) return;
        mHourAdapter.reRange(1, 24);
        mHourWheel.setAdapter(mHourAdapter);
        mHourWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mSelected[3] = mHourAdapter.getItem(index).value;
                mSelectChangeCallback.onTimeSelectChanged();
            }
        });
    }

    private void initMinuteWheel() {
        if (!isMinuteVisible() || mMinuteWheel == null) return;
        mMinuteAdapter.reRange(1, 60);
        mMinuteWheel.setAdapter(mMinuteAdapter);
        mMinuteWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mSelected[4] = mMinuteAdapter.getItem(index).value;
                emitTimeChanged();
            }
        });
    }

    private void initSecondWheel() {
        if (!isSecondVisible() || mSecondWheel == null) return;
        mSecondAdapter.reRange(1, 60);
        mSecondWheel.setAdapter(mSecondAdapter);
        mSecondWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mSelected[5] = mSecondAdapter.getItem(index).value;
                emitTimeChanged();
            }
        });
    }
    //endregion


    //region 数据
    private int[] mRangeStart = new int[]{1990, 1, 1, 0, 0, 0};
    private int[] mRangeEnd = new int[]{2100, 12, 31, 23, 59, 59};
    private int[] mSelected = new int[]{1990, 1, 1, 0, 0, 0};

    public void setSelected(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        setSelected(year,
                month,
                day,
                hour,
                minute,
                second);
    }

    public void setSelected(int year, final int month, int day, int hour, int minute, int second) {
        //show year
        setYearSelected(year);
        //show month
        setMonthSelected(month);
        //show year
        setDaySelected(day);
        setHourSelected(hour);
        setMinuteSelected(minute);
        setSecondSelected(second);

    }

    private void setYearSelected(int year) {
        mSelected[0] = year;
        if (!isYearVisible() || mYearWheel == null) return;
        mYearWheel.setCurrentItem(year - mRangeStart[0]);// 初始化时显示的数据
    }

    private void setMonthSelected(int month) {
        mSelected[1] = month;
        if (!isMonthVisible() || mMonthWheel == null) return;
        int monthStart = 1;
        int monthEnd = 12;
        int mSelectedYear = mSelected[0];
        if (mSelectedYear == mRangeStart[0]) {
            monthStart = mRangeStart[1];
        }
        if (mSelectedYear == mRangeEnd[0]) {
            monthEnd = mRangeEnd[1];
        }
        mMonthAdapter.reRange(monthStart, monthEnd);
        mMonthWheel.setCurrentItem(month - monthStart);
    }

    private void setDaySelected(int day) {
        mSelected[2] = day;
        if (!isDayVisible() || mDayWheel == null) return;
        int mSelectedYear = mSelected[0];
        int mSelectMonth = mSelected[1];

        int dayStart = 1;
        int dayEnd = 31;
        if (mSelectedYear == mRangeStart[0] && mSelectMonth == mRangeStart[1]) {
            dayStart = mRangeStart[2];
        }
        if (mSelectedYear == mRangeEnd[0] && mSelectMonth == mRangeEnd[1]) {
            dayEnd = mRangeEnd[2];
        }
        mDayAdapter.reRange(dayStart, dayEnd);
        mDayWheel.setCurrentItem(mDayAdapter.getItemIndex(mSelected[2]));
    }

    private void setHourSelected(int hour) {
        mSelected[3] = hour;
        if (!isHourVisible() || mHourWheel == null) return;
        //时
        mHourAdapter.reRange(0, 23);
        //mHourWheel.setAdapter(new NumericWheelAdapter(0, 23));
        mHourWheel.setCurrentItem(hour);
    }

    private void setMinuteSelected(int minute) {
        mSelected[4] = minute;
        if (!isMinuteVisible() || mMinuteWheel == null) return;
        //分
        mMinuteAdapter.reRange(0, 59);
        //mMinuteWheel.setAdapter(new NumericWheelAdapter(0, 59));
        mMinuteWheel.setCurrentItem(minute);
    }

    private void setSecondSelected(int second) {
        mSelected[5] = second;
        if (!isSecondVisible() || mSecondWheel == null) return;
        //秒
        mSecondAdapter.reRange(0, 59);
        //mSecondWheel.setAdapter(new NumericWheelAdapter(0, 59));
        mSecondWheel.setCurrentItem(second);
    }
    //endregion

    //region 事件监听

    //endregion

    private void setReDay(int year_num, int monthNum, int startD, int endD) {

    }

    private void updateDateArray(int[] array, @NonNull Calendar calendar) {
        array[0] = calendar.get(Calendar.YEAR);
        array[1] = calendar.get(Calendar.MONTH) + 1;
        array[2] = calendar.get(Calendar.DAY_OF_MONTH);
        array[3] = calendar.get(Calendar.HOUR_OF_DAY);
        array[4] = calendar.get(Calendar.MINUTE);
        array[5] = calendar.get(Calendar.SECOND);
    }

    public void setRangDate(@NonNull Calendar startDate, @NonNull Calendar endDate) {
        updateDateArray(mRangeStart, startDate);
        updateDateArray(mRangeEnd, endDate);
    }

    //region callback
    private ISelectTimeCallback mSelectChangeCallback;

    public void setSelectChangeCallback(ISelectTimeCallback mSelectChangeCallback) {
        this.mSelectChangeCallback = mSelectChangeCallback;
    }

    private void emitTimeChanged() {
        if (mSelectChangeCallback != null) {
            mSelectChangeCallback.onTimeSelectChanged();
        }
    }
    //endregion

    public Date getTime() {
        Log.e("getTime",
                String.format("%02d-%02d-%02d %02d:%02d:%02d",
                        mSelected[0], mSelected[1], mSelected[2],
                        mSelected[3], mSelected[4], mSelected[5]
                )
        );
        return Utils.asDate(
                mSelected[0], mSelected[1], mSelected[2],
                mSelected[3], mSelected[4], mSelected[5]);
    }


}
