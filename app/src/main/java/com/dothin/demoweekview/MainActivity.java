package com.dothin.demoweekview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dothin.horizontalweekcalendar.HorizontalWeekView;
import com.dothin.horizontalweekcalendar.WeekView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HorizontalWeekView horizontalWeekView = (HorizontalWeekView) findViewById(R.id.weekViewCalendar);
        horizontalWeekView.setOnDateSelected(new WeekView.OnDaySelected() {
            @Override
            public void onSelected(Calendar date) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try{
                    String strDateSelected = sdf.format(date.getTime());
                    Toast.makeText(MainActivity.this, "Selected date: " + strDateSelected, Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
