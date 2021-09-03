const { User,role,tryToConnect,echo, usersComunnication,register,giveUserData,addAppliance,removeAppliance,all } = require("./commandAndRoles");
const result = require("./result");
const { usersMap, isUserExist }=require('./userManagement');
const { addMsgToPrint } = require('./serverLogs');
const { addToGroups, deleteFromGroups, editUserScenarios }= require("./SaveData");

var groupsNames=[];
//const groupsMap=new Map(); //groupName => senerio  |||| scenerio=[{username:"____",onScenario:one off the commands in "commansAndRoles.js"},....] 
const actionSwitch=(action,groupName,names)=>
    action=="newGroup"?addNewgroup(groupName,names):
    action=="newItem"?addItemsTogroup(groupName,names):
    action=="remove"? removeItemsFromGroup(groupName,names)  :
    action=="removeAll"?   removeAllItemsFromGroup(groupName):
    action=="editGroup"? EditItemsGroup(groupName,names)  :
    action=="groupScenarioOn"?  groupScenario(groupName) :
    action=="groupScenarioOff"?   groupScenario(groupName):
    result.makeFailure("no action with this name");

const checkAndDoFun=(groupName,names,fun)=>{
        var usernameNotExisting=[];
        var scenarioNotExisting=[];
        names.forEach((item)=>{
            isUserExist(item.username)?(  all.includes(item.onScenario.messageType)&&all.includes(item.offScenario.messageType)? fun(item,groupName):
            scenarioNotExisting=scenarioNotExisting.concat([item.username]) 
                ):
                usernameNotExisting=usernameNotExisting.concat([item.username]);
        });
        if(usernameNotExisting.length==0&&scenarioNotExisting.length==0){
            return result.makeOk({erorrUsers:/*potential erorr*/[],comment:"Senerios added"});
        }
        addMsgToPrint("usernames ["+usernameNotExisting+"] does not exist\n and users ["+ scenarioNotExisting+"] onScenario not valid")
        return result.makeFailure({erorrUsers:usernameNotExisting,comment:"usernames: "+usernameNotExisting+" does not exist\n and users"+ scenarioNotExisting+" onScenario not valid"})
} 
const addGroupToUser=(item,groupName)=>{
    var userObj=usersMap.get(item.username);
    if(userObj!=undefined&&(userObj.role.type!="user"&&userObj.role.type!="admin")){
        if(!userObj.role.groups.has(groupName)){
            userObj.role.groups.set(groupName,{onScenario:item.onScenario,scenarioOff:item.offScenario});
            editUserScenarios(userObj);
        }
    }
}
const removeGroupToUser=(item,groupName)=>{
    var userObj=usersMap.get(item.username);
    if(userObj!=undefined&&(userObj.role.type!="user"&&userObj.role.type!="admin")){
        if(userObj.role.groups.has(groupName)){
            userObj.role.groups.delete(groupName);
            deleteFromGroups(groupName);
        }
    }
}


const addNewgroup=(groupName,names)=>{//newgroup
    if(!groupsNames.includes(groupName)){
        var ret=checkAndDoFun(groupName,names,addGroupToUser);
        if(result.isOk(ret)){
            groupsNames=groupsNames.concat([groupName]);
            addToGroups(groupName);
        }
        else{
            if(res.msg.erorrUsers.length<names.length){
                groupsNames=groupsNames.concat([groupName]);
                addToGroups(groupName);
            }
        }
        //groupsNames=groupsNames.concat([groupName]);//flip
        return ret;
    }
    else{
        return result.makeFailure({erorrUsers:names.map(x=>x.username),comment:"groupName aready exist"});
    }
}



const addItemsTogroup=(groupName,names)=>{
    if(groupsNames.includes(groupName)){
        return checkAndDoFun(groupName,names,addGroupToUser);
    }
    else{
        return result.makeFailure({erorrUsers:names.map(x=>x.username),comment:"groupName not exist"});
    }
}

const removeItemsFromGroup=(groupName,names)=>{
    if(groupsNames.includes(groupName)){
        
        var usernameNotExisting=[];
        names.forEach((item)=>{
            isUserExist(item.username)?removeGroupToUser(item,groupName):
            usernameNotExisting=usernameNotExisting.concat([item.username]);
        });
        if(!isGroupExist(groupName)){
            const index = groupsNames.indexOf(groupName);
            if (index > -1) {
                groupsNames.splice(index, 1);
                deleteFromGroups(groupName);
            }
        }
        if(usernameNotExisting.length==0){
            return result.makeOk({erorrUsers:[],comment:"Senerios deleted"});
        }
        addMsgToPrint("usernames "+usernameNotExisting+" does not exist");
        return result.makeFailure({erorrUsers:usernameNotExisting,comment:"usernames "+usernameNotExisting+" does not exist"});
        
    }
    else{
        return result.makeFailure("groupName not exist");
    }
}
const removeAllItemsFromGroup=(groupName)=>{
    var arr = Array.from(usersMap.values());
    return removeItemsFromGroup(groupName,arr);
}
const EditItemsGroup=(groupName,names)=>{
    removeItemsFromGroup(groupName,names);
    return isGroupExist(groupName)?addItemsTogroup(groupName,names):addNewgroup(groupName,names);
}

const groupScenario=(groupName)=>{//retrun all users that part of the group
    var allUsers = Array.from(usersMap.values());
    return allUsers.filter(user=>{
        if(user.role.type!="user"&&user.role.type!="admin"){
            return Array.from(user.role.groups.keys()).find(group=>group==groupName)!=undefined;
        }
        else{return false;}
    })
}


const isGroupExist=(groupName)=>{
    const iterator=usersMap.values();
    var user=iterator.next();
    while(!user.done){
        if(user.value.role.type!="user"&&user.value.role.type!="admin"){
            if(user.value.role.groups.has(groupName)){
                return true;
            }
        }
        user=iterator.next();
    }
    return false;
}
module.exports={actionSwitch};