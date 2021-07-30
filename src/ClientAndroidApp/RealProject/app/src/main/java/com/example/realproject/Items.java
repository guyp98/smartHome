package com.example.realproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private ArrayList<Integer> progImag;
    private String areaString,descString,state;
    private ListView items;
    private int positionSaver;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgramAdapter programAdapter;
    int dataSize;
    int index=0;



    //Will save the size variable, each item key will be its index and type/area. eg: 1type:Light , 1area:Kitchen , therefore just loop until size and extract all keys.
    public static final String myData = "myData";
    public static final String itemSize = "Size";


    public void getItems ( ){
        LoginPage.ws.send("give items please");


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);




        area = new ArrayList<>();
        desc = new ArrayList<>();
        progImag = new ArrayList<>();


        JSONObject jsonObject = new JSONObject(LoginPage.store);
        JSONArray jsonArray = jsonObject.getJSONArray("appliances");
        for (int i = 0; i <jsonArray.length() ; i++) {
            JSONObject applian = jsonArray.getJSONObject(i);
            areaString= applian.getString("applianceName");
            descString = applian.getString("applianceDescription");
            state = applian.getString("state");

            addItemToList(areaString,descString);
        }



        items = findViewById(R.id.listViewItems);
        programAdapter = new ProgramAdapter(this, area, progImag, desc);
        items.setAdapter(programAdapter);


        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSaver = position;
                Toast.makeText(Items.this,"you clicked"+ position,Toast.LENGTH_SHORT).show();
            }
        });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //sharedPreferences = getSharedPreferences(myData,MODE_PRIVATE);
        //editor=sharedPreferences.edit();

        /*dataSize= sharedPreferences.getInt(itemSize,0);
        for (int i = 1 ; i <= dataSize ; i++) {
            String typeString = sharedPreferences.getString(""+i+"type","");
            String areaString = sharedPreferences.getString(""+i+"area","");
            addItemToList(areaString,typeString);
        }*/




    }
        /*area.add("Bathroom");
        area.add("Bathroom");
        area.add("Master Bedroom");
        area.add("Kitchen 1");
        area.add("Kitchen 2");
        area.add("Living Room 1");
        area.add("Living Room 2");

        desc.add("Light");
        desc.add("Water Heater");
        desc.add("Light");
        desc.add("Light");
        desc.add("Light");
        desc.add("Light");
        desc.add("Light");

         progImag.add(R.drawable.lightbuldyellow);
        progImag.add(R.drawable.boiling_water);
        progImag.add(R.drawable.lightbuldyellow);
        progImag.add(R.drawable.lightbuldyellow);
        progImag.add(R.drawable.lightbuldyellow);
        progImag.add(R.drawable.lightbuldyellow);
        progImag.add(R.drawable.lightbuldyellow);
*/









    public void onButtonClick(View view) throws JSONException {
        if (view.getId() == R.id.buttonAdd) {



                Intent itemsAct = new Intent(Items.this, AddScreen.class);
                startActivityForResult(itemsAct,1);

        }
        if (view.getId() == R.id.buttonRemove && positionSaver!=-1) {
            area.remove(positionSaver);
            progImag.remove(positionSaver);
            desc.remove(positionSaver);
            dataSize--;
            editor.putInt(itemSize,dataSize);
            for (int i = positionSaver; i <dataSize ; i++) {
                String areaString = area.get(positionSaver);
                String typeString = desc.get(positionSaver);
                editor.putString(""+positionSaver+"area",areaString);
                editor.putString(""+positionSaver+"type",typeString);

            }
            editor.apply();
            positionSaver=-1;
            programAdapter.notifyDataSetChanged();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String areaString = data.getStringExtra("area");
                String typeString = data.getStringExtra("type");
                addItemToList(areaString,typeString);
                programAdapter.notifyDataSetChanged();
                dataSize++;
                //update program item size
                editor.putInt(itemSize,dataSize);
                //save new values
                editor.putString(""+dataSize+"type",typeString);
                editor.putString(""+dataSize+"area",areaString);
                editor.apply();

            }
        }
    }


    public void addItemToList(String areaString,String descString){
        area.add(areaString);
        desc.add(descString);
        if(descString.equals("Light"))
            progImag.add(R.drawable.lightbuldyellow);
        else if(descString.equals("Water Heater"))
            progImag.add(R.drawable.boiling_water);


    }
}