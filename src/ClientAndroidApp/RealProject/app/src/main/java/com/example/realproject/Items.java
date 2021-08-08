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

    private ArrayList<String> area, desc;
    private ArrayList<Integer> progImag, idList;
    private String areaString, descString, state;
    private ListView itemsListView;
    private boolean canRemove;
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
                    String details= applian.getString("details");
                    JSONObject jsonSingleAppliance = new JSONObject(LoginPage.store);
                    areaString = jsonSingleAppliance.getString("applianceName");
                    descString = jsonSingleAppliance.getString("applianceDescription");
                    //state = jsonSingleAppliance.getString("state");

                    addItemToList(areaString, descString,idInt);
                }
            }


            itemsListView = findViewById(R.id.listViewItems);
            itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    positionSaver = position;
                    Toast.makeText(Items.this, "Clicked at positon = " + position, Toast.LENGTH_SHORT).show();

                    Toast.makeText(Items.this, "you clicked" + position, Toast.LENGTH_SHORT).show();
                }
            });
            programAdapter = new ProgramAdapter(this, area, progImag, desc);
            itemsListView.setAdapter(programAdapter);

            area.add("Dan");
            desc.add("Light");
            progImag.add(R.drawable.picture_lightbuldyellow);
            programAdapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String areaString = data.getStringExtra("area");
                String typeString = data.getStringExtra("type");
                int id = data.getIntExtra("id", -1);
                addItemToList(areaString, typeString, id);
                programAdapter.notifyDataSetChanged();
                dataSize++;


            }
        }
    }


    public void onButtonClickItems(View view) throws JSONException {
        if (view.getId() == R.id.buttonAdd) {


            Intent itemsAct = new Intent(Items.this, AddScreen.class);
            startActivityForResult(itemsAct, 1);

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
            progImag.add(R.drawable.picture_lightbuldyellow);
        else if (descString.equals("Water Heater"))
            progImag.add(R.drawable.picture_boiling_water);


    }
}