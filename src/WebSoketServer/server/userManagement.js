//this file is responseble to manage user data


const { User,role,tryToConnect,echo, usersComunnication,register,giveUserData,addAppliance,removeAppliance,all } = require("./commandAndRoles");
const result = require("./result");


const usersMap=new Map(); //username => User

const parseUserAndPassword=(msg)=>{//connverts user messege to user name and password as object
    return JSON.parse(msg);
}
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

const addDataToUser=(username,dataArray)=>{
    userObj=usersMap.get(username);
    var toAdd=[];
    dataArray.forEach(newItem => {//item [{id:"1",detail:""},....]
        var found=false;
        userObj.userData.forEach(oldItem=>{
            if(newItem.id==oldItem.Id&&!found){
                oldItem.details=newItem.details;
                found=true;
            }
        });
        if(!found){
            toAdd.push(newItem);
        }
        userObj.userData.concat(toAdd);
    });
}
const addApplianceToUser=(username,detailes,SentUsername/*aka appliance*/ )=>{
    try{
        userObj=usersMap.get(username);
        
        //create new id for detailes
        var maxId=-1;
        if(userObj.userData.length!=0){
            userObj.userData.forEach(oldItem=>{
                if(maxId<oldItem.id){
                    maxId=oldItem.id;
                }
            }); 
        }
        else{maxId=0;}
        //add the data to user
        userObj.userData.push({id:maxId+1,username:SentUsername,detail:detailes});
        return result.makeOk([maxId+1,"data added succesfuley"]); 
    }
    catch(err){
        return result.makeFailure([-1,err]);
    }
}
const removeApplianceToUser=(username,id)=>{
    try{
        userObj=usersMap.get(username);
        
        for(var i=0;i<userObj.userData.length;i++){
            if(id==userObj.userData[i].id){
                userObj.userData.splice(i,1);
                return result.makeOk([maxId+1,"data removed "]);
            }
        }
        return result.makeFailure("cant remove data (id probably wrong)");
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
            (tempapp.value.role.type!="user"&&tempapp.value.role.type!="admin")&&appli.push({useranme:tempapp.value.username,type:tempapp.value.role.type});
        }
        else if(ret==='state'){
            (tempapp.value.role.type!="user"&&tempapp.value.role.type!="admin")&&appli.push({useranme:tempapp.value.username,type:tempapp.value.role.power});
        }
        tempapp=iterator.next();
    }
    return result.makeOk(appli);
    }
    catch(msg){
        return result.makeFailure(msg);
    } 
}




module.exports={isRole,getUserData,canAccess,
    parseUserAndPassword,addUser,authenticate,tryConnectUser,removeApplianceToUser,disconnect,
    isConnected,addApplianceToUser,isFunExist,retrunAllAppliances: retrunAllAppliances
};

addUser("guy","porat","user");
addUser("guy2","porat2","user");

