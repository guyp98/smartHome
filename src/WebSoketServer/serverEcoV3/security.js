
const usersMap=new Map(); //username => User
//const userIsConnnected=new Map();//check if user is connected(string,blool)
module.exports=securityData={
    //usersMap:new Map(),//username => User
    //userIsConnnected:new Map(),//check if user is connected(string,blool)
    parseUserAndPassword:(msg)=>{//connverts user messege to user name and password as object
        return JSON.parse(msg);
    },
    addUser:(userName,password)=>{
        usersMap.set(userName,new User(userName,password));
        //userIsConnnected.set(userName,false);
    },
    authenticate:(userName,password)=>{ 
        userObj=usersMap.get(userName);
        return password==userObj.password;
    },
    tryConnectUser:(userName,password)=>{
        if(securityData.authenticate(userName,password)&&!securityData.isConnected(userName)){
            userObj=usersMap.get(userName);
            userObj.Connected=true;
            //userIsConnnected.set(userName,true);
            return true;
        }
        return false;
    },
    disconnect:(userName)=>{
        userObj=usersMap.get(userName);
        userObj.Connected=false;
        //userIsConnnected.set(userName,false);
    }, 
    isConnected:(userName)=>{
        userObj=usersMap.get(userName);
        return userObj.Connected;
    }
}

class User{
    constructor(username,password){
        this.username=username;
        this.password=password;
        this.Connected=false;
    }
}