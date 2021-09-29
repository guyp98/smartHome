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
 

### Server

### Smart Switch
In its essence, the smart switch is a relay controlled by a microcontroller.                                                                                           
-The microcontroller - We used an ESP8266  chip in the ESP-01s module.
We chose the ESP because of its native integration with the Arduino ide, low price and its wifi capabilities. 

Our end result
![image]()

Circuit schematics- https://github.com/guyp98/smartHome/blob/main/readingMaterial/IOT/smartSwitchCircuit.png

### Router
In this project, the app communicates with the server on the same network as well as remotely. To achieve this remote communication we configured the router to port forward the server port. 
This will work well except for routers with dynamic IP since the IP changes regularly. We, therefore, used DDNS (duckDNS) - the server sends its IP to the DDNS. The DDNS updates the router IP regularly and the app connects to the appropriate domain in the DDNS. 






To use the app outside the home network you need to portforword the server port and use DDNS if you dont have a static ip 









