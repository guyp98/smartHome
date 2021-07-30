package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import okhttp3.WebSocket;

public class MainMenu extends AppCompatActivity {

    public static int loadedIndex=0;
    Button itemsButton;
    WebSocket ws;
    LoadingPage loadingPage;
    boolean loaded =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        loadingPage = new LoadingPage(MainMenu.this);
        itemsButton = findViewById(R.id.buttonItems);

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
                    loadedIndex++;
                    checkIfLoaded(handler,itemsAct);
                }
            }
        }, 500);


    }
    public void onButtonClick(View view){
        if(view.getId() == itemsButton.getId()) {
            Intent itemsAct = new Intent(MainMenu.this, Items.class);
            LoginPage.store = "";
            LoginPage.ws.send("dan rules");
            loadingPage.startLoadingDialog();
            Handler handler = new Handler();
            //to do find better idea

            checkIfLoaded(handler, itemsAct);
        }
        else
        {
            LoginPage.ws.send("appliances");
            Intent itemsAct = new Intent(MainMenu.this, LoginPage.class);
            startActivity(itemsAct);
        }



    }
}