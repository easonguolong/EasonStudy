package com.easonstudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easonstudy.Adapter.DateNumericAdapter;
import com.easonstudy.widget.OnWheelScrollListener;
import com.easonstudy.widget.WheelView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/5.
 */

public class DateActivity extends AppCompatActivity {

    private Button btn_sure;
    private WheelView Year,Month,Day,Hour,Min;
    private boolean wheelScrolled = false;
    private TextView set_time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_date_time);
        InitUI();
    }

    private void InitUI(){
        Calendar calendar = Calendar.getInstance();
        Year  = (WheelView)findViewById(R.id.date_year);
        Month  = (WheelView)findViewById(R.id.date_month);
        Day = (WheelView)findViewById(R.id.date_day);
        Hour  = (WheelView)findViewById(R.id.date_hour);
        Min  = (WheelView)findViewById(R.id.date_min);
        btn_sure =(Button)findViewById(R.id.sure);
        set_time =(TextView)findViewById(R.id.set_time);

        int curYear = calendar.get(Calendar.YEAR);
        Year.setViewAdapter(new DateNumericAdapter(this,2015,2100));
        Year.setCurrentItem(curYear-2015);
        Year.addScrollingListener(scrollListener);
        Year.setCyclic(true);

        int curMonth = calendar.get(Calendar.MONTH)+1;
        Month.setViewAdapter(new DateNumericAdapter(this,1,12));
        Month.setCurrentItem(curMonth-1);
        Month.addScrollingListener(scrollListener);
        Month.setCyclic(true);

        int curday = calendar.get(Calendar.DAY_OF_MONTH);
        Day.setViewAdapter(new DateNumericAdapter(this,1,31));
        Day.setCurrentItem(curday-1);
        Day.addScrollingListener(scrollListener);
        Day.setCyclic(true);

        int curhour = calendar.get(Calendar.HOUR_OF_DAY);
        Hour.setViewAdapter(new DateNumericAdapter(this,0,23));
        Hour.setCurrentItem(curhour);
        Hour.setCyclic(true);

        int curmin = calendar.get(Calendar.MINUTE);
        Min.setViewAdapter(new DateNumericAdapter(this,0,59));
        Min.setCurrentItem(curmin);
        Min.setCyclic(true);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_time.setText(String.valueOf(Year.getCurrentItem()+15)+String.valueOf(Month.getCurrentItem()+1)+String.valueOf(Day.getCurrentItem()+1));
            }
        });
    }


    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
            updateTimeStatus();
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateTimeStatus();
        }
    };

    private void updateTimeStatus(){
        int year = Year.getCurrentItem()+2015;
        int month = Month.getCurrentItem()+1;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            Day.setViewAdapter(new DateNumericAdapter(this,1,31));
        }else if(month==2){
            boolean isLeapYear = false;
            if (year % 100==0){
                if(year % 400 ==0){
                    isLeapYear = true;
                }else{
                    isLeapYear = false;
                }
            }else {
                if (year % 4 == 0) {
                    isLeapYear = true;
                } else {
                    isLeapYear = false;
                }
            }
            if (isLeapYear){
                if (Day.getCurrentItem()>28){
                    Day.scroll(30,2000);
                }
                Day.setViewAdapter(new DateNumericAdapter(this,1,29));
            }else{
                if (Day.getCurrentItem()>27){
                    Day.scroll(30,2000);
                }
                Day.setViewAdapter(new DateNumericAdapter(this,1,28));
            }
        }else{
            if (Day.getCurrentItem()>29){
                Day.scroll(30,2000);
            }
            Day.setViewAdapter(new DateNumericAdapter(this,1,30));
        }
    }


}
