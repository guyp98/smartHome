package com.example.realproject;

import org.json.JSONObject;

public interface WebSocketRecieve {

    public void onSocketUpdate(JSONObject jsonObject); // when socket recieves new message.
}
