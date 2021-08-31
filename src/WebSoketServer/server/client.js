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
   // ws.send("{\"messageType\":\"register\",\"username\":\"Dan\",\"password\":\"rotman\",\"type\":\"user\"}");
    //ws.send("{\"messageType\":\"login\",\"username\":\"Dan\",\"password\":\"ddd\"}");
    //ws.send("{\"messageType\":\"login\",\"username\":\"Dan\",\"password\":\"rotman\"}");
    ws.send("{\"messageType\":\"itemsDataInitialise\",\"username\":\"guy\"}");
    ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"},\"username\":\"1234\"}");
    ws.send("{\"messageType\":\"removeAppliance\",\"username\":\"1234\"}");
    //ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"},\"username\":\"1234\"}");
   // ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"},\"username\":\"1234\"}");
  //  ws.send("{\"messageType\":\"addAppliance\",\"details\":{\"area\":\"Guy\",\"desc\":\"Light\"},\"username\":\"1234\"}");
    //ws.send("{\"messageType\":\"userCommand\",\"sendTo\":\"1234\",\"msg\":\""+  "on"  +"\"}");
    ws.send("{\"messageType\":\"getAllAppliances\"}");
    ws.send("{\"messageType\":\"itemsDataInitialise\",\"username\":\"guy\"}");
   // setTimeout(()=>ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "true"  +"}"),2000);
   // setTimeout(()=>ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "false"  +"}"),2000);
    //setTimeout(()=>ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "true"  +"}"),2000);
    //setTimeout(()=>ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "false"  +"}"),2000);
    //setTimeout(()=>ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "true"  +"}"),2000);
    

    ws.send("{\"messageType\":\"group\",\"groupName\":\"new1\",\"names\":[{\"username\":\"1234\",\"onScenario\":{\"messageType\":\"getAllAppliances\"},\"offScenario\":{\"messageType\":\"getAllAppliances\"}}],\"action\":\"newGroup\"}");
    ws.send("{\"messageType\":\"group\",\"groupName\":\"new1\",\"names\":[{\"username\":\"1234\",\"onScenario\":{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "true"  +"},\"offScenario\":{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":"+  "false"  +"}}],\"action\":\"EditGroup\"}");
    ws.send("{\"messageType\":\"group\",\"groupName\":\"new1\",\"names\":[{\"username\":\"1234\",\"onScenario\":{\"messageType\":\"getAllAppliances\"},\"offScenario\":{\"messageType\":\"getAllAppliances\"}}],\"action\":\"groupScenarioOff\"}");
    ws.send("{\"messageType\":\"group\",\"groupName\":\"new1\",\"names\":[{\"username\":\"1234\",\"onScenario\":{\"messageType\":\"getAllAppliances\"},\"offScenario\":{\"messageType\":\"getAllAppliances\"}}],\"action\":\"groupScenarioOn\"}");
    ws.send("{\"messageType\":\"group\",\"groupName\":\"new1\",\"names\":[{\"username\":\"1234\",\"onScenario\":{\"messageType\":\"getAllAppliances\"},\"offScenario\":{\"messageType\":\"getAllAppliances\"}}],\"action\":\"groupScenarioOff\"}");
    ws.send("{\"messageType\":\"group\",\"groupName\":\"new1\",\"names\":[{\"username\":\"1234\",\"onScenario\":{\"messageType\":\"getAllAppliances\"},\"offScenario\":{\"messageType\":\"getAllAppliances\"}}],\"action\":\"groupScenarioOn\"}");
    ws.send("{\"messageType\":\"group\",\"groupName\":\"new4\",\"names\":[{\"username\":\"1234\",\"onScenario\":{\"messageType\":\"getAllAppliances\"},\"offScenario\":{\"messageType\":\"getAllAppliances\"}}],\"action\":\"newGroup\"}");
});
ws.addEventListener('send',async function(){
    //const x =prompt();
    //ws.send("{\"messageType\":\"echo\",\"toEcho\":\""+x+"\"}");
    ws.send("{\"messageType\":\"flipTheSwitch\",\"sendToUsername\":\"1234\",\"msg\":\""+  x  +"\"}");
    //ws.send("{\"messageType\":\"userCommand\",\"sendTo\":\"guy2\",\"msg\":\""+x+"\"}");//{"messageType":"","sendTo":"","msg":""} 
    });
ws.on('message',async function(msg) {
    console.log("Received from the server: " + msg);
    //ws.emit('send');
    //ws.emit('send');
});
ws.on('close',function(){console.log("server closed" );})


//{"messageType":"register","username":"Dan","password":"rotman","type":"user"}
//{"messageType":"login","username":"Dan","password":"ddd"}
//{"messageType":"login","username":"Dan","password":"rotman"}
//{"messageType":"itemsDataInitialise","username":"Dan"}
//{"messageType":"addAppliance","details":{"area":"Guy","desc":"Light"}}
//
