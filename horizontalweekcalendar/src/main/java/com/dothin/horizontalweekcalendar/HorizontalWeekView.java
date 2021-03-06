package com.dothin.horizontalweekcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DoThin on 6/12/2017.
 */

public class HorizontalWeekView extends LinearLayout implements View.OnClickListener {
    public static final String FORMAT_DATE_EEEE_MMMM_DD_YYYY = "MMMM, yyyy";

    private String colorOfSelectedDate;

    private Context context;
    private View rootView;
    private SimpleDateFormat sdf;
    private Calendar inWeekCalendar;
    private Calendar selectedCalendar;
    private StateListDrawable dateDrawable;

    private TextView tvDateDetails;
    private LinearLayout linPreviousWeek;
    private LinearLayout linNextWeek;

    private TextView tvSundayDate;
    private TextView tvMondayDate;
    private TextView tvTuesdayDate;
    private TextView tvWednesdayDate;
    private TextView tvThursdayDate;
    private TextView tvFridayDate;
    private TextView tvSaturdayDate;

    private WeekView.OnDaySelected onDateSelected;
    private Calendar startDate;
    private Calendar endDate;

    public void setAvailableDate(Calendar startDate, Calendar endDate) {
        this.startDate = (Calendar) startDate.clone();
        this.endDate = (Calendar) endDate.clone();

        updateWeekCalendar();
    }


    private enum DateState {
        NORMAL,
        SELECTED,
        SELECTED_ON_OTHER_WEEK
    }

    public HorizontalWeekView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public HorizontalWeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalWeekView);
        final int length = typedArray.getIndexCount();
        for (int i=0; i < length; i++){
            int attr = typedArray.getIndex(i);
            if(attr == R.styleable.HorizontalWeekView_date_selected_color){
                colorOfSelectedDate = typedArray.getString(attr);
            }
        }
        typedArray.recycle();

        initVariables();
        initViews();
    }

    private void initVariables() {
        int color = Utilities.getThemeColor(context, R.attr.colorAccent);
        dateDrawable = new StateListDrawable();

        ShapeDrawable defaultShape = new ShapeDrawable(new OvalShape());
        defaultShape.getPaint().setColor(Color.TRANSPARENT);
        dateDrawable.addState(new int[]{}, defaultShape);

        ShapeDrawable selectedShape = new ShapeDrawable(new OvalShape());
        selectedShape.getPaint().setColor(Color.RED);
        dateDrawable.addState(new int[]{android.R.attr.state_selected}, selectedShape);

        ShapeDrawable pressShape = new ShapeDrawable(new OvalShape());
        pressShape.getPaint().setColor(Color.GRAY);
        dateDrawable.addState(new int[]{android.R.attr.state_pressed}, pressShape);

    }

    private void initViews() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.week_view, this);

        tvDateDetails = (TextView) rootView.findViewById(R.id.tvDateDetails);
        linPreviousWeek = (LinearLayout) rootView.findViewById(R.id.linPreviousWeek);
        linPreviousWeek.setOnClickListener(this);
        linNextWeek = (LinearLayout) rootView.findViewById(R.id.linNextWeek);
        linNextWeek.setOnClickListener(this);

        tvSundayDate = (TextView) findViewById(R.id.tvSundayDate);
        tvSundayDate.setOnClickListener(this);
        tvMondayDate = (TextView) findViewById(R.id.tvMondayDate);
        tvMondayDate.setOnClickListener(this);
        tvTuesdayDate = (TextView) findViewById(R.id.tvTuesdayDate);
        tvTuesdayDate.setOnClickListener(this);
        tvWednesdayDate = (TextView) findViewById(R.id.tvWednesdayDate);
        tvWednesdayDate.setOnClickListener(this);
        tvThursdayDate = (TextView) findViewById(R.id.tvThursdayDate);
        tvThursdayDate.setOnClickListener(this);
        tvFridayDate = (TextView) findViewById(R.id.tvFridayDate);
        tvFridayDate.setOnClickListener(this);
        tvSaturdayDate = (TextView) findViewById(R.id.tvSaturdayDate);
        tvSaturdayDate.setOnClickListener(this);

        //updateBackgroundWeekView();

        inWeekCalendar = Calendar.getInstance();
        selectedCalendar = Calendar.getInstance();

        updateCurrentDate();

    }

    public void setOnDateSelected(WeekView.OnDaySelected onDateSelected) {
        this.onDateSelected = onDateSelected;
    }

    /**
     * get current date and update to inWeekCalendar
     */
    private void updateCurrentDate() {
        try {
            String localLang = Locale.getDefault().getLanguage();
            if(localLang.equals("vi")){
                sdf = new SimpleDateFormat(FORMAT_DATE_EEEE_MMMM_DD_YYYY,new Locale("vi", "VN"));
            } else {
                sdf = new SimpleDateFormat(FORMAT_DATE_EEEE_MMMM_DD_YYYY, Locale.US);
            }

            String strCurrentDate = sdf.format(inWeekCalendar.getTime());
            tvDateDetails.setText(strCurrentDate);

            updateWeekCalendar();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateWeekCalendar() {
        try {
            Calendar mondayDate = getMondayOfWeek((Calendar) inWeekCalendar.clone());
            Calendar sunDayDate = getDateNumInWeek((Calendar) mondayDate.clone(), -1);
            Calendar tuesdayDate = getDateNumInWeek((Calendar) mondayDate.clone(), 1);
            Calendar wednesdayDate = getDateNumInWeek((Calendar) mondayDate.clone(), 2);
            Calendar thursdayDate = getDateNumInWeek((Calendar) mondayDate.clone(), 3);
            Calendar fridayDate = getDateNumInWeek((Calendar) mondayDate.clone(), 4);
            Calendar saturdayDate = getDateNumInWeek((Calendar) mondayDate.clone(), 5);

            updateBackgroundDateOnWeek(tvSundayDate, sunDayDate);
            updateBackgroundDateOnWeek(tvMondayDate, mondayDate);
            updateBackgroundDateOnWeek(tvTuesdayDate, tuesdayDate);
            updateBackgroundDateOnWeek(tvWednesdayDate, wednesdayDate);
            updateBackgroundDateOnWeek(tvThursdayDate, thursdayDate);
            updateBackgroundDateOnWeek(tvFridayDate, fridayDate);
            updateBackgroundDateOnWeek(tvSaturdayDate, saturdayDate);

            tvMondayDate.setText(String.valueOf(mondayDate.get(Calendar.DAY_OF_MONTH)));
            tvSundayDate.setText(String.valueOf(sunDayDate.get(Calendar.DAY_OF_MONTH)));
            tvTuesdayDate.setText(String.valueOf(tuesdayDate.get(Calendar.DAY_OF_MONTH)));
            tvWednesdayDate.setText(String.valueOf(wednesdayDate.get(Calendar.DAY_OF_MONTH)));
            tvThursdayDate.setText(String.valueOf(thursdayDate.get(Calendar.DAY_OF_MONTH)));
            tvFridayDate.setText(String.valueOf(fridayDate.get(Calendar.DAY_OF_MONTH)));
            tvSaturdayDate.setText(String.valueOf(saturdayDate.get(Calendar.DAY_OF_MONTH)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void updateBackgroundDateOnWeek(TextView textView,Calendar date) {
        if(null != startDate && null != endDate){
            if(Utilities.compareDate(date, startDate) < 0
                    || Utilities.compareDate(date, endDate) > 0){
                textView.setEnabled(false);
                textView.setTextColor(getResources().getColor(R.color.GREY_200));
            } else {
                textView.setEnabled(true);
                if(textView.getId() == R.id.tvSundayDate || textView.getId() == R.id.tvSaturdayDate){
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    textView.setTextColor(Color.BLACK);
                }
            }
        } else if(null != startDate){
            if(Utilities.compareDate(date, startDate) < 0){
                textView.setEnabled(false);
                textView.setTextColor(getResources().getColor(R.color.GREY_200));

            } else {
                textView.setEnabled(true);
                if(textView.getId() == R.id.tvSundayDate || textView.getId() == R.id.tvSaturdayDate){
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    textView.setTextColor(Color.BLACK);
                }
            }
        } else if(null != endDate){
            if(Utilities.compareDate(date, endDate) > 0){
                textView.setEnabled(false);
                textView.setTextColor(getResources().getColor(R.color.GREY_200));

            } else {
                textView.setEnabled(true);
                if(textView.getId() == R.id.tvSundayDate || textView.getId() == R.id.tvSaturdayDate){
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    textView.setTextColor(Color.BLACK);
                }
            }
        }

        if(Utilities.compareDate(date, selectedCalendar) == 0 ){
            updateDateBackgroundState(textView, DateState.SELECTED);
        } else if(Utilities.compareDate(date, inWeekCalendar) == 0 ){
            updateDateBackgroundState(textView, DateState.SELECTED_ON_OTHER_WEEK);
        } else {
            updateDateBackgroundState(textView, DateState.NORMAL);
        }
    }

    private void updateDateBackgroundState(TextView textView, DateState state){
        switch (state){
            case SELECTED:
                setTextViewBackground(textView, R.drawable.bg_date_selected);
                break;

            case NORMAL:
                setTextViewBackground(textView, R.drawable.bg_date_normal);
                break;

            case SELECTED_ON_OTHER_WEEK:
                setTextViewBackground(textView, R.drawable.bg_date_select_on_other_week);
                break;
        }
    }

    public void setTextViewBackground(TextView textView, int background){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setBackground(context.getDrawable(background));
        } else {
            textView.setBackgroundDrawable(context.getResources().getDrawable(background));
        }
    }

    /**
     * get day_of_month in week
     * @param mondayCalendar
     * @return
     */

    private Calendar getDateNumInWeek(Calendar mondayCalendar, int i) {
        mondayCalendar.add(Calendar.DATE, i);
        return mondayCalendar;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.linPreviousWeek) {
            inWeekCalendar.add(Calendar.DATE, -7);
            updateCurrentDate();
        } else if (i == R.id.linNextWeek){
            inWeekCalendar.add(Calendar.DATE, 7);
            updateCurrentDate();
        } else if (i == R.id.tvSundayDate){
            updateSelectedDate(Calendar.SUNDAY);
        } else if (i == R.id.tvMondayDate){
            updateSelectedDate(Calendar.MONDAY);
        } else if (i == R.id.tvTuesdayDate) {
            updateSelectedDate(Calendar.TUESDAY);
        } else if (i == R.id.tvWednesdayDate) {
            updateSelectedDate(Calendar.WEDNESDAY);
        } else if (i == R.id.tvThursdayDate) {
            updateSelectedDate(Calendar.THURSDAY);
        } else if (i == R.id.tvFridayDate) {
            updateSelectedDate(Calendar.FRIDAY);
        } else if (i == R.id.tvSaturdayDate) {
            updateSelectedDate(Calendar.SATURDAY);
        }
    }

    private void updateSelectedDate(int day) {
        Calendar mondayOfWeek = getMondayOfWeek((Calendar) inWeekCalendar.clone());
        switch (day){
            case Calendar.SUNDAY:
                mondayOfWeek.add(Calendar.DATE, -1);
                break;

            case Calendar.TUESDAY:
                mondayOfWeek.add(Calendar.DATE, 1);
                break;

            case Calendar.WEDNESDAY:
                mondayOfWeek.add(Calendar.DATE, 2);
                break;

            case Calendar.THURSDAY:
                mondayOfWeek.add(Calendar.DATE, 3);
                break;

            case Calendar.FRIDAY:
                mondayOfWeek.add(Calendar.DATE, 4);
                break;

            case Calendar.SATURDAY:
                mondayOfWeek.add(Calendar.DATE, 5);
                break;
        }

        selectedCalendar = (Calendar) mondayOfWeek.clone();
        inWeekCalendar = (Calendar) mondayOfWeek.clone();

        if(null != onDateSelected){
            onDateSelected.onSelected((Calendar) selectedCalendar.clone());
        }
        updateCurrentDate();
    }

    /**
     * get first day of week
     * @param dateInWeek
     * @return
     */
    private static Calendar getMondayOfWeek(Calendar dateInWeek) {
        int dayOfWeek = dateInWeek.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == Calendar.SUNDAY){
            dateInWeek.add(Calendar.DATE, 1);
        } else {
            dateInWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        return dateInWeek;
    }
}
