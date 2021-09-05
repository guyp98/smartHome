const { User,role,tryToConnect,echo, usersComunnication,register,giveUserData,addAppliance,removeAppliance,all, group } = require("./commandAndRoles");
const { addMsgToPrint } = require('./serverLogs');
const { PrismaClient } = require('@prisma/client')
const prisma = new PrismaClient()

var addToGroups=async(group)=>{
    try{
        const user =await prisma.groups.create({
            data: {
            name: group,
            },
        })
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

var deleteFromGroups=async(group)=>{
    try{
        const user =await prisma.groups.delete({
            where: {
            name: group,
            },
        })
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

var addToUser=async(_username,_password,_roleType)=>{
    try{
        const user =await prisma.users.create({
            data: {
            username: _username,
            password: _password,
            roleType:_roleType,
            },
        })
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

var deletefromUser=async(_username)=>{
    try{
        const user =await prisma.users.delete({
            where: {
            username: _username,
            },
        })
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

var addToUserScenarios=async(userObj)=>{
    if(userObj.role.type!="user"&&userObj.role.type!="admin"){
        var groupsArray=Array.from(userObj.role.groups);
        var toSave=';';
        groupsArray.forEach((entr)=>{
            var str=entr[0].toString()+","+JSON.stringify(entr[1])+";";
            toSave=toSave+str;
        })
        try{
            const user =await prisma.userScenarios.create({
                data: {
                    username: userObj.username,
                    scenMap:toSave,
                },
            })
        }
        catch(err){
            addMsgToPrint("databaseErorr  "+err);
        }
    }
    
}

var deleteUserFromScenarios=async(_username)=>{
    try{
        const user =await prisma.userScenarios.delete({
            where: {
                username: _username,
            },
        })
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

var addUserData=async(_username,_data)=>{
    var allUserData=';';
    _data.forEach((data)=>{
        allUserData=allUserData+JSON.stringify(data)+";"
    })
    try{
        const user =await prisma.userData.create({
            data: {
                username: _username,
                data:allUserData,
            },
        })
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

var deleteUserData=async(_username)=>{
    try{
        const user =await prisma.userData.delete({
            where: {
                username: _username,
            },
        })
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}


var saveUserObj=async(userObj)=>{
    addToUser(userObj.username,userObj.password,userObj.role.type);
    addUserData(userObj.username,userObj.userData);
    addToUserScenarios(userObj)
}

var editUserData= async(_username,userData)=>{
    var allUserData=';';
    userData.forEach((data)=>{
        allUserData=allUserData+JSON.stringify(data)+";"
    })
    const upsertUser = await prisma.userData.upsert({
        where: {
            username: _username,
        },
        update: {
            data:allUserData,
        },
        create: {
            username: _username,
            data:allUserData,
        },
      })
    //await deleteUserData(_username);
    //addUserData(_username,userData);
}
var editUserScenarios= async(userObj)=>{
    await deleteUserFromScenarios(userObj.username);
    addToUserScenarios(userObj);
}






userObj=new User("guy","porat","smartSwitch");
userObj.role.groups.set("groupName",{onScenario:{g:"1"},scenarioOff:{g:"2"}})
userObj.role.groups.set("groupName1",{onScenario:{g:"3"},scenarioOff:{g:"4"}})
userObj.userData.push({username:"1234",detail:"(applianceName,applianceDescription)"});
userObj.userData.push({username:"678",detail:"(applianceName,applianceDescription)"});
//saveUserObj(userObj)

module.exports={editUserScenarios,editUserData,addToGroups,deleteFromGroups,addToUser,deletefromUser,addToUserScenarios,deleteUserFromScenarios,saveUserObj};
