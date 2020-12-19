package me.foolishchow.android.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import me.foolishchow.android.datepicker.OnDateTimeSelectListener;
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
        option.setRangeStart(2020,12,20,0,30,0);
        //option.setRangeEnd(2003,6,20);
        option.setSelected(2020,12,20,0,31,0);
        //option.setSelected(2020,12,20,0,31,0);
        option.setStyle(WheelTime.STYLE_DATE_HOUR_MINUTE);


        final WheelTime wheelTime = new WheelTime();
        wheelTime.setWheels(
                viewBind.year, viewBind.month, viewBind.day,
                viewBind.hour, viewBind.minute, viewBind.second);
        wheelTime.setRangDate(option.getRangeStart(),option.getRangeEnd());
        wheelTime.setStyle(option.getStyle());

        wheelTime.setSelectChangeCallback(new OnDateTimeSelectListener() {
            @Override
            public void onTimeSelectChanged() {
                Log.e("onTimeSelectChanged", FORMAT.format(wheelTime.getTime()));
            }
        });
        wheelTime.setSelected(option.getSelected());
    }
}