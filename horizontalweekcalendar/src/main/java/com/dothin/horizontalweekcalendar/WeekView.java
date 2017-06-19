package com.dothin.horizontalweekcalendar;

import java.util.Calendar;

/**
 * Created by ThinDV on 6/19/2017.
 */

public interface WeekView {
    interface OnDaySelected{
        public void onSelected(Calendar date);
    }
}
