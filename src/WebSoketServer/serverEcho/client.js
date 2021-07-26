const WebSocket = require('ws');

//const serverAddress = "ws://192.168.14.101:5000";
const serverAddress ="ws://cthulhuserver.duckdns.org:5050";
//const serverAddress = 'wss://simple-websocket-server-echo.glitch.me/';

const ws = new WebSocket(serverAddress, {
    headers: {
        "user-agent": "Mozilla"
    }
});

ws.on('open', function() {
    ws.send("Hello from PCamp!");
});

ws.on('message', function(msg) {
    console.log("Received msg from the server: " + msg);
});