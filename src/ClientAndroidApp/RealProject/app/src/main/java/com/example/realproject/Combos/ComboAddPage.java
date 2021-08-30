package com.example.realproject.Combos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.realproject.ComboAddAdapter.ComboAddAdapter;
import com.example.realproject.Items.Items;
import com.example.realproject.Login.LoginPage;
import com.example.realproject.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComboAddPage extends AppCompatActivity {

    private TextInputLayout groupNameTil;

    private Boolean started = false;
    private Runnable checkIfResponse;
    private Button addNewGroup;
    private ListView applianceListview;
    private ComboAddAdapter programAdapter;
    private ArrayList<Boolean> isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_add_page);

        isChecked =new ArrayList<>();
        ComboPage.options = new ArrayList<>();
        ComboPage.checkedAppliances = new ArrayList<>();
        ComboPage.checkedApplianceInt = new ArrayList<>();
        ComboPage.options.add("On");
        ComboPage.options.add("Off");

        groupNameTil = findViewById(R.id.textInputComboName);
        addNewGroup= findViewById(R.id.button_add_new_group);
        applianceListview = findViewById(R.id.listview_combo_add);

        /*applianceListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkedApplianceInt.contains(position)){
                    checkedAppliances.remove(Items.username.get(position));
                }
                else {
                    checkedAppliances.add(Items.username.get(position));
                }
            }
        });*/

        for (int i = 0; i < Items.area.size(); i++) {
            isChecked.add(false);
        }

        ArrayAdapter arScenarioOn = new ArrayAdapter(this, R.layout.layout_dropdown_add,  ComboPage.options.toArray());

        programAdapter = new ComboAddAdapter(this, Items.area,Items.progImag, isChecked,arScenarioOn,null);
        applianceListview.setAdapter(programAdapter);
    }
    public void onClickAddGroup(View view){
        if (view.getId()==addNewGroup.getId()){
            String groupName  = groupNameTil.getEditText().getText().toString();

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













        }




    }

    public void sendAddGroup(String name, ArrayList<ComboItem> checkedAppliances) {
        try {


            String jsonComboItem="";
            //String jsonNamesArrayString="{names:";
            JSONArray jsonComboArray = new JSONArray();

            //create each item and insert into the array json combo.
            //to create the name the for the array insert into jsonNameArrayString, which will become jsonNameArray.
            //so jsonComboArray is jsonNameArray without the value of name.
            for (ComboItem item: checkedAppliances){
                    String us = item.getUsername();
                    String scenarioOn = item.getScenarioOn();
                    String scenarioOff = item.getScenarioOff();


                    jsonComboItem = "{username:" + us + ", onScenario :" + scenarioOn + ", offScenario: " + scenarioOff +"}";
                    JSONObject jsonCombo = new JSONObject(jsonComboItem);

                    jsonComboArray.put(jsonCombo);

            }

            //jsonNamesArrayString=jsonNamesArrayString+ jsonComboArray.toString()+"}";
            //JSONObject jsonNamesArray = new JSONObject( jsonNamesArrayString);


            JSONObject toSend =new JSONObject();
            toSend.put("messageType","group");
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
            {
                LoginPage.ws.send(toSend.toString());

            }







        } catch (JSONException e) {

           Log.e("json error",e.toString());

        }


    }
}