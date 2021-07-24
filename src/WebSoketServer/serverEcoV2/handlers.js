


export function echoToAllHandler(socket) {
    console.log("A client just connected");
    // Attach some behavior to the incoming socket
    socket.on('message', function (msg) {
        console.log("Received message from client: "  + msg);
        // socket.send("Take this back: " + msg);
        // Broadcast that message to all connected clients
        wsServer.clients.forEach(function (client) {
            client.send("Someone said: " + msg);
        });
    });
    
    socket.on('close', function () {
        console.log('Client disconnected');
    })
}
export function echoHandler(socket){
    console.log("A client just connected");
    // Attach some behavior to the incoming socket
    socket.on('message', function (msg) {
        console.log("Received message from client: "  + msg);
        socket.send("Take this back: " + msg);
    });
    socket.on('close', function () {
        console.log('Client disconnected');
    })
}
