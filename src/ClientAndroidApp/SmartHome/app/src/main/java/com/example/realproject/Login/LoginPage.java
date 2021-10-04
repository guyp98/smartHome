package com.example.realproject.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.realproject.Items.Items;
import com.example.realproject.MainMenu;
import com.example.realproject.PopUp.LoadingPage;
import com.example.realproject.R;
import com.example.realproject.WebSocketRecieve;
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
    LoadingPage loadingPage;
    public static String store,username;
    public static final Integer ResultRemoved=2,threadSleep=3,threadCycle=3000;
    public static WebSocket ws;
    public static boolean testing=false,echo=true;



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


        loadingPage = new LoadingPage(LoginPage.this);

        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://qasx.duckdns.org:5001").build() ;
        if (testing)
            request = new Request.Builder().url("ws://echo.websocket.org").build();
        else {
            request = new Request.Builder().url("ws://qasx.duckdns.org:5001").build();
        }
        //request = new Request.Builder().url("ws://192.168.14.160:5001").build();
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
                    jsonLoginStr = "{messageType:loginResponse, loggedIn:true, username:" + username.toString() + ", password :" + password + "}";
                else
                    jsonLoginStr = "{messageType:login ,username:" + username + ", password :" + password + "}";


                JSONObject jsonLogin = new JSONObject(jsonLoginStr);
                if(echo)
                    ws.send(jsonLogin.toString());
                else {


                    Intent itemsAct = new Intent(LoginPage.this, Items.class);
                    startActivity(itemsAct);

                }

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


                            //Intent itemsAct = new Intent(LoginPage.this, MainMenu.class);
                           //startActivity(itemsAct);
                        }
                        else {
                            print(jsonObject.getString("errorDetails"));
                        }}
                    else if(type.equals("itemsDataInitialiseResponse")){

                        Intent itemsAct = new Intent(LoginPage.this, Items.class);
                        startActivity(itemsAct);

                    }




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