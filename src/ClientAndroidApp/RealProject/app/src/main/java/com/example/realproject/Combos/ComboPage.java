package com.example.realproject.Combos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.realproject.ComboPageAdapter.ComboArrayAdapter;
import com.example.realproject.Items.Items;
import com.example.realproject.R;

import java.util.ArrayList;

public class ComboPage extends AppCompatActivity {

    private ArrayList<String>  title;
    private ArrayList<Boolean> isChecked;
    private ListView comboListView;
    private Intent editComboIntent;
    private int positionSaver;
    private Button comboAdd,back2Items;
    private ComboArrayAdapter programAdapter;
    public static ArrayList<String> options;
    public static ArrayList<ComboItem> checkedAppliances;
    public static ArrayList<Integer> checkedApplianceInt;
    public static Boolean finishBool = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combos);

        title =new ArrayList<>();
        isChecked = new ArrayList<>();

        back2Items = findViewById(R.id.button_back2Items);
        comboAdd=findViewById(R.id.button_add_combos);
        comboListView = findViewById(R.id.listView_Combos);



        editComboIntent = new Intent(this, ComboInfo.class);

        comboListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSaver = position;
                editComboIntent.putExtra("title",title.get(position));

                //editComboIntent.putExtra("picture",progImag.get(position));
                //editComboIntent.putExtra("username",username.get(position));
                startActivityForResult(editComboIntent, Items.ItemInfo);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        programAdapter = new ComboArrayAdapter(this, title, isChecked);
        comboListView.setAdapter(programAdapter);

        //addItemToList("Dan",  false);
        //addItemToList("Guy",  false);


    }

    public void onClickCombo(View view) {
        if (view.getId() == comboAdd.getId()) {
            Intent itemsAct = new Intent(this, ComboAddPage.class);
            startActivity(itemsAct);

        }
        else if(view.getId()== back2Items.getId()){

            finish();
        }


    }
    public void addItemToList(String titleString,boolean checked) {

        title.add(titleString);
        isChecked.add(checked);
        programAdapter.notifyDataSetChanged();



    }

    @Override
    protected void onResume() {
        super.onResume();

        title.clear();
        isChecked.clear();
        for (String groupName: Items.title)
        {
            title.add(groupName);
            isChecked.add(false);
        }
        programAdapter.notifyDataSetChanged();
    }
}