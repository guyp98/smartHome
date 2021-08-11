package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditItem extends AppCompatActivity {
    private EditText nameEdit;
    private ImageView image;
    private String name;
    private int imageInt;
    private Button back2Info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Intent intent =getIntent();
        name = intent.getStringExtra("area");
        nameEdit = findViewById(R.id.edittext_edit_item_name);
        image=findViewById(R.id.imageView_edititem);
        nameEdit.setText(name);
        back2Info = findViewById(R.id.button_items_back2);
        imageInt = intent.getIntExtra("picture", -1);
        image.setImageResource(imageInt);
    }


    public void onClickEdit(View view){
            if(view.getId()==back2Info.getId()){

                Intent resultIntent = new Intent();
                //resultIntent.putExtra("type", typeString);
                resultIntent.putExtra("area", nameEdit.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();


            }






    }
}