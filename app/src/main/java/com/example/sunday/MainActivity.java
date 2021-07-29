package com.example.sunday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    Fragment lightbulb_fragment = new lightbulbfragment();
    Fragment calendar_fragment = new calendarfragment();
    Fragment menu_fragment = new menufragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment selectedfragment;
    int fragment_page_count;
    SharedPreferences sp;
    public static final String MAINACTIVITY_DESTROYED_TAG = "mainact_ondestroy_was_called";
    public static final String USER_SETTING_TAG = "user_setting_sharepreference";
    public static final String NOTIFICATION_STATUS_SHAREPREFERENCE_TAG = "Notification_onoroff";
    public static final String DIALOGFRAGMENT_TAG_INSTRUCTIONS ="Instruction fragment";
    public static final String DIALOGFRAGMENT_TAG_CREDITS = "credits dialogfragment";
    public static final String DIALOGFRAGMENT_TAG_TERMSNPOLICIES ="terms_policies dialogfragment";
    public static final String DIALOGFRAGMENT_TAG_LATENCY ="latency_description";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = this.getApplicationContext().getSharedPreferences(USER_SETTING_TAG, Context.MODE_PRIVATE);
        Define_BottonNavView();


    }
    private void Define_BottonNavView(){
        BottomNavigationView BOTNAVVIEW = findViewById(R.id.bottomNavigationView);
        BOTNAVVIEW.setOnNavigationItemSelectedListener(bnavlistener);
        selectedfragment = lightbulb_fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.ButtonNav_container, selectedfragment).commit();
        fragment_page_count = 2;
    }

    @Override
    protected void onDestroy() {
        sp.edit().putBoolean(MAINACTIVITY_DESTROYED_TAG,true).commit();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public BottomNavigationView.OnNavigationItemSelectedListener bnavlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            selectedfragment = null;
            switch (item.getItemId()) {
                case R.id.lightbulb_icon:
                    selectedfragment = lightbulb_fragment;
                    break;
                case R.id.calendar_icon:
                    selectedfragment = calendar_fragment;
                    break;
                case R.id.menu_icon:
                    selectedfragment = menu_fragment;
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.ButtonNav_container, selectedfragment, "currentfrag").commit();
            return true;


        }


    };


}