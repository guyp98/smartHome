package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

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
    public static Vibrator vItem;
    private ListView itemsListView;
    private Intent editItemsIntent;
    private HashMap<String ,Boolean> usernameSwitch;
    private int positionSaver,loadedIndex=0,idInt;
    private Boolean started=false;
    private Runnable checkIfResponse;
    //SharedPreferences sharedPreferences;
    //haredPreferences.Editor editor;
    private ItemArrayAdapter programAdapter;
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
            vItem = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            area = new ArrayList<>();
            desc = new ArrayList<>();
            progImag = new ArrayList<>();
            idList = new ArrayList<>();
            isChecked = new ArrayList<>();
            username=new ArrayList<>();
            usernameSwitch = new HashMap<>();







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
                    addItemToList(areaString, descString,idInt,usernameString,usernameSwitch.get(usernameString));
                }
            }
            editItemsIntent = new Intent(this, ItemInfo.class);
            itemsListView = findViewById(R.id.listView_items);
            itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    positionSaver = position;
                    editItemsIntent.putExtra("areaInput",area.get(position));
                    editItemsIntent.putExtra("picture",progImag.get(position));
                    editItemsIntent.putExtra("username",username.get(position));
                    startActivityForResult(editItemsIntent, Items.ItemInfo);

                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
            programAdapter = new ItemArrayAdapter(this, area, progImag, desc,isChecked,username);
            itemsListView.setAdapter(programAdapter);

            addItemToList("Dan","Light",2,"0x0CFF3D",false);
            addItemToList("Guy","Water-Heater",1,"0xD2FF3D",false);


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
                started = false;
                Log.d("dan", "reached onActivityResult");
                Boolean stateSwitch = data.getBooleanExtra("state", false);
                String areaString = data.getStringExtra("area");
                String typeString = data.getStringExtra("type");
                String usernameString = data.getStringExtra("username");

                int id = data.getIntExtra("id", -1);
                addItemToList(areaString, typeString, id, usernameString, stateSwitch);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            }
        }
        if (requestCode == Items.ItemInfo) {
            if (resultCode == RESULT_OK) {
                started = false;
                String areaString = data.getStringExtra("area");
                changeName(positionSaver, areaString);
                programAdapter.notifyDataSetChanged();

            } else if (resultCode == LoginPage.ResultRemoved) {

                removeAppliance(positionSaver);

            }


        }
    }




    public void onButtonClickItems(View view) throws JSONException {
        if (view.getId() == R.id.button_add_items) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            String jsonLoginStr;
            if (LoginPage.testing)
                jsonLoginStr = "{messageType:getAllAppliances,success:true,appliances:[{username:D33DFF,type:SmartSensor},{username:FF2DC8,type:SmartSensor}] }";
            else
                jsonLoginStr = "{messageType:getAllAppliances}";
            JSONObject jsonLogin = new JSONObject(jsonLoginStr);
            LoginPage.store = "";
            LoginPage.ws.send(jsonLogin.toString());

            Intent addScreenIntent = new Intent(Items.this, AddScreen.class);

            checkIfResponse = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < LoginPage.threadCycle & !started; i++) {
                        try {
                            Thread.sleep(LoginPage.threadSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!LoginPage.store.isEmpty()) {
                            started = true;
                            Bundle b = new Bundle();

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(LoginPage.store);
                                ArrayList<String> usernameExtra = new ArrayList<>();
                                ArrayList<String> typeExtra = new ArrayList<>();
                                String boo = jsonObject.getString("success");
                                if (boo.equals("true")) {

                                    //get the username-boolean hashmap. then when reading the full object add the relevent boolean
                                    JSONArray applianceArray = jsonObject.getJSONArray("appliances");
                                    for (int j = 0; j < applianceArray.length(); j++) {
                                        JSONObject appliaceSingle = applianceArray.getJSONObject(j);
                                        String usernameCheck = appliaceSingle.getString("username");
                                        if (!username.contains(usernameCheck)) {
                                            usernameExtra.add(usernameCheck);
                                            typeExtra.add(appliaceSingle.getString("type"));
                                        }

                                    }

                                }

                                b.putStringArrayList("username", usernameExtra);
                                b.putStringArrayList("type", typeExtra);
                                addScreenIntent.putExtra("usernameBundle", b);
                                startActivityForResult(addScreenIntent, Items.AddScreen);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }


                }


            };
            Thread itemsActThread = new Thread(checkIfResponse);
            itemsActThread.start();
            Log.d("check", "reached after thread start");
        } else if (view.getId() == R.id.buttonCombos) {

        }
        //on click invisible
        else {
            positionSaver = view.getId();
        }
    }


    public void addItemToList(String areaString, String descString, int id,String usernameString,Boolean stateSwitch) {
        area.add(areaString);
        desc.add(descString);
        username.add(usernameString);
        isChecked.add(stateSwitch);
        idList.add(id);
        if (descString.equals("Light"))
            progImag.add(R.drawable.picture_bulb_2_small);
        /*else if (descString.equals("Water Heater"))
            progImag.add(R.drawable.picture_boiling_water);*/
        else
            progImag.add(R.drawable.picture_boiler2);

        programAdapter.notifyDataSetChanged();



    }
    public void removeAppliance(int positionSaver){
        area.remove(positionSaver);
        desc.remove(positionSaver);
        username.remove(positionSaver);
        isChecked.remove(positionSaver);
        idList.remove(positionSaver);
        progImag.remove(positionSaver);

        programAdapter.notifyDataSetChanged();

    }

}