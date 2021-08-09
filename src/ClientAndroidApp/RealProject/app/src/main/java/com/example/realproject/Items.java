package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Items extends AppCompatActivity {

    public static final int AddScreen=1,EditItems=2;
    private ArrayList<String> area, desc;
    private ArrayList<Integer> progImag, idList;
    private String areaString, descString, state;
    private ListView itemsListView;
    private boolean canRemove;
    private Intent editItemsIntent;
    private int positionSaver,loadedIndex,idInt;
    //SharedPreferences sharedPreferences;
    //haredPreferences.Editor editor;
    private ProgramAdapter programAdapter;
    int dataSize;
    int index = 0;


    //Will save the size variable, each item key will be its index and type/area. eg: 1type:Light , 1area:Kitchen , therefore just loop until size and extract all keys.
    public static final String myData = "myData";
    public static final String itemSize = "Size";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_items);

            area = new ArrayList<>();
            desc = new ArrayList<>();
            progImag = new ArrayList<>();
            idList = new ArrayList<>();


            JSONObject jsonObject = new JSONObject(LoginPage.store);
            String boo = jsonObject.getString("success");
            if (boo.equals("true")) {
                JSONArray jsonArray = jsonObject.getJSONArray("appliances");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject applian = jsonArray.getJSONObject(i);
                    idInt = applian.getInt("id");
                    String details= applian.getString("detail");
                    JSONObject jsonSingleAppliance = new JSONObject(details);
                    areaString = jsonSingleAppliance.getString("area");
                    descString = jsonSingleAppliance.getString("desc");
                    //state = jsonSingleAppliance.getString("state");

                    addItemToList(areaString, descString,idInt);
                }
            }
            editItemsIntent = new Intent(this,EditItem.class);
            itemsListView = findViewById(R.id.listViewItems);
            itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    positionSaver = position;
                    editItemsIntent.putExtra("areaInput",area.get(position));
                    editItemsIntent.putExtra("picture",progImag.get(position));
                    startActivityForResult(editItemsIntent, Items.EditItems);

                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
            programAdapter = new ProgramAdapter(this, area, progImag, desc);
            itemsListView.setAdapter(programAdapter);

            area.add("Dan");
            desc.add("Light");
            progImag.add(R.drawable.picture_bulb_2_small);
            programAdapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changeName (int position, String areaString ){
        area.set(position,areaString);
        programAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Items.AddScreen) {
            if (resultCode == RESULT_OK) {
                String areaString = data.getStringExtra("area");
                String typeString = data.getStringExtra("type");
                int id = data.getIntExtra("id", -1);
                addItemToList(areaString, typeString, id);
                programAdapter.notifyDataSetChanged();
                dataSize++;


            }
        }
        if (requestCode == Items.EditItems) {
            if (resultCode == RESULT_OK) {
                Boolean changed = data.getBooleanExtra("changed",false);
                if(changed) {
                    String areaString = data.getStringExtra("area");
                    changeName(positionSaver, areaString);
                }


            }
        }
    }


    public void onButtonClickItems(View view) throws JSONException {
        if (view.getId() == R.id.buttonAdd) {
            Intent itemsAct = new Intent(Items.this, AddScreen.class);
            startActivityForResult(itemsAct, Items.AddScreen);

        }
        else if (view.getId() == R.id.buttonRemove && positionSaver != -1) {

            String jsonRemoveAppliaceStr;
            if(LoginPage.testing)
                jsonRemoveAppliaceStr= "{messageType:removeApplianceResponse, removed:true }";
            else
                jsonRemoveAppliaceStr= "{messageType:removeAppliance, id:" + idList.get(positionSaver) + "}";
            JSONObject jsonRemoveAppliance = new JSONObject(jsonRemoveAppliaceStr);
            LoginPage.ws.send(jsonRemoveAppliance.toString());

            Handler handler = new Handler();
            checkIfLoaded(handler);


            area.remove(positionSaver);
            progImag.remove(positionSaver);
            desc.remove(positionSaver);
            idList.remove(positionSaver);
            //todo notify Guy
            positionSaver = -1;
            programAdapter.notifyDataSetChanged();
        }
        //on click invisible
        else
        {
            positionSaver= view.getId();

        }


    }


    public void checkIfLoaded(Handler handler) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!LoginPage.store.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(LoginPage.store);
                        String boo = jsonObject.getString("removed");
                        if (boo.equals("true")) {
                            canRemove = true;
                        } else
                            canRemove = false;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (loadedIndex < 300) {
                        loadedIndex++;
                        checkIfLoaded(handler);
                    }
                    //else
                    //  errorLoading.setText("Could not load, please check internet connection");
                }
            }
        }, 30);
    }



    public void addItemToList(String areaString, String descString, int id) {
        area.add(areaString);
        desc.add(descString);
        idList.add(id);
        if (descString.equals("Light"))
            progImag.add(R.drawable.picture_bulb_2_small);
        /*else if (descString.equals("Water Heater"))
            progImag.add(R.drawable.picture_boiling_water);*/
        else
            progImag.add(R.drawable.picture_boiling_water);



    }
}