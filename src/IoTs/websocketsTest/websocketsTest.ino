#include <ArduinoWebsockets.h>
#include <ESP8266WiFi.h>
#include <ArduinoJson.h>

const char* ssid = "BEZEQINT-A09A-2.4G"; //Enter SSID
const char* password = "SSgdaA0584442626"; //Enter Password
const char* websockets_server = "ws://192.168.14.101:5001"; //server adress and port
const char* Username="1234";
const char* Password="1234";
const char* Type="smartSwitch";

boolean loggedin=false;
String lastSender="guy";
using namespace websockets;
WebsocketsClient client;

void onMessageCallback(WebsocketsMessage message) {
    Serial.print("Got Message: ");
    Serial.println(message.data());
    inputCommand(message.data());
    
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


void setup() {
    Serial.begin(115200);
    pinMode(LED_BUILTIN, OUTPUT);
    WiFi.begin(ssid, password);
    while(WiFi.status() != WL_CONNECTED){
        Serial.print(".");
        delay(1000);
    }
    client.onMessage(onMessageCallback);
    client.onEvent(onEventsCallback);
    client.connect(websockets_server);
    client.ping();
    client.send(registerToServer(Username,Password,Type));
    client.poll();
    client.send(loginToServer(Username,Password));
    client.poll();
    digitalWrite(LED_BUILTIN, LOW);
    client.send(statusToServer("on"));
}


void loop() {
    client.poll();
    
  //client.send(inputSerialMonitor());
   // client.send(loginToServer("guy","porat"));
   // client.poll();
   // delay(1000000);
    
  
}

String registerToServer(String username,String password,String type){
    DynamicJsonDocument doc(1024);
    doc["messageType"]="register";
    doc["username"] = username;
    doc["password"]   = password;
    doc["type"] = type;
    String stringify="";
    serializeJson(doc, stringify);
    return stringify;
  }
String loginToServer(String username,String password){
  DynamicJsonDocument doc(1024);
  doc["messageType"]="login";
  doc["username"] = username;
  doc["password"]   = password;
  String stringify="";
  serializeJson(doc, stringify);
  return stringify;
}
String statusToServer(String statusT){
  DynamicJsonDocument doc(1024);
  doc["messageType"]="statusResponse";
  doc["status"] = statusT;
  doc["sendTo"]=lastSender;
  String stringify="";
  serializeJson(doc, stringify);
  return stringify;
}
String inputCommand(String json){
  DynamicJsonDocument doc(1024);
  deserializeJson(doc, json);
  if(doc["messageType"]=="usersCommunication"){
    String command= doc["message"];
    //lastSender= doc["from"];
    if(command=="on"){Serial.print("flip switch on");digitalWrite(LED_BUILTIN, LOW);}
    else if("off"){Serial.print("flip switch off");digitalWrite(LED_BUILTIN, HIGH);}
    if(command!=""){
      client.send(statusToServer(command));
      return "fail";
    }
    return "ok"; 
  }
  else if(doc["messageType"]=="loginResponse"){
    boolean logged= doc["loggedIn"];
    if(logged){
      loggedin=true;
      return "ok";}
    }
  return "fail"; 
    
  }
  /*
String inputSerialMonitor(){
  int incomingByte;
    String toString="";
    while (Serial.available() > 0) {
      incomingByte = Serial.read();
      Serial.print("I received: ");
      Serial.println(incomingByte,DEC);
      String tempNewStr=String(((char)incomingByte));
      toString.concat(tempNewStr);
    }
    return toString;
  }*/
