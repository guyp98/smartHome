//this file is responseble to manage user data


const { User,role,tryToConnect,echo, usersComunnication,register,giveUserData,addAppliance,removeAppliance,all } = require("./commandAndRoles");
const result = require("./result");


const usersMap=new Map(); //username => User

const parseUserAndPassword=(msg)=>{//connverts user messege to user name and password as object
    return JSON.parse(msg);
}

const isUserExist=(username)=>usersMap.has(username);


const addUser=(userName,password,type)=>{//register new user
    try{
        if(usersMap.has(userName)){
            return result.makeFailure("already exist");
        }
        userObj=new User(userName,password,type);
        usersMap.set(userName,userObj);
        return result.makeOk("register successful");
    }
    catch(msg){
        console.log(msg);
        return result.makeFailure("cant add user");
        
    }
    
}
const authenticate=(userName,password)=>{ 
    userObj=usersMap.get(userName);
    if(!userObj){
        return result.makeFailure("no user with this user name");
        }
    return password==userObj.password?result.makeOk(""):result.makeFailure("password incorrect");
}
const tryConnectUser=(userName,password)=>{
    var auth=authenticate(userName,password);
    if(result.isOk(auth)){
        var isconn=isConnected(userName);
        if( !result.isOk(isconn)){
            userObj=usersMap.get(userName);
            if(userObj==undefined){return result.makeFailure("user not exist")}
            userObj.Connected=true;
            return result.makeOk("login successful");
        }
        return result.makeFailure("user already connected")
    }
    return auth;
}
const disconnect=(userName)=>{
    userObj=usersMap.get(userName);
    if(userObj==undefined){return result.makeFailure("user not exist")}
    userObj.Connected=false; 
    return result.makeOk("user disconnected");
}

const isConnected=(userName)=>{
    userObj=usersMap.get(userName);
    if(userObj!=undefined){
        return userObj.Connected;
    }
    else{
        return false;
    }
}

const editApplianceDetails=(username,details)=>{
    userObj=usersMap.get(username);
    const item=userObj.userData.find(item=>{item.username==SentUsername});
    if(undefined!=item){
        item.details=details
        return result.makeOk("detailes edited");
    }
    else{
        return result.makeFailure("no details with this username");
    }
}
const addApplianceToUser=(username,detailes,SentUsername/*aka appliance*/ )=>{
    try{
        userObj=usersMap.get(username);
        if(undefined!=userObj.userData.find(item=>{item.username==SentUsername})){
            throw 'username already exist';
        }
        //add the data to user
        userObj.userData.push({username:SentUsername,detail:detailes});
        return result.makeOk([SentUsername,"data added succesfuley"]); 
    }
    catch(err){
        return result.makeFailure([SentUsername,err]);
    }
}
const removeApplianceToUser=(username,toRemove)=>{
    try{
        userObj=usersMap.get(username);
        
        for(var i=0;i<userObj.userData.length;i++){
            if(toRemove==userObj.userData[i].username){
                userObj.userData.splice(i,1);
                return result.makeOk([toRemove,"data removed "]);
            }
        }
        return result.makeFailure("cant remove data ("+toRemove+" probably wrong)");
    }
    catch(err){
        return result.makeFailure(err);
    }
}
const getUserData=(username)=>{
    userObj=usersMap.get(username);
    return userObj.userData;
}


const canAccess=(userName,funcName)=>{
    let userRole=usersMap.get(userName).role;
    return userRole.permissions.includes(funcName);
}
const isRole=(userName,roleType)=>{
    let userRole=usersMap.get(userName).role;
    return userRole.type==roleType;
}
const isFunExist=(funType)=>{
    return all.includes(funType);
}

const retrunAllAppliances=(ret)=>{
    try{
    var iterator=usersMap.values();
    var appli=[];
    var tempapp=iterator.next();
    while(!tempapp.done){
        if(ret==='type'){
            (tempapp.value.role.type!="user"&&tempapp.value.role.type!="admin")&&appli.push({
                username:tempapp.value.username,
                type:tempapp.value.role.type
            });
        }
        else if(ret==='state'){
            (tempapp.value.role.type!="user"&&tempapp.value.role.type!="admin")&&appli.push({
                username:tempapp.value.username,
                state:tempapp.value.role.power,
                groups:Array.from(tempapp.value.role.groups).map(group=>{
                    return {name:group[0],scenarioOn:group[1],scenerioOff:group[2]}
                })
            });
        }
        tempapp=iterator.next();
    }
    return result.makeOk(appli);
    }
    catch(msg){
        return result.makeFailure(msg);
    } 
}

const setPowerState=(username,powerState)=>{
    var user=usersMap.get(username);
    if(user==undefined){
        return result.makeFailure("no user with this username");
    }
    else if(user.role.type=="user"&&user.role.type=="admin"){
        return result.makeFailure("user or adimin not have power state"); 
    }
    else{
        user.role.power=powerState;
        return result.makeOk(user.role.power);
    }
}
const getPowerState=(username)=>{
    var user=usersMap.get(username);
    if(user==undefined){
        return result.makeFailure("no user with this username");
    }
    else if(user.role.type=="user"&&user.role.type=="admin"){
        return result.makeFailure("user or adimin not have power state"); 
    }
    else{
        //console.log(user.role.power);
        return result.makeOk(user.role.power);
    }
}


module.exports={isRole,getUserData,canAccess,
    parseUserAndPassword,addUser,authenticate,tryConnectUser,removeApplianceToUser,disconnect,
    isConnected,addApplianceToUser,editApplianceDetails,setPowerState,isFunExist,retrunAllAppliances, getPowerState
    ,isUserExist,usersMap
};

addUser("guy","porat","user");
addUser("guy2","porat2","user");

