package me.foolishchow.android.datepicker.adapters;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.contrarywind.adapter.WheelAdapter;

import java.util.ArrayList;
import java.util.List;

import me.foolishchow.android.datepicker.data.DateWheelVo;

public class DateWheelAdapter implements WheelAdapter<DateWheelVo> {

    public static DateWheelVo mDefault = new DateWheelVo("01",0);

    private boolean formatNumber = true;

    public DateWheelAdapter() {
    }

    public DateWheelAdapter(boolean formatNumber) {
        this.formatNumber = formatNumber;
    }

    private int mStart = 0;
    private int mEnd = 0;

    @SuppressLint("DefaultLocale")
    public void reRange(int start, int end) {
        if(mStart == start && mEnd == end) return;
        mStart = start;
        mEnd = end;
        List<DateWheelVo> list = new ArrayList<>();
        for (int current = start; current <= end; current++) {
            if(formatNumber){
                list.add(new DateWheelVo(String.format("%02d",current), current ));
            }else {
                list.add(new DateWheelVo(String.format("%d",current), current ));
            }
        }
        mList.clear();
        mList.addAll(list);
    }

    public int getItemIndex(int value){
        return value - mStart;
    }

    @NonNull
    public final List<DateWheelVo> mList = new ArrayList<>();

    @Override
    public int getItemsCount() {
        return mList.size();
    }

    @Override
    public DateWheelVo getItem(int index) {
        if(index < 0 || index >= getItemsCount()){
            return mDefault;
        }
        return mList.get(index);
    }

    @Override
    public int indexOf(DateWheelVo o) {
        return mList.indexOf(o);
    }
}
