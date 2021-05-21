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
        sp = getActivity().getApplicationContext().getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);

        ///////////////////define latency spinner////////////////////////////////////
        View v = inflater.inflate(R.layout.fragment_menu,container,false);
        latency_spinner = v.findViewById(R.id.latency_spinner);
        ArrayAdapter<CharSequence> latency_adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.latency_selection, R.layout.latency_selection_viewresource);
        latency_adapter.setDropDownViewResource(R.layout.latency_selection_viewresource);
        latency_spinner.setAdapter(latency_adapter);
        latency_spinner.setOnItemSelectedListener(this);

        latency_spinner.setSelection(latency_identify_position(sp));

        ///////////////////intrustion dialog fragment/////////////////////////////////////
        instruction_button = v.findViewById(R.id.instruction_button);
        View.OnClickListener instructionlistener= new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                instruction_class instruction_dialogfrag = new instruction_class();
                instruction_dialogfrag.show(getActivity().getSupportFragmentManager(), "Instruction fragment");

            }
        };
        instruction_button.setOnClickListener(instructionlistener);


        //////////////////credits dialog fragment////////////////////////////////////////
        contributions_credit_button = v.findViewById(R.id.contributions_credits);
        contributions_credit_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                credits_class credits_class = new credits_class();
                credits_class.show(getActivity().getSupportFragmentManager(),"credits dialogfragment");
            }
        });


        //////////////////terms_policies dialog fragment////////////////////////////////////////
        terms_policies_button = v.findViewById(R.id.terms_policies_button);
        terms_policies_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                terms_policies_fragment terms_policies_fragment = new terms_policies_fragment();
                terms_policies_fragment.show(getActivity().getSupportFragmentManager(),"terms_policies dialogfragment");
            }
        });


        //////////////////Latency description////////////////////////////////////////
        latency_info_button = v.findViewById(R.id.latency_information_button);
        latency_info_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                latency_description_class latency_des_class= new latency_description_class();
                latency_des_class.show(getActivity().getSupportFragmentManager(),"latency_description");
            }
        });


        /////////////////notification switch//////////////////////////////////////////
        notification_switch = v.findViewById(R.id.notification_switch);
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                speditor = sp.edit();
                if(isChecked){
                    speditor.putBoolean("Notification_onoroff", true);
                }else{
                    speditor.putBoolean("Notification_onoroff", false);
                }
                speditor.commit();
            }
        });
        notification_switch.setChecked(sp.getBoolean("Notification_onoroff",true));
        return v;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sp = getActivity().getApplicationContext().getSharedPreferences("user_setting_sharepreference", Context.MODE_PRIVATE);
        speditor = sp.edit();
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

    /////////////////////////////helper function////////////////////////////
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


