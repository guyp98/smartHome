const WebSocket = require('ws');

const serverAddress = "ws://127.0.0.1:5001";
//const serverAddress ="ws://cthulhuserver.duckdns.org:5050";
//const serverAddress = 'wss://simple-websocket-server-echo.glitch.me/';
const prompt = require("prompt-sync")({ sigint: true });



const ws = new WebSocket(serverAddress, {
    headers: {
        "user-agent": "Mozilla"
    }
});

ws.on('open', function() {
    ws.send("{\"messageType\":\"login\",\"username\":\"guy\",\"password\":\"porat\"}");
   /* ws.send("{\"messageType\":\"register\",\"username\":\"Dan\",\"password\":\"rotman\",\"type\":\"user\"}");
    ws.send("{\"messageType\":\"login\",\"username\":\"Dan\",\"password\":\"ddd\"}");
    ws.send("{\"messageType\":\"login\",\"username\":\"Dan\",\"password\":\"rotman\"}");
    ws.send("{\"messageType\":\"itemsDataInitialise\",\"username\":\"Dan\"}");
    ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"}}");
    ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"}}");
    ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"}}");
    ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"}}");
    ws.send("{\"messageType\":\"userCommand\",\"sendTo\":\"1234\",\"msg\":\""+  "on"  +"\"}");
    ws.send("{\"messageType\":\"getAllAppliances\"}");
    ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "true"  +"}");*/
    
});
ws.addEventListener('send',async function(){
    const x =prompt();
    //ws.send("{\"messageType\":\"echo\",\"toEcho\":\""+x+"\"}");
    ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":\""+  x  +"\"}");
    //ws.send("{\"messageType\":\"userCommand\",\"sendTo\":\"guy2\",\"msg\":\""+x+"\"}");//{"messageType":"","sendTo":"","msg":""} 
    });
ws.on('message',async function(msg) {
    console.log("Received from the server: " + msg);
    ws.emit('send');
    //ws.emit('send');
});
ws.on('close',function(){console.log("server closed" );})


//{"messageType":"register","username":"Dan","password":"rotman","type":"user"}
//{"messageType":"login","username":"Dan","password":"ddd"}
//{"messageType":"login","username":"Dan","password":"rotman"}
//{"messageType":"itemsDataInitialise","username":"Dan"}
//{"messageType":"addAppliance","details":{"area":"Guy","desc":"Light"}}
//
