package com.example.realproject.Combos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.realproject.ComboAddAdapter.ComboAddAdapter;
import com.example.realproject.Items.Items;
import com.example.realproject.Login.LoginPage;
import com.example.realproject.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComboEdit extends AppCompatActivity {

    private TextInputLayout groupNameTil;
    private Boolean started = false;
    private Runnable checkIfResponse;
    private Button save;
    private ListView applianceListview;
    private ComboAddAdapter programAdapter;
    private ArrayList<Boolean> isChecked;
    private String name;
    private Spinner sp;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_edit);

        save = findViewById(R.id.button_save);
        groupNameTil = findViewById(R.id.textInputComboNameEdit);
        applianceListview = findViewById(R.id.listview_combo_edit);
        sp= findViewById(R.id.spinner_scenario);

        isChecked =new ArrayList<>();
        ComboPage.options = new ArrayList<>();
        ComboPage.checkedAppliances = new ArrayList<>();
        ComboPage.checkedApplianceInt = new ArrayList<>();
        ComboPage.options.add("On");
        ComboPage.options.add("Off");

        Intent intent = getIntent();
        name = intent.getStringExtra("area");




        for (int i = 0; i < Items.area.size(); i++) {
            isChecked.add(false);
        }

        ArrayAdapter arScenarioOn = new ArrayAdapter(this, R.layout.layout_dropdown_add,  ComboPage.options.toArray());

        programAdapter = new ComboAddAdapter(this, Items.area,Items.progImag, isChecked,arScenarioOn);
        applianceListview.setAdapter(programAdapter);


        groupNameTil.getEditText().setText(name);


       for (int i = 0; i < programAdapter.getCount(); i++) {
            Spinner d = programAdapter.getView(i,null,applianceListview).findViewById(R.id.spinner_scenario);
            TextView dview = programAdapter.getView(i,null,applianceListview).findViewById(R.id.textView_title_group);
            dview.setText("I win");
           //applianceListview.getChildAt(i).findViewById(R.id.spinner_scenario);
           d.setSelection(2);
        }



    }

    public void onClickSave(View view) {
        if (view.getId() == save.getId()) {
            for (int i = 0; i < programAdapter.getCount(); i++) {
                Spinner d = programAdapter.getView(i,null,applianceListview).findViewById(R.id.spinner_scenario);
                int spin = d.getId();
                //applianceListview.getChildAt(i).findViewById(R.id.spinner_scenario);
                d.setSelection(3);
                TextView dview = programAdapter.getView(i,null,applianceListview).findViewById(R.id.textView_title_group);
                dview.setText("I win");
            }
            /*String groupName  = groupNameTil.getEditText().getText().toString();

            sendAddGroup(groupName,ComboPage.checkedAppliances);


            checkIfResponse = new Runnable() {
                @Override
                public void run() {

                    started=false;
                    for (int i = 0; i < LoginPage.threadCycle & !started; i++) {

                        try {
                            Thread.sleep(LoginPage.threadSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!LoginPage.store.isEmpty()) {
                            started = true;
                            try {
                                JSONObject jsonObject = new JSONObject(LoginPage.store);
                                String boo = jsonObject.getString("success");
                                if (boo.equals("true")) {
                                    Items.title.add(groupName);
                                    Items.groups.put(groupName,ComboPage.checkedAppliances);
                                    finish();
                                } else {
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    finish();
                }



            };
            Thread itemsActThread = new Thread(checkIfResponse);
            itemsActThread.start();







*/
        }
    }



    public void sendAddGroup(String name, ArrayList<ComboItem> checkedAppliances) {
        try {

            String jsonComboItem="";
            JSONArray jsonComboArray = new JSONArray();
            //create each item and insert into the array json combo.

            for (ComboItem item: checkedAppliances){

                String us = item.getUsername();
                String scenarioOn = item.getScenarioOn();
                String scenarioOff = item.getScenarioOff();

                jsonComboItem = "{username:" + us + ", onScenario :" + scenarioOn + ", offScenario: " + scenarioOff +"}";
                JSONObject jsonCombo = new JSONObject(jsonComboItem);
                jsonComboArray.put(jsonCombo);

            }

            JSONObject toSend =new JSONObject();
            toSend.put("messageType","editGroup");
            toSend.put("groupName",name);
            toSend.put("names",jsonComboArray);
            toSend.put("action","newGroup");


            if(LoginPage.testing)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("messageType","groupResponse");
                jsonObject.put("success",true);
                LoginPage.store=jsonObject.toString();
            }
            else
                LoginPage.ws.send(toSend.toString());
        } catch (JSONException e) {

            Log.e("json error",e.toString());

        }


    }
}