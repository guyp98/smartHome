package com.example.realproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class LoginPage extends AppCompatActivity implements WebSocketRecieve {
    private TextInputLayout usernameTil,passwordTil;
    private Button login,register;
    private TextView outputFromServer;
    private OkHttpClient client;
    public static final Object switchLock = new Object();
    public static String store,username;
    public static Integer runningId=0;
    public static WebSocket ws;
    public static boolean testing=true;



    private void print (final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputFromServer.setText(outputFromServer.getText().toString() + "\n\n" + txt);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        usernameTil = findViewById(R.id.usernameInput);
        passwordTil = findViewById(R.id.passwordInput);
        login = findViewById(R.id.button_done_register);
        register = findViewById(R.id.button_register);
        outputFromServer = findViewById(R.id.TextViewFromServer);



        client = new OkHttpClient();
        Request request;
        if (testing)
            request = new Request.Builder().url("ws://echo.websocket.org").build();
        else
            request = new Request.Builder().url("ws://cthulhuserver.duckdns.org:5001").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
    }

    public void onClickLogin (View view) throws JSONException {

         if(view.getId()== login.getId())
        {

            username = usernameTil.getEditText().getText().toString();
            String password = passwordTil.getEditText().getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty())
                    usernameTil.setHelperText("Required*");
                if (password.isEmpty())
                    passwordTil.setHelperText("Required*");
            }
            else {

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                String jsonLoginStr;
                if (LoginPage.testing)
                    jsonLoginStr = "{messageType:loginResponse, loggedIn:true, username:" + username + ", password :" + password + "}";
                else
                    jsonLoginStr = "{messageType:login ,username:" + username + ", password :" + password + "}";


                JSONObject jsonLogin = new JSONObject(jsonLoginStr);
                ws.send(jsonLogin.toString());

                //Toast.makeText(LoginPage.this,"username "+ username +" password" + password,Toast.LENGTH_SHORT).show();
            }
        }
        else if (view.getId()== register.getId()){

            Intent itemsAct = new Intent(LoginPage.this, RegisterScreen.class);
            startActivity(itemsAct);

        }


    }

    @Override
    public void onSocketUpdate(JSONObject jsonObject) {
        try {
            String type = jsonObject.getString("messageType");
            if (type.equals("loginResponse")) {
                if (jsonObject.getString("loggedIn").equals("true")) {
                    Intent itemsAct = new Intent(LoginPage.this, MainMenu.class);
                    startActivity(itemsAct);
                } else {
                    print(jsonObject.getString("errorDetails"));
                }
            }
        }
        catch (Exception e){
            System.out.println(e.toString());;
        }
    }


    private final class EchoWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            print("connection made");
        }


        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                store = text;
                String type = "";
                String success = "";

                JSONObject jsonObject = new JSONObject(text);
                type = jsonObject.getString("messageType");

                    if(type.equals( "loginResponse"))
                    {
                        if (jsonObject.getString("loggedIn").equals("true")) {

                            Intent itemsAct = new Intent(LoginPage.this, MainMenu.class);
                            startActivity(itemsAct);
                        }
                        else {
                            print(jsonObject.getString("errorDetails"));
                        }}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000,null);
            print("Closing: " + code + "/" + reason);
        }



        @Override
        public void onFailure(WebSocket webSocket, Throwable t,  Response response) {
            print("Error: " + t.getMessage());
        }
    }
}