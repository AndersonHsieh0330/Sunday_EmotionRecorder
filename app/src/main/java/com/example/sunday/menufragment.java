package com.example.sunday;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class menufragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner latency_spinner;
    Switch notification_switch;
    Button instruction_button;
    Button contributions_credit_button;
    Button terms_policies_button;
    ImageButton latency_info_button;
    SharedPreferences sp;
    SharedPreferences.Editor speditor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getApplicationContext().getSharedPreferences(MainActivity.USER_SETTING_TAG, Context.MODE_PRIVATE);
        speditor = sp.edit();
        return inflater.inflate(R.layout.fragment_menu,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        latency_spinner_definition();
        popup_dialogs_definition();
        notification_switch_definition();
    }

    private void latency_spinner_definition(){
        latency_spinner = getView().findViewById(R.id.latency_spinner);
        ArrayAdapter<CharSequence> latency_adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.latency_selection, R.layout.latency_selection_viewresource);
        latency_adapter.setDropDownViewResource(R.layout.latency_selection_viewresource);
        latency_spinner.setAdapter(latency_adapter);
        latency_spinner.setOnItemSelectedListener(this);
        latency_spinner.setSelection(latency_identify_position(sp));
    }
    private void popup_dialogs_definition(){
        ///////////////////intrustion dialog fragment/////////////////////////////////////
        instruction_button = getView().findViewById(R.id.instruction_button);
        instruction_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop_instructions_dialogfragment();
            }
        });


        //////////////////credits dialog fragment////////////////////////////////////////
        contributions_credit_button = getView().findViewById(R.id.contributions_credits);
        contributions_credit_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                pop_credits_dialogfragment();
            }
        });


        //////////////////terms_policies dialog fragment////////////////////////////////////////
        terms_policies_button = getView().findViewById(R.id.terms_policies_button);
        terms_policies_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                pop_termsnpolicies_dialogfragment();
            }
        });


        //////////////////Latency description////////////////////////////////////////
        latency_info_button = getView().findViewById(R.id.latency_information_button);
        latency_info_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                pop_latency_description_dialogfragment();
            }
        });

    }
    private void pop_instructions_dialogfragment(){
        instruction_class instruction_dialogfrag = new instruction_class();
        instruction_dialogfrag.show(getActivity().getSupportFragmentManager(), MainActivity.DIALOGFRAGMENT_TAG_INSTRUCTIONS);
    }
    private void pop_credits_dialogfragment(){
        credits_class credits_class = new credits_class();
        credits_class.show(getActivity().getSupportFragmentManager(),MainActivity.DIALOGFRAGMENT_TAG_CREDITS);
    }
    private void pop_termsnpolicies_dialogfragment(){
        terms_policies_fragment terms_policies_fragment = new terms_policies_fragment();
        terms_policies_fragment.show(getActivity().getSupportFragmentManager(),MainActivity.DIALOGFRAGMENT_TAG_TERMSNPOLICIES);
    }

    private void pop_latency_description_dialogfragment(){
        latency_description_class latency_des_class= new latency_description_class();
        latency_des_class.show(getActivity().getSupportFragmentManager(),MainActivity.DIALOGFRAGMENT_TAG_LATENCY);
    }
    private void notification_switch_definition(){
        notification_switch = getView().findViewById(R.id.notification_switch);
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                speditor = sp.edit();
                speditor.putBoolean(MainActivity.NOTIFICATION_STATUS_SHAREPREFERENCE_TAG, isChecked);
                speditor.commit();
            }
        });
        notification_switch.setChecked(sp.getBoolean(MainActivity.NOTIFICATION_STATUS_SHAREPREFERENCE_TAG,true));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    speditor.putInt("Latency", 600000);
                    break;
                case 1:
                    speditor.putInt("Latency", 1800000);
                    break;
                case 2:
                    speditor.putInt("Latency", 3600000);
                    break;
                case 3:
                    speditor.putInt("Latency", 10800000);
                    break;

            }

            speditor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public int latency_identify_position(SharedPreferences sp){
        switch (sp.getInt("Latency",0)){
            case 600000:
                return 0;
            case 1800000:
                return 1;
            case 3600000:
                return 2;
            case 10800000:
                return 3;
        }
        return 0;
    }
}


