package com.example.realproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramViewHolder {
    ImageView itemImage;
    TextView progTitle;
    TextView progDesc;

    ProgramViewHolder (View v){
      itemImage = v.findViewById(R.id.imageView2);
      progTitle = v.findViewById(R.id.textView1);
      progDesc = v.findViewById(R.id.textView2);


    }



}
