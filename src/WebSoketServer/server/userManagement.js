
const usersMap=new Map(); //username => User


const parseUserAndPassword=(msg)=>{//connverts user messege to user name and password as object
    return JSON.parse(msg);
}
const addUser=(userName,password,type)=>{
    usersMap.set(userName,new User(userName,password,type));
}
const authenticate=(userName,password)=>{ 
    userObj=usersMap.get(userName);
    return password==userObj.password;
}
const tryConnectUser=(userName,password)=>{
    if(authenticate(userName,password) &&
                                !isConnected(userName)){
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


class User{
    constructor(username,password,roleName){
        this.username=username;
        this.password=password;
        this.Connected=false;
        this.role=assignRole(roleName);
    }
}
class role{//all the user permissions
    constructor(type,permissions){
        this.type=type;
        this.permissions=permissions;//type of messages role can do
    }
}

//all functions names that users can use
const tryToConnect="try connect"; //{"messageType":"","username":"","password":""} try to connect user
const echo="echo";//{"messageType":"","toEcho":""} echo user msg
const all=[tryToConnect,echo];//all- can use all functions

assignRole=(name)=>{
    switch(name){
        case "smartSwitch":
            return new role("smartSwitch",[tryToConnect]);
        case "user":
            return new role("user",[tryToConnect,echo]); 
        case "admin":
            return new role("admin",all);
        default:
            throw "error"
    }
    

}

const canAccess=(userName,funcName)=>{
    userRole=usersMap.get(userName).role;
    return userRole.permissions.includes(funcName);
}



module.exports={canAccess,parseUserAndPassword,addUser,authenticate,tryConnectUser,disconnect,isConnected,tryToConnect,echo};


addUser("guy","porat","user");
addUser("guy2","porat2","user");