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

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    Fragment lightbulb_fragment = new lightbulbfragment();
    Fragment calendar_fragment = new calendarfragment();
    Fragment menu_fragment = new menufragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment selectedfragment;
    int Slide_in_animation;
    int Slide_out_animation;
    int fragment_page_count;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = this.getApplicationContext().getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);
        BottomNavigationView BOTNAVVIEW = findViewById(R.id.bottomNavigationView);
        BOTNAVVIEW.setOnNavigationItemSelectedListener(bnavlistener);
        selectedfragment = lightbulb_fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.ButtonNav_container, selectedfragment).commit();
        fragment_page_count = 2;



    }

    @Override
    protected void onDestroy() {
        sp.edit().putBoolean("mainact_ondestroy_was_called",true).commit();
        super.onDestroy();

    }

    public BottomNavigationView.OnNavigationItemSelectedListener bnavlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            selectedfragment = null;
            switch (item.getItemId()) {
                case R.id.lightbulb_icon:
                    selectedfragment = lightbulb_fragment;
//                    if (fragment_page_count < 2) {
//                        Slide_in_animation = R.anim.slide_in_right;
//                        Slide_out_animation = R.anim.slide_out_left;
//                    } else if (fragment_page_count > 2) {
//                        Slide_in_animation = R.anim.slide_in_left;
//                        Slide_out_animation = R.anim.slide_out_right;
//                    } else {
//                        Log.d("animation", "first error");
//                    }
//                    fragment_page_count = 2;

                    break;
                case R.id.calendar_icon:
                    selectedfragment = calendar_fragment;
//                    Slide_in_animation = R.anim.slide_in_right;
//                    Slide_out_animation = R.anim.slide_out_left;
//                    fragment_page_count = 3;
                    break;
                case R.id.menu_icon:
                    selectedfragment = menu_fragment;
//                    Slide_in_animation = R.anim.slide_in_left;
//                    Slide_out_animation = R.anim.slide_out_right;
//                    fragment_page_count = 1;
                    break;
            }

            /////////////////////.setCustomAnimations(Slide_in_animation, Slide_out_animation)////////////////////////
            /////////////////////temporary removed////////////////////
            fragmentManager.beginTransaction().replace(R.id.ButtonNav_container, selectedfragment, "currentfrag").commit();

            Log.d("animation", selectedfragment + String.valueOf(selectedfragment.isVisible()));


            return true;


        }


    };


}