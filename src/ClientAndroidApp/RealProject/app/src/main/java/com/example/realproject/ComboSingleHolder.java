package com.example.realproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

public class ComboSingleHolder {

    TextView title;
    SwitchCompat progCheck;

    ComboSingleHolder(View v){

        title = v.findViewById(R.id.textView_combo_name);
        progCheck =  v.findViewById(R.id.switchCombo);
    }



}
