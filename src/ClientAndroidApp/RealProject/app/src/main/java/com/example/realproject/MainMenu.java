package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.realproject.Combos.ComboPage;
import com.example.realproject.Items.Items;
import com.example.realproject.Login.LoginPage;
import com.example.realproject.PopUp.LoadingPage;

import org.json.JSONException;
import org.json.JSONObject;

public class MainMenu extends AppCompatActivity {

    public static int loadedIndex=0;
    Button itemsButton,buttonCombos;
    TextView errorLoading;
    Runnable checkIfResponse;
    private Boolean started=false;
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


    public void onButtonClick(View view) throws JSONException {
        if (view.getId() == itemsButton.getId()) {
            Intent itemsAct = new Intent(MainMenu.this, Items.class);
            LoginPage.store = "";
            String jsonLoginStr;
            if (LoginPage.testing)
                jsonLoginStr = "{messageType:itemsDataInitialiseResponse ,appliances: [], predicament:[], success:true }";
            else
                jsonLoginStr = "{messageType:itemsDataInitialise ,username:" + LoginPage.username + "}";
            JSONObject jsonLogin = new JSONObject(jsonLoginStr);

            if(LoginPage.echo)
                LoginPage.ws.send(jsonLogin.toString());
            else
                LoginPage.store=jsonLogin.toString();

            loadingPage.startLoadingDialog();
            //to do find better idea



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
                                loadingPage.dismissDialog();
                                startActivity(itemsAct);

                            }
                        }
                        if (!started)
                            errorLoading.setText("Could not load, please check internet connection");
                    }
            };
            Thread itemsActThread = new Thread(checkIfResponse);
            itemsActThread.start();

        }
        if (view.getId() == buttonCombos.getId()) {
            started=false;


            Intent itemsAct = new Intent(MainMenu.this, ComboPage.class);
            startActivity(itemsAct);
            LoginPage.store = "";
            String jsonLoginStr;
            if (LoginPage.testing)
                jsonLoginStr = "{messageType:combosResponse ,appliances: [], predicament:[], success:true }";
            else
                jsonLoginStr = "{messageType:combos ,username:" + LoginPage.username + "}";
            JSONObject jsonLogin = new JSONObject(jsonLoginStr);
            LoginPage.ws.send(jsonLogin.toString());




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