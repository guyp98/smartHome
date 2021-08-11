package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemInfo extends AppCompatActivity {

    private TextView name;
    private ImageView image;
    private int imageInt;
    private Button back, edit;
    private boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        back = findViewById(R.id.button_items_back);
        edit = findViewById(R.id.button_edit_item);
        name = findViewById(R.id.textView_appliance_name);
        image = findViewById(R.id.imageView_edititem);
        Intent intent = getIntent();
        String nameString = intent.getStringExtra("areaInput");
        imageInt = intent.getIntExtra("picture", -1);
        name.setText(nameString);
        image.setImageResource(imageInt);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void OnClickInfo(View view) {
        if (view.getId() == back.getId()) {
            //add info
            Intent resultIntent = new Intent();
            //resultIntent.putExtra("type", typeString);
            resultIntent.putExtra("area", name.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        if (view.getId() == edit.getId()) {
            Intent editItemsIntent = new Intent(this, EditItem.class);
            editItemsIntent.putExtra("area", name.getText().toString());
            editItemsIntent.putExtra("picture",imageInt);
            startActivityForResult(editItemsIntent, Items.EditItem);


        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Items.EditItem) {
            if (resultCode == RESULT_OK) {
                String areaString = data.getStringExtra("area");
                name.setText(areaString);
            }
        }
    }

}