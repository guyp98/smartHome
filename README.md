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
 The application is written in Android Studio and is targeted for android users. It is written in Java and contains 10 different Activities. The communication with the server is done through websockets that send string of predefined JSON objects.

#### Features:

- Login and Registration windows.
- Option to add new Items, the system automatically reviews appliances that are connected to the server but not to the specific account and displays them. 
- Ability to change an items name, description and area.
- Ability to delete an item.

Combos - We wanted to do something new and we thought of combos, the ability to activate multiple appliances with one click. For example while watching TV you would like to turn off all the lights in the living room and turn on the kitchen light in one click. This is possible by creating a combo.
- Create a combo and define which appliances are a part of it and what the appliances will do when the combo is activated. This was very intricate because there is a lot of information to display and for the user to input. Therefor keeping it all in one page, organized and readable was challenging.
- Each Item can be a part of as many combos as you’d like, and each combo can have between 1 - n items (n being the amount of appliances). 
- Combos can be edited to change an appliance activity or to change which appliances are a part of the combo.


### Server
The server's role is to control all the smart appliances in the house and to save all of their data, such as the appliances name, area, state... The user can access this data through the app.                          
The server is a WebSockets server written in Node.js. We chose Node.js because of its support of asynchronous execution, which we use frequently. This makes the server very robust and with extremely low latency. The server has no GUI for the users, and is only meant to work with the designated android app. The app and the server communicate via strings of JSON objects. 

### Smart Switch
The main component of the Smart Switch is a relay that is controlled by a microcontroller.                                                                                          
- Microcontroller - We used an ESP8266 chip, with the ESP-01s module. We chose the ESP because of its native integration with wi-fi and the Arduino ide, also it has a low price. 
-  The switch only saves its own unique id. The rest of the switch's information is saved by the server. The switch acts as "slave" to the server - gets commands and return responses.
- Power Supply - The smart switch operates with 5v, but can operate with 220v do to its switched-mode power supply.


### Our end result

![image](https://github.com/guyp98/smartHome/blob/main/readingMaterial/IOT/smartSwitch1.jpg)
![image](https://github.com/guyp98/smartHome/blob/main/readingMaterial/IOT/smartSwitch2.jpg)

Circuit schematics- https://github.com/guyp98/smartHome/blob/main/readingMaterial/IOT/smartSwitchCircuit.png

### Router
In this project, the app communicates with the server remotely as well as on the same network.
To achieve this remote communication:
- We port-forwarded the servers port.
- We used a DDNS (duckDNS) which the server updates every 5 minutes with its current IP.
- Then the DDNS routs the servers domain to the new IP.
- Therefor the android app has one static domain that it can connect to from any network in the world.


















