//this file is responseble to manage user data

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
        userObj=new User(userName,password,type)
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
const addApplianceToUser=(username,detailes)=>{
    try{
        userObj=usersMap.get(username);
        
        //create new id for detailes
        var maxId=-1;
        if(userObj.userData.length!=0){
            userObj.userData.forEach(oldItem=>{
                if(maxId<oldItem.Id){
                    maxId=oldItem.id;
                    oldItem.details=newItem.details;
                }
            }); 
        }
        else{maxId=1;}
        //add the data to user
        userObj.userData.push({id:maxId+1,detail:detailes});
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
                return result.makeOk([maxId+1,"data removed succesfuley"]);
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
class User{
    constructor(username,password,roleName){
        this.username=username;
        this.password=password;
        this.Connected=false;
        this.role=new role(roleName);
        this.userData=[];
    }
}

//all functions names that users can use
const tryToConnect="login"; //{"messageType":"","username":"","password":""} try to connect user
const echo="echo";//{"messageType":"","toEcho":""} echo user msg
const usersComunnication="userCommand";//{"messageType":"","sendTo":"","msg":""} user send command to smartXXXX
const register="register";//{messageType:"registerResponse",registered:true,errorDetails:"register successful"}
const giveUserData="itemsDataInitialise";//{messageType:":",username:"",password:"",type:"" }
const addAppliance="addAppliance";//{messageType:":",details:"" }
const removeAppliance="removeAppliance";//{messageType:":",id:"" }
const all=[tryToConnect,echo,usersComunnication,register,giveUserData,addAppliance,removeAppliance];//all- can use all functions

class role{//all the user permissions
    constructor(type){
        switch(type){
            case "smartSwitch":
                this.permissions=[tryToConnect,usersComunnication,register];
                break;
            case "smartSensor":
                this.permissions=[tryToConnect,usersComunnication,register];
                break;
            case "user":
                this.permissions=[tryToConnect,usersComunnication,echo,register,giveUserData,addAppliance,removeAppliance];
                break;
            case "admin":
                this.permissions=all;
                break;
            default:
                throw "error"//**todo** throw is not ideal need to find better solution for assignRole fail 
        }
        this.type=type;
        
    }
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

module.exports={isRole,getUserData,canAccess,
    parseUserAndPassword,addUser,authenticate,tryConnectUser,removeApplianceToUser,disconnect,
    isConnected,addApplianceToUser,tryToConnect,echo, usersComunnication,register,giveUserData,addAppliance,removeAppliance,isFunExist
};

addUser("guy","porat","user");
addUser("guy2","porat2","user");