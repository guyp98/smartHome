const WebSocket = require('ws');

const serverAddress = "ws://127.0.0.1:5050";
//const serverAddress ="ws://cthulhuserver.duckdns.org:5050";
//const serverAddress = 'wss://simple-websocket-server-echo.glitch.me/';
const prompt = require("prompt-sync")({ sigint: true });







const ws = new WebSocket(serverAddress, {
    headers: {
        "user-agent": "Mozilla"
    }
});

ws.on('open', function() {
    ws.send("connect{\"username\":\"guy\",\"password\":\"porat\"}");
});
ws.addEventListener('send',()=>{
    const x = prompt();
    ws.send(x);
    });
ws.on('message', function(msg) {
    console.log("Received msg from the server: " + msg);
    ws.emit('send');
});