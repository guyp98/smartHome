

const WebSocket=require('ws');
const { addMsgToPrint } = require('./serverLogs');
const Users=require('./userManagement');
const result = require("./result");

const PORT = 5001;
const wsServer = new WebSocket.Server({port: PORT});

wsServer.on('connection', async function echoHandler(socket){
    let user={username:""};
    addMsgToPrint("A client just connected, "+wsServer.clients.size+" clients connected");

    socket.on('message',async function (msg) {
        //addMsgToPrint(msg);
        interpetMsg(msg,user,socket);
    });
    
    socket.on('close', function () {
        if(user.username!=""){Users.disconnect(user.username);}
        addMsgToPrint('Client disconnected, '+wsServer.clients.size+" clients connected");
    });

    socket.on('send message to user',async function (username,JsonMsg) {
        //addMsgToPrint(JSON.stringify(JsonMsg));
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
            socket.emit('send message to user',user.username,{messageType:parseErorr,content: "you need to login first"});/**todo need be jason */
        }
        else if(!Users.isFunExist(inputObj.messageType)){
            socket.emit('send message to user',user.username,{messageType:parseErorr,content: "no message with this type"});
        }
        else if(Users.isConnected(user.username) && !Users.canAccess(user.username,inputObj.messageType)){
            socket.emit('send message to user',user.username,{messageType:parseErorr,content: "you dont have permition for this command"});
        }
        else{
            handleMsg(inputObj,user,socket);
        }
    }
    catch(msg){
        addMsgToPrint("cant parse user:("+user.username+")massage. stack trace "+msg);//todo create print manager to the server
        socket.emit('send message to user',user.username,{messageType:parseErorr,content:"message interpetion error"}); 
    }
}


function handleMsg(inputObj,user,userSocket){
    switch(inputObj.messageType){
        
        case Users.tryToConnect:
            var connected=Users.tryConnectUser(inputObj.username,inputObj.password);
            if(result.isOk(connected)){
                user.username=inputObj.username;
                addMsgToPrint(user.username+" logedin");
            }
            userSocket.emit('send message to user',user.username,{messageType:"loginResponse",loggedIn:result.isOk(connected),errorDetails:connected.msg });
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
            var res=Users.addUser(inputObj.username,inputObj.password,inputObj.type);
            userSocket.emit('send message to user',user.username,{messageType:"registerResponse",registered:result.isOk(res),errorDetails:res.msg});
            break;
        
        case Users.giveUserData:
            userSocket.emit('send message to user',user.username,{messageType:"itemsDataInitialiseResponse",appliances:Users.getUserData(user.username) });
            break;
        case Users.addAppliance:
            var res=Users.addApplianceToUser(user.username,inputObj.details);
            userSocket.emit('send message to user',user.username,{messageType:"addApplianceResponse",added:result.isOk(res),itemId:(res.msg)[0],errorDetails:(res.msg)[1]});
            break;
        case Users.removeAppliance:
            var res=Users.removeApplianceToUser(user.username,inputObj.id);
            userSocket.emit('send message to user',user.username,{messageType:"removeApplianceResponse",removed:result.isOk(res),errorDetails:res.msg});
            break;
            
        default:
            return userSocket.emit('send message to user',user.username,{messageType:parseErorr,content:"no message with this type"}); 

    }
}



addMsgToPrint(" Server is listening on port " + PORT);


