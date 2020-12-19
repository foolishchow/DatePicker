package me.foolishchow.android.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.bigkoo.pickerview.listener.ISelectTimeCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import me.foolishchow.android.datepicker.WheelTime;
import me.foolishchow.android.datepicker.options.DatePickerOption;
import me.foolishchow.android.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SimpleDateFormat")
    private DateFormat FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding viewBind = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(viewBind.getRoot());

        DatePickerOption option = new DatePickerOption();
        option.setRangeStart(2001,1,1);
        option.setRangeEnd(2004,3,31);
        option.setSelected(2001,4,2);
        option.setStyle(WheelTime.STYLE_DATE_HOUR_MINUTE);


        final WheelTime wheelTime = new WheelTime();
        wheelTime.setWheels(
                viewBind.year, viewBind.month, viewBind.day,
                viewBind.hour, viewBind.minute, viewBind.second);
        wheelTime.setRangDate(option.getRangeStart(),option.getRangeEnd());
        wheelTime.setStyle(option.getStyle());

        wheelTime.setSelectChangeCallback(new ISelectTimeCallback() {
            @Override
            public void onTimeSelectChanged() {
                Log.e("onTimeSelectChanged", FORMAT.format(wheelTime.getTime()));
            }
        });
        wheelTime.setSelected(option.getSelected());
    }
}