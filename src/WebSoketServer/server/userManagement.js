const usersMap=new Map(); //username => User

module.exports=userData={
    parseUserAndPassword:(msg)=>{//connverts user messege to user name and password as object
        return JSON.parse(msg);
    },
    addUser:(userName,password)=>{
        usersMap.set(userName,new User(userName,password));
    },
    authenticate:(userName,password)=>{ 
        userObj=usersMap.get(userName);
        return password==userObj.password;
    },
    tryConnectUser:(userName,password)=>{
        if(userData.authenticate(userName,password) &&
                                    !userData.isConnected(userName)){
            userObj=usersMap.get(userName);
            userObj.Connected=true;
            return true;
        }
        return false;
    },
    disconnect:(userName)=>{
        userObj=usersMap.get(userName);
        userObj.Connected=false;
        
    }, 
    isConnected:(userName)=>{
        userObj=usersMap.get(userName);
        if(userObj!=undefined){
            return userObj.Connected;
        }
        else{
            return false;
        }
    }
}

class User{
    constructor(username,password){
        this.username=username;
        this.password=password;
        this.Connected=false;
    }
}


userData.addUser("guy","porat");
userData.addUser("guy2","porat2");