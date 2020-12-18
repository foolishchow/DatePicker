package me.foolishchow.android.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.contrarywind.listener.OnItemSelectedListener;

/**
 * Description:
 * Author: foolishchow
 * Date: 18/12/2020 3:56 PM
 */
public class DateWheelView extends com.contrarywind.view.WheelView {
    public DateWheelView(Context context) {
        this(context, null);
    }

    public DateWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateWheelView, 0, 0);
            int mFontFamily = a.getResourceId(R.styleable.DateWheelView_android_fontFamily, -1);

            float lineSpaceExtra = a.getFloat(R.styleable.DateWheelView_lineSpacingExtra,1.6F);

            float textSize = a.getDimension(R.styleable.DateWheelView_android_textSize,sp2px(20));
            final int min = a.getInt(R.styleable.DateWheelView_min, 0);
            int max = a.getInt(R.styleable.DateWheelView_max, 10);
            final int value = a.getInt(R.styleable.DateWheelView_android_value, min);
            a.recycle();//回收内存

            setLineSpacingMultiplier(lineSpaceExtra);
            setTextSize(px2sp(textSize));
            setAdapter(new NumericWheelAdapter(min, max));// 设置"年"的显示数据
            setCurrentItem(value - min);

            setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    Log.e("onItemSelected",
                            String.format("index %d value %d ",  index,
                            min + index));
                }
            });
        }





    }


    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     */
    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Value of px to value of dp.
     *
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * Value of sp to value of px.
     *
     * @param spValue The value of sp.
     * @return value of px
     */
    public static int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
