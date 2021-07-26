
const WebSocket=require('ws');
const { addMsgToPrint, printPendingLogs } = require('./serverLogs');
const Users=require('./userManagement');


const PORT = 5001;

const wsServer = new WebSocket.Server({port: PORT});

wsServer.on('connection',function echoHandler(socket){
    let user={username:""};
    //console.log("A client just connected, "+wsServer.clients.size+" clients connected");
    addMsgToPrint("A client just connected, "+wsServer.clients.size+" clients connected");
    printPendingLogs();

    socket.on('message', function (msg) {
        var respond=interpetMsg(msg,user);
        socket.send(respond);
    });

    socket.on('close', function () {
        if(user.username!=""){Users.disconnect(user.username);}
        addMsgToPrint('Client disconnected, '+wsServer.clients.size+" clients connected");
        printPendingLogs();
    })
});


addMsgToPrint(" Server is listening on port " + PORT);
printPendingLogs();

const parseErorr="parse Erorr";//{"messageType":"","content":""}when the server cant parse user message
const tryToConnect="try connect"; //{"messageType":"","username":"","password":""} try to connect user
const echo="echo";//{"messageType":"","toEcho":""} echo user msg

function interpetMsg(msg,user){//take msg string and user- and return astring to send back to the user
    const inputObj=parseMsg(msg);
    try{
        if(!Users.isConnected(user.username)&&inputObj.messageType!="try connect"){//if user is not connected the only option is to try to connect
            return "you need to login first"
        }
        switch(inputObj.messageType){

            case parseErorr:
                addMsgToPrint("cant parse user:("+user.username+")massage. stack trace"+(inputObj.content));//todo create print manager to the server
                printPendingLogs();
                return "cant parse message";

            case tryToConnect:
                if(Users.tryConnectUser(inputObj.username,inputObj.password)){
                    user.username=inputObj.username;
                    addMsgToPrint(user.username+" logedin");
                    printPendingLogs();
                    return "Login succesfull";
                }
                else{
                    return "wrong username or password or user already connected";
                }

            case echo:
                return inputObj.toEcho;
            
            default:
                return "cant respond to this type of message"; 

        }
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







