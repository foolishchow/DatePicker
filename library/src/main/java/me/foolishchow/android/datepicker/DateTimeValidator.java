package me.foolishchow.android.datepicker;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeValidator {
    private final int[] mRangeStart = new int[]{1990, 1, 1, 0, 0, 0};
    private final int[] mRangeEnd = new int[]{2100, 12, 31, 23, 59, 59};
    private final int[] mSelected = new int[]{1990, 1, 1, 0, 0, 0};
    private final int[][] mRangeCurrent = new int[][]{
            {1990, 2100}, {1, 12}, {1, 31}, {0, 23}, {0, 59}, {0, 59}
    };

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
        mRangeCurrent[0][0] = mRangeStart[0];
        mRangeCurrent[0][1] = mRangeEnd[0];
    }

    public void YearChange(int year) {
        mSelected[0] = year;
        Validate();
    }

    public void MonthChange(int month) {
        mSelected[1] = month;
        Validate();
    }

    public void DayChange(int dayOfMonth) {
        mSelected[2] = dayOfMonth;
        Validate();
    }

    public void HourChange(int hourOfDay) {
        mSelected[3] = hourOfDay;
        Validate();
    }

    public void MinuteChange(int minute) {
        mSelected[4] = minute;
        Validate();
    }

    public void SecondChange(int second) {
        mSelected[5] = second;
        Validate();
    }

    public void setSelected(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        mSelected[0] = year;
        mSelected[1] = month;
        mSelected[2] = dayOfMonth;
        mSelected[3] = hourOfDay;
        mSelected[4] = minute;
        mSelected[5] = second;
        Validate();
    }


    public void Validate() {
        int year = mSelected[0];
        int month = mSelected[1];
        int dayOfMonth = mSelected[2];
        int hourOfDay = mSelected[3];
        int minute = mSelected[4];
        int second = mSelected[5];
        //region month
        int[] monthRange = {1, 12};
        if (year == mRangeStart[0]) {
            monthRange[0] = mRangeStart[1];
        }
        if (year == mRangeEnd[0]) {
            monthRange[1] = mRangeEnd[1];
        }
        if (month < monthRange[0]) {
            month = monthRange[0];
            mSelected[1] = month;
        } else if (month > monthRange[1]) {
            month = monthRange[1];
            mSelected[1] = month;
        }
        mRangeCurrent[1] = monthRange;
        //endregion

        //region dayOfMonth
        int[] dayOfMonthRange = {1, getMonthDay(year, month)};
        if (year == mRangeStart[0] && month == mRangeStart[1]) {
            dayOfMonthRange[0] = mRangeStart[2];
        }
        if (year == mRangeEnd[0] && month == mRangeEnd[1]) {
            dayOfMonthRange[1] = mRangeEnd[2];
        }
        if (dayOfMonth < dayOfMonthRange[0]) {
            dayOfMonth = dayOfMonthRange[0];
            mSelected[2] = dayOfMonth;
        } else if (dayOfMonth > dayOfMonthRange[1]) {
            dayOfMonth = dayOfMonthRange[1];
            mSelected[2] = dayOfMonth;
        }
        mRangeCurrent[2] = dayOfMonthRange;
        //endregion

        //region hourOfDay
        int[] hourOfDayRange = {0, 23};
        if (year == mRangeStart[0] && month == mRangeStart[1] && dayOfMonth == mRangeStart[2]) {
            hourOfDayRange[0] = mRangeStart[3];
        }
        if (year == mRangeEnd[0] && month == mRangeEnd[1] && dayOfMonth == mRangeEnd[2]) {
            hourOfDayRange[1] = mRangeEnd[3];
        }
        if (hourOfDay < hourOfDayRange[0]) {
            hourOfDay = hourOfDayRange[0];
            mSelected[3] = hourOfDay;
        } else if (hourOfDay > hourOfDayRange[1]) {
            hourOfDay = hourOfDayRange[1];
            mSelected[3] = hourOfDay;
        }
        mRangeCurrent[3] = hourOfDayRange;
        //endregion

        //region minute
        int[] minuteRange = {0, 59};
        if (year == mRangeStart[0] && month == mRangeStart[1] && dayOfMonth == mRangeStart[2] && hourOfDay == mRangeStart[3]) {
            minuteRange[0] = mRangeStart[4];
        }
        if (year == mRangeEnd[0] && month == mRangeEnd[1] && dayOfMonth == mRangeEnd[2] && hourOfDay == mRangeEnd[3]) {
            minuteRange[1] = mRangeEnd[4];
        }
        if (minute < minuteRange[0]) {
            minute = minuteRange[0];
            mSelected[4] = minute;
        } else if (minute > minuteRange[1]) {
            minute = minuteRange[1];
            mSelected[4] = minute;
        }
        mRangeCurrent[4] = minuteRange;
        //endregion


        emitValidate();

    }

    private void emitValidate() {
        ValidateResult[] list = new ValidateResult[6];
        for (int index = 0; index < 6; index++) {
            list[index] = new ValidateResult(mRangeCurrent[index][0], mRangeCurrent[index][1], mSelected[index]);
        }
        if (mValidatedListener != null) {
            try {
                mValidatedListener.onDateTimeValidated(list[0], list[1], list[2], list[3], list[4], list[5]);
            } catch (Exception e) {

            }
        }
    }

    private int getMonthDay(int year, int month) {
        if (month == 2) {
            return Utils.isLeapYear(year) ? 29 : 28;
        }
        if (LESS_MONTH_List.contains(month)) {
            return 30;
        }
        return 31;
    }

    private static final List<Integer> FULL_MONTH_LIST = Arrays.asList(1, 3, 5, 7, 8, 10, 12);
    private static final List<Integer> LESS_MONTH_List = Arrays.asList(4, 6, 9, 11);

    //region validate listener
    private ValidatedListener mValidatedListener;

    public void setValidatedListener(ValidatedListener listener) {
        this.mValidatedListener = listener;
    }

    public interface ValidatedListener {
        void onDateTimeValidated(
                @NonNull ValidateResult year, @NonNull ValidateResult month, @NonNull ValidateResult dayOfMonth,
                @NonNull ValidateResult hourOfDay, @NonNull ValidateResult minute, @NonNull ValidateResult second
        );
    }

    public static class ValidateResult {
        public final int rangeStart;
        public final int rangeEnd;
        public final int current;

        public ValidateResult(int rangeStart, int rangeEnd, int current) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
            this.current = current;
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
