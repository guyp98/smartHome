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
    ws.send("{\"messageType\":\"login\",\"username\":\"guy2\",\"password\":\"porat2\"}");
});
ws.addEventListener('send',function(){
    const x ="hi guy";
    ws.send("{\"messageType\":\"echo\",\"toEcho\":\""+x+"\"}");
    //ws.send("{\"messageType\":\"userCommand\",\"sendTo\":\"guy\",\"msg\":\""+x+"\"}");//{"messageType":"","sendTo":"","msg":""} 
    });
ws.on('message', function(msg) {
    console.log("Received msg from the server: " + msg);
    ws.emit('send');
});
ws.on('close',function(){console.log("server closed" );})

//{"messageType":"echo","toEcho":"hi guy"}