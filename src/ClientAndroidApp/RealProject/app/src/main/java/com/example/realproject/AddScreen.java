package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class AddScreen extends AppCompatActivity {
    private TextInputLayout til;

    private Spinner dropDowntype,dropDownUsername;
    private String [] usernameArray;
    private int loadedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screen);
        til = findViewById(R.id.textInputArea);
        dropDowntype = findViewById(R.id.spinner_type);

        //dropDownUsername = findViewById(R.id.spinner_username);
        ArrayAdapter arType = new ArrayAdapter(this, R.layout.layout_dropdown_add, getResources().getStringArray(R.array.TypeOfAppliances));
        //ArrayAdapter arUsername = new ArrayAdapter(this, R.layout.layout_dropdown_add, usernameArray);
        arType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDowntype.setAdapter(arType);


    }

    public void sendAddAppliance(String area, String desc) {
        try {

            String jsonLoginStr = "{area:" + area + ", desc:" + desc + "}";
            JSONObject jsonLogin = new JSONObject(jsonLoginStr);
            String jsonAddAppliaceStr;
            int iddan = LoginPage.runningId;
            if(LoginPage.testing)
                jsonAddAppliaceStr = "{messageType:addApplianceResponse, added:true, itemId:"+iddan+" }";
            else
                    jsonAddAppliaceStr = "{messageType:addAppliance, details:" + jsonLogin.toString() + "}";

            JSONObject jsonAddAppliance = new JSONObject(jsonAddAppliaceStr);
            LoginPage.ws.send(jsonAddAppliance.toString());
        } catch (JSONException e) {
            System.out.println(e.toString());
        }


    }

    public void onButtonClick(View view) {
        if (view.getId() == R.id.buttonAddFinal) {
            String typeString = dropDowntype.getSelectedItem().toString();
            String areaString = til.getEditText().getText().toString();
            Toast.makeText(AddScreen.this, "you selected " + areaString, Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("type", typeString);
            resultIntent.putExtra("area", areaString);
            LoginPage.store = "";
            sendAddAppliance(areaString, typeString);


            Handler handler = new Handler();
            checkIfLoaded(handler, resultIntent);







        }


    }


    public void checkIfLoaded(Handler handler, Intent resultIntent) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!LoginPage.store.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(LoginPage.store);
                        String boo = jsonObject.getString("added");
                        if (boo.equals("true")) {
                            setResult(Activity.RESULT_OK, resultIntent);
                            int id = jsonObject.getInt("itemId");
                            resultIntent.putExtra("id", id);
                            finish();

                        } else
                            setResult(Activity.RESULT_CANCELED, resultIntent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    if (loadedIndex < 300) {
                        loadedIndex++;
                        checkIfLoaded(handler, resultIntent);
                    }
                    else
                    {
                        setResult(Activity.RESULT_CANCELED, resultIntent);
                        finish();

                    }
                    //else
                    //errorLoading.setText("Could not load, please check internet connection");
                }
            }
        }, 30); }
}