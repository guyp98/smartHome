package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocketListener;

public class RegisterScreen extends AppCompatActivity {

    private Button register;
    private TextView errorPassword;
    private TextInputLayout username, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        register = findViewById(R.id.button_done_register);
        username = findViewById(R.id.usernameRegister);
        password = findViewById(R.id.passwordRegister);
        password2 = findViewById(R.id.passwordRegister2);
        errorPassword = findViewById(R.id.textview_password_error);


    }

    public void checkIfLoaded(Handler handler) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!LoginPage.store.isEmpty()) {
                    finish();
                } else {
                    checkIfLoaded(handler);
                }
                //else
                //  errorLoading.setText("Could not load, please check internet connection");
            }
        }, 30);
    }


    public void onClickLogin(View view) throws JSONException {

        if (view.getId() == register.getId()) ;
        {


            String usernameString = username.getEditText().getText().toString();
            String passwordString = password.getEditText().getText().toString();
            String passwordString2 = password2.getEditText().getText().toString();
            if (passwordString.equals(passwordString2)) {
                String jsonLoginStr;
                if(LoginPage.testing)
                    jsonLoginStr = "{messageType:registerResponse, registered:true, username:" + usernameString + ", password :" + passwordString + "}";
                else
                    jsonLoginStr = "{messageType:register, username:" + usernameString + ", password :" + passwordString + ",type: user }";

                JSONObject jsonLogin = new JSONObject(jsonLoginStr);
                LoginPage.store = "";
                LoginPage.ws.send(jsonLogin.toString());

                Handler handler = new Handler();
                //to do find better idea


                checkIfLoaded(handler);
            } else {

                errorPassword.setText("passwords do not match");
            }
        }


    }


    public void checkMessage(JSONObject jsonObject) {
        try {
            String type = jsonObject.getString("messageType");
            if (type.equals("registerResponse")) {
                if (jsonObject.getString("registered").equals("true")) {
                    finish();
                } else {
                    System.out.println(jsonObject.getString("errorDetails"));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}