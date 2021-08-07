#include <ArduinoWebsockets.h>
#include <ESP8266WiFi.h>

const char* ssid = "ONEPLUS8t-guy"; //Enter SSID
const char* password = "guy123123"; //Enter Password
const char* websockets_server = "wss://echo.websocket.org"; //server adress and port

using namespace websockets;

void onMessageCallback(WebsocketsMessage message) {
    Serial.print("Got Message: ");
    Serial.println(message.data());
}

void onEventsCallback(WebsocketsEvent event, String data) {
    if(event == WebsocketsEvent::ConnectionOpened) {
        Serial.println("Connnection Opened");
    } else if(event == WebsocketsEvent::ConnectionClosed) {
        Serial.println("Connnection Closed");
    } else if(event == WebsocketsEvent::GotPing) {
        Serial.println("Got a Ping!");
    } else if(event == WebsocketsEvent::GotPong) {
        Serial.println("Got a Pong!");
    }
}

WebsocketsClient client;
void setup() {
    Serial.begin(115200);
    WiFi.begin(ssid, password);
    while(WiFi.status() != WL_CONNECTED){
        Serial.print(".");
        delay(1000);
    }
    client.onMessage(onMessageCallback);
    client.onEvent(onEventsCallback);
    client.connect(websockets_server);
    client.send("Hi Server!");
    client.ping();
}


void loop() {
    client.poll();
    int incomingByte;
    String toString="";
    while (Serial.available() > 0) {
    incomingByte = Serial.read();
    Serial.print("I received: ");
    Serial.println(incomingByte,DEC);
    String tempNewStr=String(((char)incomingByte));
    toString.concat(tempNewStr);
    
  }
  client.send(toString);
}
