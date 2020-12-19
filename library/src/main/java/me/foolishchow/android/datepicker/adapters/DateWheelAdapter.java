package me.foolishchow.android.datepicker.adapters;

import androidx.annotation.NonNull;

import com.contrarywind.adapter.WheelAdapter;

import java.util.ArrayList;
import java.util.List;

import me.foolishchow.android.datepicker.data.DateWheelVo;

public class DateWheelAdapter implements WheelAdapter<DateWheelVo> {

    public void reRange(int start, int end) {
        List<DateWheelVo> list = new ArrayList<>();
        for (int current = start; current <= end; current++) {
            list.add(new DateWheelVo(String.valueOf(start), start));
        }
        mList.clear();
        mList.addAll(list);
    }


    @NonNull
    public final List<DateWheelVo> mList = new ArrayList<>();

    @Override
    public int getItemsCount() {
        return mList.size();
    }

    @Override
    public DateWheelVo getItem(int index) {
        return mList.get(index);
    }

    @Override
    public int indexOf(DateWheelVo o) {
        return mList.indexOf(o);
    }
}
