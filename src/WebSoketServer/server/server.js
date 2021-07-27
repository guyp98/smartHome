
const WebSocket=require('ws');
const { addMsgToPrint } = require('./serverLogs');
const Users=require('./userManagement');


const PORT = 5001;

const wsServer = new WebSocket.Server({port: PORT});

wsServer.on('connection',function echoHandler(socket){
    let user={username:""};
    addMsgToPrint("A client just connected, "+wsServer.clients.size+" clients connected");

    socket.on('message', function (msg) {
        var respond=interpetMsg(msg,user);
        socket.send(respond);
    });

    socket.on('close', function () {
        if(user.username!=""){Users.disconnect(user.username);}
        addMsgToPrint('Client disconnected, '+wsServer.clients.size+" clients connected");
    })
});


addMsgToPrint(" Server is listening on port " + PORT);

const parseErorr="parse Erorr";//{"messageType":"","content":""}when the server cant parse user message

function interpetMsg(msg,user){//take msg string and user- and return astring to send back to the user
    const inputObj=parseMsg(msg);
    try{
        if(!Users.isConnected(user.username)&&inputObj.messageType!="try connect"){//if user is not connected the only option is to try to connect
            return "you need to login first"
        }
        else if(Users.isConnected(user.username)&&!Users.canAccess(user.username,inputObj.messageType)){
            return "you dont have permition for this"
        }
        handleMsg(inputObj,user);
    }
    catch(msg){
        return "cant respond to this type of message"; 
    }
}
const parseMsg=(msg)=>{
    try{
        return JSON.parse(msg);
    }
    catch(err){
        return {messageType:parseErorr,content:err};
    }
}

function handleMsg(inputObj,user){
    switch(inputObj.messageType){
        case parseErorr:
            addMsgToPrint("cant parse user:("+user.username+")massage. stack trace"+(inputObj.content));//todo create print manager to the server
            return "cant parse message";

        case Users.tryToConnect:
            if(Users.tryConnectUser(inputObj.username,inputObj.password)){
                user.username=inputObj.username;
                addMsgToPrint(user.username+" logedin");
                return "Login succesfull";
            }
            else{
                return "wrong username or password or user already connected";
            }

        case Users.echo:
            return inputObj.toEcho;
        
        default:
            return "cant respond to this type of message"; 

    }
}






