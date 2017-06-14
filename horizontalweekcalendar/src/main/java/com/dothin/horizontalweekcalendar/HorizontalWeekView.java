package com.dothin.horizontalweekcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DoThin on 6/12/2017.
 */

public class HorizontalWeekView extends LinearLayout implements View.OnClickListener {
    public static final String FORMAT_DATE_EEEE_MMMM_DD_YYYY = "EEEE, dd MMMM, yyyy";

    private Context context;
    private View rootView;
    private SimpleDateFormat sdf;

    private TextView tvDateDetails;
    private LinearLayout linPreviousWeek;
    private LinearLayout linNextWeek;

    private TextView tvSundayTitle;
    private TextView tvSundayDate;
    private TextView tvMondayDateTitle;
    private TextView tvMondayDate;
    private TextView tvTuesdayDateTitle;
    private TextView tvTuesdayDate;
    private TextView tvWednesdayDateTitle;
    private TextView tvWednesdayDate;
    private TextView tvThursdayDateTitle;
    private TextView tvThursdayDate;
    private TextView tvFridayDate;
    private TextView tvFridayDateTitle;
    private TextView tvSaturdayDateTitle;
    private TextView tvSaturdayDate;

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
//        for (int i=0; i < length; i++){
//            int attr = typedArray.getIndex(i);
//            if(attr == R.styleable.SpinnerWithSearch_hintText){
//                hintText = typedArray.getString(attr);
//            }
//        }
        typedArray.recycle();

        initViews();
    }

    private void initViews() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.week_view, this);

        tvDateDetails = (TextView) rootView.findViewById(R.id.tvDateDetails);
        linPreviousWeek = (LinearLayout) rootView.findViewById(R.id.linPreviousWeek);
        linPreviousWeek.setOnClickListener(this);
        linNextWeek = (LinearLayout) rootView.findViewById(R.id.linNextWeek);
        linNextWeek.setOnClickListener(this);

        tvSundayTitle = (TextView) findViewById(R.id.tvSundayTitle);
        tvSundayDate = (TextView) findViewById(R.id.tvSundayDate);
        tvMondayDateTitle = (TextView) findViewById(R.id.tvMondayDateTitle);
        tvMondayDate = (TextView) findViewById(R.id.tvMondayDate);
        tvTuesdayDateTitle = (TextView) findViewById(R.id.tvTuesdayDateTitle);
        tvTuesdayDate = (TextView) findViewById(R.id.tvTuesdayDate);
        tvWednesdayDateTitle = (TextView) findViewById(R.id.tvWednesdayDateTitle);
        tvWednesdayDate = (TextView) findViewById(R.id.tvWednesdayDate);
        tvThursdayDateTitle = (TextView) findViewById(R.id.tvThursdayDateTitle);
        tvThursdayDate = (TextView) findViewById(R.id.tvThursdayDate);
        tvFridayDateTitle = (TextView) findViewById(R.id.tvFridayDateTitle);
        tvFridayDate = (TextView) findViewById(R.id.tvFridayDate);
        tvSaturdayDateTitle = (TextView) findViewById(R.id.tvSaturdayDateTitle);
        tvSaturdayDate = (TextView) findViewById(R.id.tvSaturdayDateTitle);

        updateCurrentDate();

    }

    /**
     * get current date and update to calendar
     */
    private void updateCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        try {
            String localLang = Locale.getDefault().getLanguage();
            if(localLang.equals("vi")){
                sdf = new SimpleDateFormat(FORMAT_DATE_EEEE_MMMM_DD_YYYY,new Locale("vi", "VN"));
            } else {
                sdf = new SimpleDateFormat(FORMAT_DATE_EEEE_MMMM_DD_YYYY, Locale.US);
            }

            String strCurrentDate = sdf.format(currentDate);
            tvDateDetails.setText(strCurrentDate);

            updateWeekCalendar(calendar);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateWeekCalendar(Calendar currentDate) {
        try {
            Calendar mondayDate = getMondayOfWeek(currentDate);
            String strSunDayDate = getSundayDateNum((Calendar) mondayDate.clone());

            tvMondayDate.setText(String.valueOf(mondayDate.get(Calendar.DAY_OF_MONTH)));
            tvSundayDate.setText(strSunDayDate);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * get day_of_month of sunday in week
     * @param mondayDate
     * @return
     */
    private String getSundayDateNum(Calendar mondayDate) {
        mondayDate.add(Calendar.DATE, -1);
        return String.valueOf(mondayDate.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.linPreviousWeek) {
            Toast.makeText(context, "Previous week", Toast.LENGTH_LONG).show();
        }
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

    /**
     * increase days in calendar
     *
     * @param date date input
     * @param days date input
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); // minus number would decrement the days
        return cal.getTime();
    }

}
