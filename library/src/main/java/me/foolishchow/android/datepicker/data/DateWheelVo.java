package me.foolishchow.android.datepicker.data;

import androidx.annotation.NonNull;

import com.contrarywind.interfaces.IPickerViewData;

public class DateWheelVo implements IPickerViewData {
    @NonNull
    public final String label;
    public final int value;

    public DateWheelVo(String label, int value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String getPickerViewText() {
        return label;
    }
}
