package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocket;

public class AddScreen extends AppCompatActivity {
    private TextInputLayout til;
    private String [] types;
    private Spinner dropDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screen);
        til = findViewById(R.id.textInputArea);
        dropDown = findViewById(R.id.spinnerType);

        ArrayAdapter ar  = new ArrayAdapter(this,R.layout.layout_dropdown_add,
                getResources().getStringArray(R.array.TypeOfAppliances));
        ar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(ar);



    }
    public void sendAddAppliance(String area, String desc){
        try {
            String jsonLoginStr = "{messageType:addAppliance, area:" + area + ", desc:" + desc + "}";
            JSONObject jsonLogin = new JSONObject(jsonLoginStr);
            LoginPage.ws.send(jsonLogin.toString());
        }
        catch (JSONException e){
            System.out.println(e.toString());
        }



    }

    public void onButtonClick(View view){
        if(view.getId() == R.id.buttonAddFinal) {
            String typeString = dropDown.getSelectedItem().toString();
            String areaString = til.getEditText().getText().toString();
            Toast.makeText(AddScreen.this,"you selected "+ areaString ,Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("type", typeString);
            resultIntent.putExtra("area", areaString);
            setResult(Activity.RESULT_OK,resultIntent);

            sendAddAppliance(areaString,typeString);
            finish();

            //Intent itemsAct = new Intent(MainActivity.this, Items.class);
            //startActivity(itemsAct);


        }



    }




}