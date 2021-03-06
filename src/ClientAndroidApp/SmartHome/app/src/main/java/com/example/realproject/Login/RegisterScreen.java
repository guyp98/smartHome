package com.example.realproject.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.realproject.Login.LoginPage;
import com.example.realproject.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterScreen extends AppCompatActivity {

    private Button register;
    private TextView errorPassword;
    private TextInputLayout username, password, password2;
    private boolean started = false;
    private Runnable checkIfResponse;

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


    public void onClickLogin(View view) throws JSONException {

        if (view.getId() == register.getId()) ;
        {
            String usernameString = username.getEditText().getText().toString();
            String passwordString = password.getEditText().getText().toString();
            String passwordString2 = password2.getEditText().getText().toString();
            if (passwordString.equals(passwordString2)) {
                Boolean letter=false,digit= false;
                for (int i = 0; i < passwordString.length() ; i++) {
                    if(Character.isLetter(passwordString.charAt(i)))
                        letter=true;
                    if(Character.isDigit(passwordString.charAt(i)))
                        digit=true;
                }
                if(letter & digit) {


                    String jsonLoginStr;
                    if (LoginPage.testing)
                        jsonLoginStr = "{messageType:registerResponse, registered:true, username:" + usernameString + ", password :" + passwordString + "}";
                    else
                        jsonLoginStr = "{messageType:register, username:" + usernameString + ", password :" + passwordString + ",type: user }";

                    JSONObject jsonLogin = new JSONObject(jsonLoginStr);
                    LoginPage.store = "";
                    if (LoginPage.echo)
                        LoginPage.ws.send(jsonLogin.toString());
                    else
                        LoginPage.store = jsonLogin.toString();

                    //to do find better idea


                    checkIfResponse = new Runnable() {
                        @Override
                        public void run() {
                            Log.d("checkIfResponseThread", "my id is " + Thread.currentThread().getName());
                            started = false;
                            for (int i = 0; i < LoginPage.threadCycle & !started; i++) {
                                try {
                                    Thread.sleep(LoginPage.threadSleep);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!LoginPage.store.isEmpty()) {
                                    started = true;
                                    finish();
                                }
                            }
                            //if (!started)
                            //  errorLoading.setText("Could not load, please check internet connection");
                        }
                    };
                    Thread itemsActThread = new Thread(checkIfResponse);
                    itemsActThread.start();
                }
                else{
                    errorPassword.setText("password must contain a letter and digit");

                }
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