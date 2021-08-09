package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EditItem extends AppCompatActivity {

     private TextView name;
     private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        name = findViewById(R.id.textView_appliance_name);
        image = findViewById(R.id.imageView_appliance_kind);
        Intent intent = getIntent();
        String nameString = intent.getStringExtra("areaInput");
        int imageInt = intent.getIntExtra("picture",-1);
        name.setText(nameString);
        image.setImageResource(imageInt);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void back (View view){
        finish();
    }
}