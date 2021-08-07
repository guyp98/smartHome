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
        return false;
        }
    return password==userObj.password;
}
const tryConnectUser=(userName,password)=>{
    if(authenticate(userName,password) && !isConnected(userName)){
        userObj=usersMap.get(userName);
        userObj.Connected=true;
        return true;
    }
    return false;
}
const disconnect=(userName)=>{
    userObj=usersMap.get(userName);
    userObj.Connected=false;
    
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
            toAdd.push(newItem.details);
        }
        userObj.userData.concat(toAdd);
    });
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
const giveUserData="itemsDataInitialise";//{messageType:":ItemsDataInitialiseResponse",username:"",password:"",type:"" }
const all=[tryToConnect,echo,usersComunnication,register,giveUserData];//all- can use all functions

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
                this.permissions=[tryToConnect,usersComunnication,echo,register,giveUserData];
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

module.exports={isRole,getUserData,addDataToUser,canAccess,
    parseUserAndPassword,addUser,authenticate,tryConnectUser,disconnect,
    isConnected,tryToConnect,echo, usersComunnication,register,giveUserData
};

addUser("guy","porat","user");
addUser("guy2","porat2","user");
