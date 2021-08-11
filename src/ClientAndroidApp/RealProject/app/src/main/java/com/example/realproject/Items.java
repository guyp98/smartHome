package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Items extends AppCompatActivity {

    public static final int AddScreen=1,ItemInfo=2,EditItem=3;
    private ArrayList<String> area, desc, username;
    private ArrayList<Integer> progImag, idList;
    private ArrayList<Boolean> isChecked;
    private String areaString, descString;
    private ListView itemsListView;
    private boolean canRemove;
    private Intent editItemsIntent;
    private HashMap<String ,Boolean> usernameSwitch;
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
            isChecked = new ArrayList<>();
            usernameSwitch = new HashMap<>();

            isChecked.add(true);
            isChecked.add(true);





            JSONObject jsonObject = new JSONObject(LoginPage.store);
            String boo = jsonObject.getString("success");
            if (boo.equals("true")) {

                //get the username-boolean hashmap. then when reading the full object add the relevent boolean
              JSONArray switchArray = jsonObject.getJSONArray("predicament");
                for (int i = 0; i < switchArray.length(); i++) {
                    JSONObject switchSingle=switchArray.getJSONObject(i);
                    usernameSwitch.put(switchSingle.getString("username"),switchSingle.getBoolean("state"));
                    username.add(switchSingle.getString("username"));
                }

                JSONArray jsonArray = jsonObject.getJSONArray("appliances");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject applian = jsonArray.getJSONObject(i);
                    idInt = applian.getInt("id");
                    String usernameString = applian.getString("username");
                    String details= applian.getString("detail");
                    JSONObject jsonSingleAppliance = new JSONObject(details);
                    areaString = jsonSingleAppliance.getString("area");
                    descString = jsonSingleAppliance.getString("desc");


                    isChecked.add(usernameSwitch.get(usernameString));
                    addItemToList(areaString, descString,idInt,usernameString);
                }
            }
            editItemsIntent = new Intent(this, ItemInfo.class);
            itemsListView = findViewById(R.id.listViewItems);
            itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    positionSaver = position;
                    editItemsIntent.putExtra("areaInput",area.get(position));
                    editItemsIntent.putExtra("picture",progImag.get(position));
                    startActivityForResult(editItemsIntent, Items.ItemInfo);

                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
            programAdapter = new ProgramAdapter(this, area, progImag, desc,isChecked,username);
            itemsListView.setAdapter(programAdapter);

            area.add("Dan");
            desc.add("Light");
            progImag.add(R.drawable.picture_bulb_2_small);
            area.add("Guy");
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
                //todo not run!!!
                addItemToList(areaString, typeString, id,"ChangeMe");
                programAdapter.notifyDataSetChanged();


            }
        }
        if (requestCode == Items.ItemInfo) {
            if (resultCode == RESULT_OK) {
                String areaString = data.getStringExtra("area");
                changeName(positionSaver, areaString);
                programAdapter.notifyDataSetChanged();

            }


            }
        }
    public void getView(final int position, View convertView, ViewGroup parent) {


        if(convertView.getId()==itemsListView.getId()){
            System.out.println("dan Rules");


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



    public void addItemToList(String areaString, String descString, int id,String usernameString) {
        area.add(areaString);
        desc.add(descString);
        username.add(usernameString);
        idList.add(id);
        if (descString.equals("Light"))
            progImag.add(R.drawable.picture_bulb_2_small);
        /*else if (descString.equals("Water Heater"))
            progImag.add(R.drawable.picture_boiling_water);*/
        else
            progImag.add(R.drawable.picture_boiling_water);



    }

}