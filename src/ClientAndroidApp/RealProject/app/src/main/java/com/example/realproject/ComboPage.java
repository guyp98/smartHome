package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

public class ComboPage extends AppCompatActivity {

    private ArrayList<String> title;
    private ArrayList<Boolean> isChecked;
    private ListView comboListView;
    private Intent editComboIntent;
    private int positionSaver;
    private Button comboAdd;
    private ComboArrayAdapter programAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combos);
        title = new ArrayList<>();
        isChecked = new ArrayList<>();



        //editComboIntent = new Intent(this, ItemInfo.class);
        comboAdd=findViewById(R.id.button_add_combos);
        comboListView = findViewById(R.id.listView_Combos);
        comboListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSaver = position;
                //editComboIntent.putExtra("areaInput",area.get(position));
                //editComboIntent.putExtra("picture",progImag.get(position));
                //editComboIntent.putExtra("username",username.get(position));
                startActivityForResult(editComboIntent, Items.ItemInfo);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        programAdapter = new ComboArrayAdapter(this, title, isChecked);
        comboListView.setAdapter(programAdapter);

        //addItemToList("Dan", "Light", 2, "0x0CFF3D", false);


    }

    public void onClickCombo(View view) {
        if (view.getId() == comboAdd.getId()) {
            addItemToList("Dan",false);
            addItemToList("Guy",false);
        }


    }
    public void addItemToList(String titleString,boolean checked) {

        title.add(titleString);
        isChecked.add(checked);
        programAdapter.notifyDataSetChanged();



    }
}