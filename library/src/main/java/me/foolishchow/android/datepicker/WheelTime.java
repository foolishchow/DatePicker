package me.foolishchow.android.datepicker;

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
import java.util.List;

import me.foolishchow.android.datepicker.adapters.DateWheelAdapter;

/**
 * 对比原版 WheelTime 主要改变的几个点
 * 1. 数据校验不在此处进行 全部交由{@link me.foolishchow.android.datepicker.options.DatePickerOption} 处理 所有的数据都确保是validated
 * 2. 控件外部可控 你需要展示什么就初始化什么 不需要的就设置为初始null 不在关心布局
 * 3. 时间range校验流程修改
 */
public class WheelTime {
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

    // 添加大小月月份并将其转换为list,方便之后的判断
    //String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    //String[] months_little = {"4", "6", "9", "11"};
    private static final List<String> FULL_MONTH_LIST = Arrays.asList("1", "3", "5", "7", "8", "10", "12");
    private static final List<String> LESS_MONTH_List = Arrays.asList("4", "6", "9", "11");

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

    @DateStyle
    private int mDateStyle = STYLE_DATE;

    public void setStyle(@DateStyle int style) {
        mDateStyle = style;
        boolean[] displayConfig = DISPLAY_CONFIG[mDateStyle];
        toggleVisible(mYearWheel, displayConfig[0]);
        initYearChangeListener();
        toggleVisible(mMonthWheel, displayConfig[1]);
        toggleVisible(mDayWheel, displayConfig[2]);
        toggleVisible(mHourWheel, displayConfig[3]);
        toggleVisible(mMinuteWheel, displayConfig[4]);
        toggleVisible(mSecondWheel, displayConfig[5]);

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
    //endregion

    private int[] mRangeStart = new int[]{1990, 1, 1, 0, 0, 0};
    private int[] mRangeEnd = new int[]{2100, 12, 31, 23, 59, 59};
    private int[] mSelected = new int[]{1990, 1, 1, 0, 0, 0};

    private int mStartYear = 1990;
    private int mStartMonth = 1;
    private int mStartDay = 1;

    private int mEndYear = 2100;
    private int mEndMonth = 12;
    private int mEndDay = 31; //表示31天的

    private int mCurrentYear;

    public void setSelected(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
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
        if (isYearVisible() && mYearWheel != null) {
            initYearWheel(year);
        }
        //show month
        if (isMonthVisible()) {
            initMonthWheel(year, month);
        }
        //show year
        if (isDayVisible()) {
            initDayWheel(year, month, day);
        }
        initHourWheel(hour);
        initMinuteWheel(minute);
        initSecondWheel(second);
    }

    //region 事件监听
    private void initSecondWheel(int second) {
        //秒
        //mSecondWheel = (WheelView) mWrapView.findViewById(R.id.second);
        mSecondWheel.setAdapter(new NumericWheelAdapter(0, 59));
        mSecondWheel.setCurrentItem(second);
        setChangedListener(mSecondWheel);
    }

    private void initMinuteWheel(int minute) {
        //分
        //mMinuteWheel = (WheelView) mWrapView.findViewById(R.id.min);
        mMinuteWheel.setAdapter(new NumericWheelAdapter(0, 59));
        mMinuteWheel.setCurrentItem(minute);
        setChangedListener(mMinuteWheel);
    }

    private void initHourWheel(int hour) {
        //时
        //mHourWheel = (WheelView) mWrapView.findViewById(R.id.hour);
        mHourWheel.setAdapter(new NumericWheelAdapter(0, 23));
        mHourWheel.setCurrentItem(hour);
        setChangedListener(mHourWheel);
    }

    private void initDayWheel(int year, int month, int day) {
        // 日
        //mDayWheel = (WheelView) mWrapView.findViewById(R.id.day);
        boolean leapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
        if (mStartYear == mEndYear && mStartMonth == mEndMonth) {
            if (FULL_MONTH_LIST.contains(String.valueOf(month + 1))) {
                if (mEndDay > 31) {
                    mEndDay = 31;
                }
                mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, mEndDay));
            } else if (LESS_MONTH_List.contains(String.valueOf(month + 1))) {
                if (mEndDay > 30) {
                    mEndDay = 30;
                }
                mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, mEndDay));
            } else {
                // 闰年
                if (leapYear) {
                    if (mEndDay > 29) {
                        mEndDay = 29;
                    }
                    mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, mEndDay));
                } else {
                    if (mEndDay > 28) {
                        mEndDay = 28;
                    }
                    mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, mEndDay));
                }
            }
            mDayWheel.setCurrentItem(day - mStartDay);
        } else if (year == mStartYear && month + 1 == mStartMonth) {
            // 起始日期的天数控制
            if (FULL_MONTH_LIST.contains(String.valueOf(month + 1))) {

                mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, 31));
            } else if (LESS_MONTH_List.contains(String.valueOf(month + 1))) {

                mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, 30));
            } else {
                // 闰年 29，平年 28
                mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, leapYear ? 29 : 28));
            }
            mDayWheel.setCurrentItem(day - mStartDay);
        } else if (year == mEndYear && month + 1 == mEndMonth) {
            // 终止日期的天数控制
            if (FULL_MONTH_LIST.contains(String.valueOf(month + 1))) {
                if (mEndDay > 31) {
                    mEndDay = 31;
                }
                mDayWheel.setAdapter(new NumericWheelAdapter(1, mEndDay));
            } else if (LESS_MONTH_List.contains(String.valueOf(month + 1))) {
                if (mEndDay > 30) {
                    mEndDay = 30;
                }
                mDayWheel.setAdapter(new NumericWheelAdapter(1, mEndDay));
            } else {
                // 闰年
                if (leapYear) {
                    if (mEndDay > 29) {
                        mEndDay = 29;
                    }
                    mDayWheel.setAdapter(new NumericWheelAdapter(1, mEndDay));
                } else {
                    if (mEndDay > 28) {
                        mEndDay = 28;
                    }
                    mDayWheel.setAdapter(new NumericWheelAdapter(1, mEndDay));
                }
            }
            mDayWheel.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (FULL_MONTH_LIST.contains(String.valueOf(month + 1))) {
                mDayWheel.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (LESS_MONTH_List.contains(String.valueOf(month + 1))) {
                mDayWheel.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年 29，平年 28
                mDayWheel.setAdapter(new NumericWheelAdapter(mStartDay, leapYear ? 29 : 28));
            }
            mDayWheel.setCurrentItem(day - 1);
        }
        setChangedListener(mDayWheel);
    }

    private void initMonthWheel(int year, int month) {
        // 月
        //mMonthWheel = (WheelView) mWrapView.findViewById(R.id.month);
        if (mStartYear == mEndYear) {//开始年等于终止年
            mMonthWheel.setAdapter(new NumericWheelAdapter(mStartMonth, mEndMonth));
            mMonthWheel.setCurrentItem(month + 1 - mStartMonth);
        } else if (year == mStartYear) {
            //起始日期的月份控制
            mMonthWheel.setAdapter(new NumericWheelAdapter(mStartMonth, 12));
            mMonthWheel.setCurrentItem(month + 1 - mStartMonth);
        } else if (year == mEndYear) {
            //终止日期的月份控制
            mMonthWheel.setAdapter(new NumericWheelAdapter(1, mEndMonth));
            mMonthWheel.setCurrentItem(month);
        } else {
            mMonthWheel.setAdapter(new NumericWheelAdapter(1, 12));
            mMonthWheel.setCurrentItem(month);
        }
        // 添加"月"监听
        mMonthWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;

                if (mStartYear == mEndYear) {
                    month_num = month_num + mStartMonth - 1;
                    if (mStartMonth == mEndMonth) {
                        //重新设置日
                        setReDay(mCurrentYear, month_num, mStartDay, mEndDay, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else if (mStartMonth == month_num) {

                        //重新设置日
                        setReDay(mCurrentYear, month_num, mStartDay, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else if (mEndMonth == month_num) {
                        setReDay(mCurrentYear, month_num, 1, mEndDay, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else {
                        setReDay(mCurrentYear, month_num, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    }
                } else if (mCurrentYear == mStartYear) {
                    month_num = month_num + mStartMonth - 1;
                    if (month_num == mStartMonth) {
                        //重新设置日
                        setReDay(mCurrentYear, month_num, mStartDay, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else {
                        //重新设置日
                        setReDay(mCurrentYear, month_num, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    }

                } else if (mCurrentYear == mEndYear) {
                    if (month_num == mEndMonth) {
                        //重新设置日
                        setReDay(mCurrentYear, mMonthWheel.getCurrentItem() + 1, 1, mEndDay, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else {
                        setReDay(mCurrentYear, mMonthWheel.getCurrentItem() + 1, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    }

                } else {
                    //重新设置日
                    setReDay(mCurrentYear, month_num, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                }

                emitTimeChanged();
            }
        });
    }

    private void initYearChangeListener() {
        if (!isYearVisible() || mYearWheel == null) return;
        // 添加"年"监听
        mYearWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = mYearAdapter.getItem(index).value;
                mCurrentYear = year_num;
                int currentMonthItem = mMonthWheel.getCurrentItem();//记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (mStartYear == mEndYear) {
                    //重新设置月份
                    mMonthWheel.setAdapter(new NumericWheelAdapter(mStartMonth, mEndMonth));

                    if (currentMonthItem > mMonthWheel.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = mMonthWheel.getAdapter().getItemsCount() - 1;
                        mMonthWheel.setCurrentItem(currentMonthItem);
                    }

                    int monthNum = currentMonthItem + mStartMonth;

                    if (mStartMonth == mEndMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, mStartDay, mEndDay, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else if (monthNum == mStartMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, mStartDay, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else if (monthNum == mEndMonth) {
                        setReDay(year_num, monthNum, 1, mEndDay, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else {//重新设置日
                        setReDay(year_num, monthNum, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    }
                } else if (year_num == mStartYear) {//等于开始的年
                    //重新设置月份
                    mMonthWheel.setAdapter(new NumericWheelAdapter(mStartMonth, 12));

                    if (currentMonthItem > mMonthWheel.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = mMonthWheel.getAdapter().getItemsCount() - 1;
                        mMonthWheel.setCurrentItem(currentMonthItem);
                    }

                    int month = currentMonthItem + mStartMonth;
                    if (month == mStartMonth) {
                        //重新设置日
                        setReDay(year_num, month, mStartDay, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else {
                        //重新设置日
                        setReDay(year_num, month, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    }

                } else if (year_num == mEndYear) {
                    //重新设置月份
                    mMonthWheel.setAdapter(new NumericWheelAdapter(1, mEndMonth));
                    if (currentMonthItem > mMonthWheel.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = mMonthWheel.getAdapter().getItemsCount() - 1;
                        mMonthWheel.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + 1;

                    if (monthNum == mEndMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, mEndDay, FULL_MONTH_LIST, LESS_MONTH_List);
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                    }

                } else {
                    //重新设置月份
                    mMonthWheel.setAdapter(new NumericWheelAdapter(1, 12));
                    //重新设置日
                    setReDay(year_num, mMonthWheel.getCurrentItem() + 1, 1, 31, FULL_MONTH_LIST, LESS_MONTH_List);
                }

                emitTimeChanged();
            }
        });
    }


    private DateWheelAdapter mYearAdapter = new DateWheelAdapter();
    private void initYearWheel(int year) {
        mCurrentYear = year;
        mYearAdapter.reRange(mRangeStart[0], mRangeEnd[0]);
        // 年
        //mYearWheel = (WheelView) mWrapView.findViewById(R.id.year);
        mYearWheel.setAdapter(mYearAdapter);// 设置"年"的显示数据
        mYearWheel.setCurrentItem(year - mRangeStart[0]);// 初始化时显示的数据
    }


    private void setChangedListener(WheelView wheelView) {
        if (mSelectChangeCallback != null) {
            wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            });
        }

    }
    //endregion

    private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = mDayWheel.getCurrentItem();

        if (list_big.contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            mDayWheel.setAdapter(new com.bigkoo.pickerview.adapter.NumericWheelAdapter(startD, endD));
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            mDayWheel.setAdapter(new com.bigkoo.pickerview.adapter.NumericWheelAdapter(startD, endD));
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                mDayWheel.setAdapter(new com.bigkoo.pickerview.adapter.NumericWheelAdapter(startD, endD));
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                mDayWheel.setAdapter(new NumericWheelAdapter(startD, endD));
            }
        }

        if (currentItem > mDayWheel.getAdapter().getItemsCount() - 1) {
            currentItem = mDayWheel.getAdapter().getItemsCount() - 1;
            mDayWheel.setCurrentItem(currentItem);
        }
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

    public void setSelectChangeCallback(ISelectTimeCallback mSelectChangeCallback) {
        this.mSelectChangeCallback = mSelectChangeCallback;
    }

    private void emitTimeChanged() {
        if (mSelectChangeCallback != null) {
            mSelectChangeCallback.onTimeSelectChanged();
        }
    }
    private ISelectTimeCallback mSelectChangeCallback;


    //todo return Date or Calendar
    public String getTime() {
        StringBuilder sb = new StringBuilder();
        if (mCurrentYear == mStartYear) {
           /* int i = wv_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/
            if ((mMonthWheel.getCurrentItem() + mStartMonth) == mStartMonth) {
                sb.append((mYearWheel.getCurrentItem() + mStartYear)).append("-")
                        .append((mMonthWheel.getCurrentItem() + mStartMonth)).append("-")
                        .append((mDayWheel.getCurrentItem() + mStartDay)).append(" ")
                        .append(mHourWheel.getCurrentItem()).append(":")
                        .append(mMinuteWheel.getCurrentItem()).append(":")
                        .append(mSecondWheel.getCurrentItem());
            } else {
                sb.append((mYearWheel.getCurrentItem() + mStartYear)).append("-")
                        .append((mMonthWheel.getCurrentItem() + mStartMonth)).append("-")
                        .append((mDayWheel.getCurrentItem() + 1)).append(" ")
                        .append(mHourWheel.getCurrentItem()).append(":")
                        .append(mMinuteWheel.getCurrentItem()).append(":")
                        .append(mSecondWheel.getCurrentItem());
            }

        } else {
            sb.append((mYearWheel.getCurrentItem() + mStartYear)).append("-")
                    .append((mMonthWheel.getCurrentItem() + 1)).append("-")
                    .append((mDayWheel.getCurrentItem() + 1)).append(" ")
                    .append(mHourWheel.getCurrentItem()).append(":")
                    .append(mMinuteWheel.getCurrentItem()).append(":")
                    .append(mSecondWheel.getCurrentItem());
        }

        return sb.toString();
    }
}
