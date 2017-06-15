package com.dothin.horizontalweekcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by DoThin on 6/14/2017.
 */

public class Utilities {
    /**
     * compareDate
     *
     * @param date1 date input
     * @param date2 date input to compare
     * @return 1 if date1 after date2, 0 if equal, -1 if date1 before date2
     */
    public static int compareDate(Calendar date1, Calendar date2) {
        if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                && date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
                && date1.get(Calendar.DATE) == date2.get(Calendar.DATE)) {
            return 0;
        } else if (date1.get(Calendar.YEAR) < date2.get(Calendar.YEAR)
                || (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                    && date1.get(Calendar.MONTH) < date2.get(Calendar.MONTH))
                || (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                    && date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
                    && date1.get(Calendar.DATE) < date2.get(Calendar.DATE))) {
            return -1;
        } else {
            return 1;
        }
    }

    @ColorInt
    public static int getThemeColor( @NonNull final Context context,  @AttrRes final int attributeColor) {
        final TypedValue value = new TypedValue();
        context.getTheme ().resolveAttribute (attributeColor, value, true);
        return value.data;
    }

    private int fetchAccentColor(Context mContext) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = mContext.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
