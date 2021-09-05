package com.example.realproject.Combos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.realproject.Items.EditItem;
import com.example.realproject.Items.Items;
import com.example.realproject.R;

public class ComboInfo extends AppCompatActivity {

    private TextView itemsInCombo,comboTitle;
    private String nameString;
    private Button setTimer,comboBack,editCombo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_info);

        comboBack= findViewById(R.id.button_combo_back);
        setTimer = findViewById(R.id.button_set_timer_combo);
        editCombo = findViewById(R.id.button_edit_combo);
        comboTitle = findViewById(R.id.textView_combo_name);
        itemsInCombo = findViewById(R.id.textView_notes_combo);

        Intent intent = getIntent();
        nameString = intent.getStringExtra("title");
        String notes ="  List of appliances:";
        for (ComboItem appliance: Items.groups.get(nameString))
        {
            String name = Items.getAreaFromUsername(appliance.getUsername());
            notes= notes +"\n  -" + name;
        }
        itemsInCombo.setText(notes);

        comboTitle.setText(nameString);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ComboPage.finishBool){
            ComboPage.finishBool=false;
            finish();
        }
    }

    public void onClickInfoCombo(View view) {
        if (view.getId() == comboBack.getId()) {
            finish();
        }
        if (view.getId() == editCombo.getId()) {
            Intent editItemsIntent = new Intent(this, ComboEdit.class);
            editItemsIntent.putExtra("area", comboTitle.getText().toString());
            startActivity(editItemsIntent);
        }
    }
}