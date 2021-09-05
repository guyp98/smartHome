package com.example.tryingservers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    int dan;
    private EditText e1;
    private TextView t1;
    private OkHttpClient client;
    EchoWebSocketListener listener;
    WebSocket ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = findViewById(R.id.EditTextIP);
        t1 = findViewById(R.id.textView);

        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);


    }


    public void onClick (View view){
        if (view.getId() == findViewById(R.id.ButtonSend).getId()){
            ws.send(e1.getText().toString());
        }
        else if (view.getId() == findViewById(R.id.ButtonDiscon).getId()){
            ws.close(1000,"Closing");
        }

    }

    private void start(){

        client.dispatcher().executorService().shutdown();


    }
    private void print (final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t1.setText(t1.getText().toString() +"\n\n"+ txt);
            }
        });

    }


    private final class EchoWebSocketListener extends WebSocketListener{
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
          print("Connection open");


        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            print("Receiving: " +text);

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            print("Receiving: " +bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000,null);
            print("Closing: " + code + " / "+ reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t,  Response response) {
            print("Error: " + t.getMessage());
        }
    }



}