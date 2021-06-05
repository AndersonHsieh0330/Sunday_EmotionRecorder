package com.example.sunday;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;

public class emotion_node {

        private final Calendar cal;
        private final int Year;
        private final int Month;
        private final int Date;
        private final int hr;
        private final int min;
        private final int emotion_count;

        public emotion_node(Calendar c, int e){
            ////////////when creating a node, user only needs to input a calendar object and a emotion icon/////////////////
            cal = c;
            emotion_count=e;
            Year = cal.get(Calendar.YEAR);
            Month = cal.get(Calendar.MONTH)+1;
            Date = cal.get(Calendar.DATE);
            hr = cal.get(Calendar.HOUR_OF_DAY);
            min = cal.get(Calendar.MINUTE);

        }

    public Calendar getCal() {
        return cal;
    }



    public int getMonth() {
        return Month;
    }



    public int getDate() {
        return Date;
    }



    public int getHr() {
        return hr;
    }

    public int getYear() {
        return Year;
    }

    public int getMin() {
        return min;
    }

    public int getEmotion_count(){
            return emotion_count;
    }

    public int getEmotion_icon() {
        if (emotion_count == 1){
            return R.drawable.lightbulb_good;
        }else if(emotion_count ==2 ){
            return R.drawable.lightbulb_bad_painted;
        }else{
            return 0;
        }
    }




}




