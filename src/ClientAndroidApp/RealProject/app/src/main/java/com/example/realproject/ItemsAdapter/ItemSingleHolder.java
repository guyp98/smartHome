package com.example.realproject.ItemsAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.realproject.R;

public class ItemSingleHolder {
    ImageView itemImage;
    TextView progTitle;
    TextView progDesc;
    SwitchCompat progCheck;

    ItemSingleHolder(View v){
      itemImage = v.findViewById(R.id.imageView2);
      progTitle = v.findViewById(R.id.textView1);
      progDesc = v.findViewById(R.id.textView2);
      progCheck =  v.findViewById(R.id.switchAplliance);
    }



}
