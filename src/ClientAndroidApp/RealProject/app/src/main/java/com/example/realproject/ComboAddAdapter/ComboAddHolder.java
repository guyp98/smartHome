package com.example.realproject.ComboAddAdapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.realproject.R;

public class ComboAddHolder {

    ImageView itemImage;
    TextView title;
    CheckBox progCheck;
    Spinner dropDownScenarioOn;

    ComboAddHolder(View v){

        title = v.findViewById(R.id.textView_title_group);
        progCheck =  v.findViewById(R.id.checkBox);
        itemImage = v.findViewById(R.id.imageView_group);
        dropDownScenarioOn=v.findViewById(R.id.spinner_scenario);
    }

}
