package me.foolishchow.android.datepicker.adapters;

import androidx.annotation.IntDef;

import com.contrarywind.adapter.WheelAdapter;

/**
 * Description:
 * Author: foolishchow
 * Date: 18/12/2020 4:30 PM
 */
public class NumericWheelAdapter implements WheelAdapter<Integer> {

    private int minValue;
    private int maxValue;

    /**
     * Constructor
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public Integer getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + index;
            return value;
        }
        return 0;
    }

    @Override
    public int indexOf(Integer o) {
        try {
            return (int) o - minValue;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }


}