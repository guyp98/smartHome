package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import okhttp3.WebSocket;

public class MainMenu extends AppCompatActivity {

    WebSocket ws;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
    }

    public void onButtonClick(View view){
        if(view.getId() == R.id.buttonItems) {

            //LoadingPage loadingPage = new LoadingPage(MainMenu.this);
            //loadingPage.startLoadingDialog();




            Intent itemsAct = new Intent(MainMenu.this, Items.class);
            startActivity(itemsAct);
        }
        else
        {
            LoginPage.ws.send("appliances");
            Intent itemsAct = new Intent(MainMenu.this, LoginPage.class);
            startActivity(itemsAct);
        }



    }
}