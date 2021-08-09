package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocket;

public class MainMenu extends AppCompatActivity {

    public static int loadedIndex=0;
    Button itemsButton,buttonCombos;
    TextView errorLoading;

    LoadingPage loadingPage;
    boolean loaded =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        loadingPage = new LoadingPage(MainMenu.this);
        itemsButton = findViewById(R.id.buttonItems);
        buttonCombos= findViewById(R.id.buttonCombos);
        // errorLoading = findViewById(R.id.error_loading_items);

    }

    public void checkIfLoaded (Handler handler, Intent itemsAct){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!LoginPage.store.isEmpty()) {
                    loadingPage.dismissDialog();
                    startActivity(itemsAct);

                }

                else{
                    if(loadedIndex <300) {
                        loadedIndex++;
                        checkIfLoaded(handler, itemsAct);
                    }
                    else
                        errorLoading.setText("Could not load, please check internet connection");
                }
            }
        }, 30);


    }
    public void onButtonClick(View view) throws JSONException {
        if (view.getId() == itemsButton.getId()) {
            Intent itemsAct = new Intent(MainMenu.this, Items.class);
            LoginPage.store = "";
            String jsonLoginStr;
            if (LoginPage.testing)
                jsonLoginStr = "{messageType:itemsDataInitialiseResponse ,appliances: [], success:true }";
            else
                jsonLoginStr = "{messageType:itemsDataInitialise ,username:" + LoginPage.username + "}";
            JSONObject jsonLogin = new JSONObject(jsonLoginStr);
            LoginPage.ws.send(jsonLogin.toString());
            loadingPage.startLoadingDialog();
            Handler handler = new Handler();
            //to do find better idea

            checkIfLoaded(handler, itemsAct);
        } else if (view.getId() == buttonCombos.getId()) {

            LoginPage.ws.send("combos");
            Intent itemsAct = new Intent(MainMenu.this, LoginPage.class);
            startActivity(itemsAct);
        }
    }
    public void onSocketUpdate(JSONObject jsonObject) {

        try {
            String type = jsonObject.getString("messageType");
            if (type.equals("itemsResponse")) {
                if (jsonObject.getString("success").equals("true")) {
                    loadingPage.dismissDialog();

                    Intent itemsAct = new Intent(MainMenu.this, Items.class);
                    startActivity(itemsAct);
                } else {
                    //System.out.println(jsonObject.getString("errorDetails"));
                }
            }
        }
        catch (Exception e){
            System.out.println(e.toString());;
        }
    }
}