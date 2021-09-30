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
 The application is written in Android studio and is direct for android users. It is written in Java and contains 10 different Activities.

The application uses a web socket that is created when the user enters the application. The user logs in and the web socket sends the data to the server and waits for a reply. 

#### Features:

Each of the features written changes in the app and notifies/get approval from the server.
- Login and Registration windows.
- Option to add new Items, the system automatically reviews appliances that are connected to the server but not to the specific account and displays them. 
- Ability to change name, and items description and area
- Ability to delete an item.

``` make more prevelent? ```

Combos - We wanted to do something new and we thought of combos, the ability to activate many different appliances with one click. For example while watching TV you enjoy turning off all the light in the living room and turning on the kitchen light. This is possible by creating a combo.
- Create a combo and define which appliance are a part of it and what the appliances will do when the combo is activated. This was especially difficult because there is a lot of information to display and to input. Keeping it all in one page and organized and readable was tough.
- Each Item can be a part of as many combos as you’d like, and each combo can have between 1 - n items (n being the amount of appliances). 
- Combos can be edited to change an appliance activity or to change which appliances are part of the combo.

#### Technical details:

- The communication is done with Json objects with a pre defined structure.

- Activities that need to load data wait for the reply form the server, where as activities that should be immediate (turning on/off a light switch) act immediately and if there is an issue notify when the response from the server returns.  loading data the 

- In the items page each we have a list view with a custom array adapter which displays the photo of the product, a description, area and the switch to turn on and off. If clicked on the item (onClickListener) it will open the ItemInfo page which displays more info about the appliance. A major problem was how to have two onClickListener objects, one for the whole product to transition into the itemInfo page and the other to turn on the appliance. This was solved by writing in the XML file of the listview (for a single item) “android:descendantFocusability="blocksDescendants”. Which makes both the appliance clickable and the switch. This was also used in the comboPage.

- To make the code more efficient I used a holder for the array adapters. This holder held all the object (switches, images, textviews…) and would make runtime quicker by not having to use “findViewById”. 

### Server

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
















