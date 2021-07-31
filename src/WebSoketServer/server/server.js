

const WebSocket=require('ws');
const { addMsgToPrint } = require('./serverLogs');
const Users=require('./userManagement');

const PORT = 5001;
const wsServer = new WebSocket.Server({port: PORT});

wsServer.on('connection', async function echoHandler(socket){
    let user={username:""};
    addMsgToPrint("A client just connected, "+wsServer.clients.size+" clients connected");

    socket.on('message',async function (msg) {
        interpetMsg(msg,user,socket);
    });
    
    socket.on('close', function () {
        if(user.username!=""){Users.disconnect(user.username);}
        addMsgToPrint('Client disconnected, '+wsServer.clients.size+" clients connected");
    });

    socket.on('send message to user',async function (username,JsonMsg) {
        if(username==user.username){
            socket.send(JSON.stringify(JsonMsg));
        }
    });
});

const parseErorr="parseErorr";//{"messageType":"","content":""}when the server cant parse user message

function interpetMsg(msg,user,socket){//take msg string and user- and return astring to send back to the user
    try{
        const inputObj=JSON.parse(msg);
        if(!Users.isConnected(user.username)&&inputObj.messageType!=Users.tryToConnect&&inputObj.messageType!=Users.register){//if user is not connected the only option is to try to connect
            userSocket.emit('send message to user',user.username,{messageType:parseErorr,content: "you need to login first"});/**todo need be jason */
        }
        else if(Users.isConnected(user.username) && !Users.canAccess(user.username,inputObj.messageType)){
            userSocket.emit('send message to user',user.username,{messageType:parseErorr,content: "you dont have permition for this command"});
        }
        else{
            handleMsg(inputObj,user,socket);
        }
    }
    catch(msg){
        addMsgToPrint("cant parse user:("+user.username+")massage. stack trace "+msg);//todo create print manager to the server
        userSocket.emit('send message to user',user.username,{messageType:parseErorr,content:"message interpetion error"}); 
    }
}


function handleMsg(inputObj,user,userSocket){
    switch(inputObj.messageType){
        
        case Users.tryToConnect:
            if(Users.tryConnectUser(inputObj.username,inputObj.password)){
                user.username=inputObj.username;
                addMsgToPrint(user.username+" logedin");
                userSocket.emit('send message to user',user.username,{messageType:"loginResponse",loggedin:true,errorDetails:"Login succesfull" });
            }
            else{
                userSocket.emit('send message to user',user.username,{messageType:"loginResponse",loggedin:false,errorDetails:"wrong username or password or user already connected"});
            }
            break;

        case Users.echo:
            userSocket.emit('send message to user',user.username,{messageType:"echoResponse",message:inputObj.toEcho });
            break;

        case Users.usersComunnication:
            wsServer.clients.forEach(function(socket){
                socket.emit('send message to user',inputObj.sendTo,{messageType:"usersCommunication",from:user.username,message:inputObj.msg });
            }); 
            break;

        case Users.register:
            if(Users.addUser(inputObj.username,inputObj.password,inputObj.type)){
                userSocket.emit('send message to user',user.username,{messageType:"registerResponse",registered:true,errorDetails:"register successful"});
            }
            else{
                userSocket.emit('send message to user',user.username,{messageType:"registerResponse",registered:false,errorDetails:"register faild"});
            }
            break;
        
        case Users.giveUserData:
            //
            //
            //
            //
        default:
            return userSocket.emit('send message to user',user.username,{messageType:parseErorr,content:"no message with this type"}); 

    }
}



addMsgToPrint(" Server is listening on port " + PORT);


