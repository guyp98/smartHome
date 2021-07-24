class security{
static  usersMap=new Map();//username and passwords
static  userIsConnnected=new Map();//check if user is connected(string,blool)
}


export let parseUserAndPassword=(msg)=>{//connverts user messege to user name and password as object
    return JSON.parse(msg);
}
export let addUser=(userName,password)=>{
    security.usersMap.set(userName,password);
    security.userIsConnnected.set(userName,false);
}

 let authenticate=(userName,password)=>{ 
    return password==security.usersMap.get(userName);
}

export let tryConnectUser=(userName,password)=>{
    if(authenticate(userName,password)&&!isConnected(userName)){
        security.userIsConnnected.set(userName,true);
        return true;
    }
    return false;
}
export let disconnect=(userName)=>{
    security.userIsConnnected.set(userName,false);
} 
export const isConnected=(userName)=>{
    return security.userIsConnnected.get(userName);
}

addUser("guy","porat");
