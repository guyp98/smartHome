# Smart Home

This project is a full package to implement a smart home. Starting from the android app all the way to a physical smart switch. This includes code for an android app, a home server and smart Switch. We also included the schematics for the smart switch.

## How it all comes together
![image](https://user-images.githubusercontent.com/63144072/134199323-d933e467-3390-48bd-a524-684467573887.png)


The android app comunicate with the server and the server with the smart switch.
As seen in the picture every home has one server that control the switches in the house.

https://user-images.githubusercontent.com/63144072/134307949-1ada24e5-78f1-4906-a318-38dc257076d3.mp4




## Flow Chart success scenario
![image](https://github.com/guyp98/smartHome/blob/main/readingMaterial/flow_chart.png)


## The breakdown of each component

### Android app
 The application is written in Android studio and is targeted for android users. It is written in Java and contains 10 different Activities. The communication with the server is done through websockets that send predefined json objects.

#### Features:

- Login and Registration windows.
- Option to add new Items, the system automatically reviews appliances that are connected to the server but not to the specific account and displays them. 
- Ability to change name and items description and area.
- Ability to delete an item.

Combos - We wanted to do something new and we thought of combos, the ability to activate multiple appliances with one click. For example while watching TV you enjoy turning off all the light in the living room and turning on the kitchen light. This is possible by creating a combo.
- Create a combo and define which appliance are a part of it and what the appliances will do when the combo is activated. This was very intricate because there is a lot of information to display and to input therefor keeping it all in one page and organized and readable was challenging.
- Each Item can be a part of as many combos as you’d like, and each combo can have between 1 - n items (n being the amount of appliances). 
- Combos can be edited to change an appliance activity or to change which appliances are part of the combo.


### Server
The server's role is to control all the smart appliances in the house and save all data concerning them, this data can be shared with the user via the app.                          
The server is a WebSockets server written in node.js .we chose node.js thanks for its support asynchronous execution, so the server is robust and has very low latency. The server has no GUI for users. It's only meant to work with the designated app. For simplicity, the app and the server communicate via strings of JSON files. 

### Smart Switch
In its essence, the smart switch is a relay controlled by a microcontroller.                                                                                           
- The microcontroller - We used an ESP8266  chip in the ESP-01s module.
We chose the ESP because of its native integration with the Arduino ide, low price and its wifi capabilities. 
-  the switch only saves its own id. All the rest is saved by the server. The switch acts as "slave" to the server (gets commands and return responses) 

Our end result

![image](https://github.com/guyp98/smartHome/blob/main/readingMaterial/IOT/smartSwitch1.jpg)
![image](https://github.com/guyp98/smartHome/blob/main/readingMaterial/IOT/smartSwitch2.jpg)

Circuit schematics- https://github.com/guyp98/smartHome/blob/main/readingMaterial/IOT/smartSwitchCircuit.png

### Router
In this project, the app communicates with the server on the same network as well as remotely. To achieve this remote communication we configured the router to port forward the server port. This will work well except for routers with dynamic IP since the IP changes regularly. We, therefore, used DDNS (duckDNS) - the server sends its IP to the DDNS. The DDNS updates the router IP regularly and the app connects to the appropriate domain in the DDNS. 
















