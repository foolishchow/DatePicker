package me.foolishchow.android.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.contrarywind.listener.OnItemSelectedListener;

import me.foolishchow.android.datepicker.adapters.NumericWheelAdapter;

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
        if (attrs == null) return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateWheelView, 0, 0);


        float dividerWidth = a.getDimension(R.styleable.DateWheelView_divider_width, 2);
        setDividerWidth((int) dividerWidth);

        int dividerColor = a.getColor(R.styleable.DateWheelView_divider_color, Color.TRANSPARENT);
        setDividerColor(dividerColor);

        int dividerType = a.getInt(R.styleable.DateWheelView_divider_type,0);
        switch (dividerType){
            case 2:
                setDividerType(DividerType.CIRCLE);
                break;
            case 1:
                setDividerType(DividerType.WRAP);
                break;
            case 0:
            default:
                setDividerType(DividerType.FILL);
                break;
        }

        boolean alphaGradient = a.getBoolean(R.styleable.DateWheelView_alpha_gradient,true);
        setAlphaGradient(alphaGradient);

        int gravity = a.getInt(R.styleable.DateWheelView_android_gravity, Gravity.CENTER);
        setGravity(gravity);

        int textXOffset = a.getInt(R.styleable.DateWheelView_text_x_offset,0);
        setTextXOffset(textXOffset);

        int mFontFamily = a.getResourceId(R.styleable.DateWheelView_android_fontFamily, -1);
        if (mFontFamily != -1) {
            try {
                Typeface font = ResourcesCompat.getFont(context, mFontFamily);
                setTypeface(font);
            } catch (Resources.NotFoundException ignored) {

            }
        }

        float textSize = a.getDimension(R.styleable.DateWheelView_android_textSize, sp2px(20));
        setTextSize(px2sp(textSize));

        String label = a.getString(R.styleable.DateWheelView_android_label);
        if(!TextUtils.isEmpty(label)){
            setLabel(label);
        }

        boolean is_center_label = a.getBoolean(R.styleable.DateWheelView_is_center_label,false);
        isCenterLabel(is_center_label);

        float lineSpaceMultiplier = a.getFloat(R.styleable.DateWheelView_line_spacing_multiplier, 1.6F);
        setLineSpacingMultiplier(lineSpaceMultiplier);

        int item_visible_count = a.getInt(R.styleable.DateWheelView_item_visible_count,7);
        setItemsVisibleCount(item_visible_count);

        final int min = a.getInt(R.styleable.DateWheelView_min, 0);
        int max = a.getInt(R.styleable.DateWheelView_max, 10);
        final int value = a.getInt(R.styleable.DateWheelView_android_value, min);
        setCurrentItem(value - min);


        setAdapter(new NumericWheelAdapter(min, max));// 设置"年"的显示数据
        a.recycle();//回收内存
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.e("onItemSelected",
                        String.format("index %d value %d ", index,
                                min + index));
            }
        });

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
