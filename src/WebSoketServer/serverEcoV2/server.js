


import WebSocket from "ws";
import {  disconnect, isConnected, parseUserAndPassword, tryConnectUser } from "./security.js";

const PORT = 5050;
const tryToConnectKeyWord="connect";

const wsServer = new WebSocket.Server({port: PORT});

wsServer.on('connection',function echoHandler(socket){
    let user="";
    console.log("A client just connected, "+wsServer.clients.size+" clients connected");
    if(user==""){
        socket.send("enter user name and password to login (format \"connect{\"username\":\"youruser\",\"password\":\"yourPassword\"}\" )" );
    }

    socket.on('message', function (msg) {
        if(msg.startsWith(tryToConnectKeyWord)){
            const obj=parseUserAndPassword(msg.slice(tryToConnectKeyWord.length));
            if(tryConnectUser(obj.username,obj.password)){user=obj.username;}
            else{socket.send("wrong username or password or user already connected");}
        }
        else if(user!=""&&isConnected(user)){
            console.log("Received message from "+user+": "  + msg);
            socket.send("Take this back: " + msg);
        }
        else{
            socket.send("no user connected need to send \"connect{\"username\":\"youruser\",\"password\":\"yourPassword\"}\"");
        }
    });

    socket.on('close', function () {
        if(user!=""){disconnect(user);}
        console.log('Client disconnected, '+wsServer.clients.size+" clients connected");
    })
});

console.log( (new Date()) + " Server is listening on port " + PORT);




