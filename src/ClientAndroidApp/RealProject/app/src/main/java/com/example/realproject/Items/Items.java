package com.example.realproject.Items;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.realproject.Combos.ComboItem;
import com.example.realproject.Combos.ComboPage;
import com.example.realproject.ItemsAdapter.ItemArrayAdapter;
import com.example.realproject.Login.LoginPage;
import com.example.realproject.MainMenu;
import com.example.realproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Items extends AppCompatActivity {

    public static final int AddScreen=1,ItemInfo=2,EditItem=3;


    private ArrayList<Boolean> isChecked;
    private String areaString, descString;
    private ListView itemsListView;
    private Intent editItemsIntent;
    private HashMap<String ,Boolean> usernameSwitch;
    private int positionSaver,loadedIndex=0;
    private Boolean started=false;
    private Runnable checkIfResponse;


    public static Vibrator vItem;
    public static HashMap<String, ArrayList<ComboItem>> groups;
    public static ArrayList<String> username, area, desc,title;
    public static ArrayList<Integer> progImag;



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
            isChecked = new ArrayList<>();
            username=new ArrayList<>();
            usernameSwitch = new HashMap<>();
            groups = new HashMap<>();
            title = new ArrayList<>();

            for (String groupName: Items.groups.keySet())
            {
                title.add(groupName);
            }



            editItemsIntent = new Intent(this, ItemInfo.class);
            itemsListView = findViewById(R.id.listView_items);
            itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    positionSaver = position;
                    editItemsIntent.putExtra("areaInput",area.get(position));
                    editItemsIntent.putExtra("desc",desc.get(position));
                    editItemsIntent.putExtra("picture",progImag.get(position));
                    editItemsIntent.putExtra("username",username.get(position));
                    startActivityForResult(editItemsIntent, Items.ItemInfo);

                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
            programAdapter = new ItemArrayAdapter(this, area, progImag, desc,isChecked,username);
            itemsListView.setAdapter(programAdapter);





            JSONObject jsonObject = new JSONObject(LoginPage.store);
            String boo = jsonObject.getString("success");
            if (boo.equals("true")) {

                //get the username-boolean hashmap. then when reading the full object add the relevent boolean
              JSONArray switchArray = jsonObject.getJSONArray("predicament");
                for (int i = 0; i < switchArray.length(); i++) {
                    JSONObject switchSingle=switchArray.getJSONObject(i);
                    String usernameTemp = switchSingle.getString("username");
                    usernameSwitch.put(usernameTemp,switchSingle.getBoolean("state"));

                    JSONArray jsonGroups = switchSingle.getJSONArray("groups");

                    for (int j = 0; j < jsonGroups.length(); j++) {
                        JSONObject gSingle = jsonGroups.getJSONObject(j);
                        String groupTemp = gSingle.getString("name");
                        if(!groups.containsKey(groupTemp)){
                            groups.put(groupTemp,new ArrayList<>());
                            title.add(groupTemp);
                        }
                        groups.get(groupTemp).add(new ComboItem(usernameTemp,gSingle.getString("scenarioOn"),gSingle.getString("scenarioOff")));
                    }


                }

                JSONArray jsonArray = jsonObject.getJSONArray("appliances");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject applian = jsonArray.getJSONObject(i);
                    String usernameString = applian.getString("username");
                    String details= applian.getString("detail");
                    JSONObject jsonSingleAppliance = new JSONObject(details);
                    areaString = jsonSingleAppliance.getString("area");
                    descString = jsonSingleAppliance.getString("desc");
                    addItemToList(areaString, descString,usernameString,usernameSwitch.get(usernameString));


                    /*JSONArray jsonArrayGroups = jsonObject.getJSONArray("groups");
                    for (int j = 0; j < jsonArrayGroups.length(); j++) {
                        JSONObject groupSingle = jsonArray.getJSONObject(i);
                        String groupName = groupSingle.getString("name");
                        if(!groups.containsKey(groupName))
                            groups.put(groupName,new ArrayList<String>());
                        groups.get(groupName).add(usernameString);



                    }
*/



                }
            }



            if(LoginPage.testing) {
                addItemToList("Dan","Light","0xBB23D1",false);
                addItemToList("Guy","Water-Heater","0xD2FF3D",false);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addGroup(String groupName, ArrayList<ComboItem> actions){
        title.add(groupName);
        groups.put(groupName,actions);

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
                addItemToList(areaString, typeString, usernameString, stateSwitch);
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
                Handler handler = new Handler();
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
                                    String boo = jsonObject.getString("removed");
                                    if (boo.equals("true")) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                removeAppliance(positionSaver);
                                            }
                                        });
                                    } else {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Could not delete item", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                };
                Thread itemsActThread = new Thread(checkIfResponse);
                itemsActThread.start();


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
            if(LoginPage.echo)
                LoginPage.ws.send(jsonLogin.toString());
            else
                LoginPage.store=jsonLogin.toString();

            Intent addScreenIntent = new Intent(Items.this, com.example.realproject.Items.AddScreen.class);

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
            ArrayList<ComboItem> usernameExample = new ArrayList<>();
            usernameExample.add(new ComboItem("0xBB23D1", "{messageType:flipTheSwitch, sendToUsername:\"" + "0xBB23D1" + "\",  msg:true}","{messageType:flipTheSwitch, sendToUsername:\"" + "0xBB23D1" + "\",  msg:false}"));
            usernameExample.add(new ComboItem("0xD2FF3D", "{messageType:flipTheSwitch, sendToUsername:\"" + "0xD2FF3D" + "\",  msg:true}", "{messageType:flipTheSwitch, sendToUsername:\"" + "0xD2FF3D" + "\",  msg:false}"));
            if (!groups.containsKey("dan"))
                addGroup("dan",usernameExample);



            Intent itemsAct = new Intent(this, ComboPage.class);
            startActivity(itemsAct);


        }
        //on click invisible
        else {
            positionSaver = view.getId();
        }
    }


    public static String getAreaFromUsername(String usernameString){
        for (int i = 0; i < username.size(); i++) {
            if(username.get(i).equals(usernameString))
                return area.get(i);
        }
        return null;

    }

    public static Integer getPositionFromUsername (String usernameString){
        for (int i = 0; i < username.size(); i++) {
            if(username.get(i).equals(usernameString))
                return i;
        }
        return null;


    }

    public void addItemToList(String areaString, String descString,String usernameString,Boolean stateSwitch) {
        area.add(areaString);
        desc.add(descString);
        username.add(usernameString);
        isChecked.add(stateSwitch);
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
        progImag.remove(positionSaver);

        programAdapter.notifyDataSetChanged();

    }



}