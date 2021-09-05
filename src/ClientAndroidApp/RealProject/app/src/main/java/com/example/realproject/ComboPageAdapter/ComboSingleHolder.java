package com.example.realproject.ComboPageAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.realproject.R;

public class ComboSingleHolder {

    TextView title;
    SwitchCompat progCheck;

    ComboSingleHolder(View v){

        title = v.findViewById(R.id.textView_combo_name);
        progCheck =  v.findViewById(R.id.switchCombo);
    }



}
