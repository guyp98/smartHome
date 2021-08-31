

const WebSocket=require('ws');
const { addMsgToPrint } = require('./serverLogs');
const Users=require('./userManagement');
const result = require("./result");
const { tryToConnect,echo,statusToServer,flipTheSwitch, usersComunnication,register,giveUserData
    ,addAppliance,editAppliance,removeAppliance,getAllAppliances, group } = require('./commandAndRoles');
const { actionSwitch }=require("./scenarios");

const PORT = 5001;
const wsServer = new WebSocket.Server({port: PORT});
const socketsMap=new Map();//username==>socket


wsServer.on('connection', async function echoHandler(socket){
    socket.isAlive = true;

    let user={username:""};
    addMsgToPrint("A client just connected, "+wsServer.clients.size+" clients connected");


    socket.on('message',async function (msg) {
        addMsgToPrint(msg);
        interpetMsg(msg,user,socket);
    });

    socket.on('close', function () {
        if(user.username!=""){
            socketsMap.delete(user.username);
            Users.disconnect(user.username);
        }
        addMsgToPrint('Client disconnected, '+wsServer.clients.size+" clients connected");
    });


    socket.on('send message to user',async function (username,JsonMsg) {
        if(username==user.username){
            addMsgToPrint("send to "+user.username+": "+JSON.stringify(JsonMsg));
            socket.send(JSON.stringify(JsonMsg));
        }
    });

    socket.on('pong', function(){socket.isAlive=true});
});
// checks if connections alive
const interval = setInterval(function ping() {
    wsServer.clients.forEach(function each(socket) {
      if (socket.isAlive === false) return socket.terminate();

      socket.isAlive = false;
      socket.ping(()=>{});
    });
  }, 20000);

wsServer.on('close', function close() {
    clearInterval(interval);
});
const parseErorr="parseErorr";//{"messageType":"","content":""}when the server cant parse user message

function interpetMsg(msg,user,socket,respond=true){//take msg string and user- and return astring to send back to the user
    try{
        const inputObj=JSON.parse(msg);
        if(!Users.isConnected(user.username)&&inputObj.messageType!=tryToConnect&&inputObj.messageType!=register){//if user is not connected the only option is to try to connect
            respond&&socket.emit('send message to user',user.username,{messageType:parseErorr,content: "you need to login first"});/**todo need be jason */
        }
        else if(!Users.isFunExist(inputObj.messageType)){
            respond&&socket.emit('send message to user',user.username,{messageType:parseErorr,content: "no message with this type"});
        }
        else if(Users.isConnected(user.username) && !Users.canAccess(user.username,inputObj.messageType)){
            respond&&socket.emit('send message to user',user.username,{messageType:parseErorr,content: "you dont have permition for this command"});
        }
        else{
            handleMsg(inputObj,user,socket,respond);
        }
    }
    catch(msg){
        addMsgToPrint("cant parse user:("+user.username+")massage. stack trace "+msg);//todo create print manager to the server
        socket.emit('send message to user',user.username,{messageType:parseErorr,content:"message interpetion error"});
    }
}


function handleMsg(inputObj,user,userSocket,respond=true){
    var res;
    switch(inputObj.messageType){

        case tryToConnect:
            var connected=Users.tryConnectUser(inputObj.username,inputObj.password);
            if(result.isOk(connected)){
                user.username=inputObj.username;
                addMsgToPrint(user.username+" logedin");
            }
            result.isOk(connected)&&socketsMap.set(user.username,userSocket);
            respond&&userSocket.emit('send message to user',user.username,{messageType:"loginResponse",loggedIn:result.isOk(connected),errorDetails:connected.msg });
            break;

        case echo:
            respond&&userSocket.emit('send message to user',user.username,{messageType:"echoResponse",message:inputObj.toEcho });
            break;

        case register:
            res=Users.addUser(inputObj.username,inputObj.password,inputObj.type);
            respond&&userSocket.emit('send message to user',user.username,{messageType:"registerResponse",registered:result.isOk(res),errorDetails:res.msg});
            break;

        case giveUserData:
            res=Users.retrunAllAppliances('state');
            respond&&userSocket.emit('send message to user',user.username,{messageType:"itemsDataInitialiseResponse",success:result.isOk(res),appliances:Users.getUserData(user.username),predicament:result.isOk(res)?res.msg:[] });
            break;

        case addAppliance:
            res=Users.addApplianceToUser(user.username,inputObj.details,inputObj.username);
            respond&&userSocket.emit('send message to user',user.username,{messageType:"addApplianceResponse",added:result.isOk(res)&&result.isOk(Users.getPowerState(inputObj.username)),itemId:(res.msg)[0],state:Users.getPowerState(inputObj.username).msg,errorDetails:(res.msg)[1]});
            break;

        case removeAppliance:
            res=Users.removeApplianceToUser(user.username,inputObj.username);
            respond&&userSocket.emit('send message to user',user.username,{messageType:"removeApplianceResponse",removed:result.isOk(res),errorDetails:res.msg});
            break;
        
        case editAppliance:
            const msgRes=Users.editApplianceDetails();
            respond&&userSocket.emit('send message to user',user.username,{messageType:"editApplianceResponse",success:result.isOk(msgRes),errorDetails:res.msg});
            break;

        case getAllAppliances:
            res=Users.retrunAllAppliances('type');
            respond&&userSocket.emit('send message to user',user.username,{messageType:"getAllAppliancesReponse",success:result.isOk(res),appliances:result.isOk(res)?res.msg:[],errorDetails:result.isOk(res)?"success":res.msg});
            break;

        case flipTheSwitch:
            res=Users.retrunAllAppliances('type');
            var theSwitch=result.isOk(res)?res.msg.find((tuple)=>{
                    return ((tuple.type==='smartSwitch')&&(tuple.username===inputObj.sendToUsername))
                }):undefined;
            if(theSwitch!=undefined){//send comand to switch
                if(Users.isConnected(theSwitch.username)){//check if the sitch is connected
                    const sendToSocket=socketsMap.get(inputObj.sendToUsername);
                    sendToSocket.emit('send message to user',inputObj.sendToUsername,{messageType:"usersCommunication",from:user.username,message:inputObj.msg?"on":"off" });//ask for switch status
                }
                else{
                    respond&&userSocket.emit('send message to user',user.username,{messageType:"flipTheSwitchResponse",success:false,state:"undefined"});
                }
            }
            else{
                respond&&userSocket.emit('send message to user',user.username,{messageType:"flipTheSwitchResponse",success:false,state:"undefined"});
            }
            break;
        case statusToServer:
            res=Users.setPowerState(user.username,inputObj.status=="on");
            if(Users.isConnected(inputObj.sendTo)){
                const sendToSocket=socketsMap.get(inputObj.sendTo);
                respond&&sendToSocket.emit('send message to user',inputObj.sendTo,{messageType:"flipTheSwitchResponse",success:result.isOk(res),state:result.isOk(res)?res.msg:"undefined"});
            }
            break;
        case usersComunnication:
            const sendToSocket=socketsMap.get(inputObj.sendTo);
            sendToSocket.emit('send message to user',inputObj.sendTo,{messageType:"usersCommunication",from:user.username,message:inputObj.msg });
            break;

        case group:
            res =actionSwitch(inputObj.action,inputObj.groupName,inputObj.names);
            if(inputObj.action!="groupScenarioOn"&&inputObj.action!="groupScenarioOff"){
                return userSocket.emit('send message to user',user.username,{messageType:"groupRespones",success:result.isOk(res),names:res.msg.erorrUsers,action:inputObj.action,groupName:inputObj.groupName,errorDetails:res.msg.comment});
            }
            else if(inputObj.action=="groupScenarioOn"){
                res.forEach(app=>{
                    const sen=app.role.groups.get(inputObj.groupName);
                    const soc=socketsMap.get(app.username);
                    interpetMsg(JSON.stringify(sen.onScenario),user,userSocket,false);
                });
            }
            else if(inputObj.action=="groupScenarioOff"){
                res.forEach(app=>{
                    const sen=app.role.groups.get(inputObj.groupName);
                    const soc=socketsMap.get(app.username);
                    interpetMsg(JSON.stringify(sen.offScenario),user,userSocket,false);
                });
            }
            break;

        default:
            return userSocket.emit('send message to user',user.username,{messageType:parseErorr,content:"no message with this type"});

    }
}



addMsgToPrint(" Server is listening on port " + PORT);
