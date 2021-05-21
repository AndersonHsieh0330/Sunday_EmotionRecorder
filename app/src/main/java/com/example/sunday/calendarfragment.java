package com.example.sunday;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class calendarfragment extends Fragment {

    private CalendarView calendarView;
    private dbhelper sqlitehelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        sqlitehelper = new dbhelper(this.getActivity());

        PieChart mPieChart = (PieChart) v.findViewById(R.id.piechart);
        calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mPieChart.clearChart();

                if((0!=sqlitehelper.get_date_happyemo(year, month, dayOfMonth))&&(0!=sqlitehelper.get_date_unhappyemo(year, month, dayOfMonth))) {
                    mPieChart.addPieSlice(new PieModel("Happy!", sqlitehelper.get_date_happyemo(year, month, dayOfMonth), Color.parseColor("#FEDB41")));
                    mPieChart.addPieSlice(new PieModel("UnHappy:(", sqlitehelper.get_date_unhappyemo(year, month, dayOfMonth), Color.parseColor("#FF0000")));

                }else{
                    mPieChart.addPieSlice(new PieModel("No Data For This Date", 0, Color.parseColor("#FFFFFF")));

                }

                mPieChart.startAnimation();


            }


        });


        return v;

    }
}
