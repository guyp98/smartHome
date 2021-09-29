const { addMsgToPrint } = require('./serverLogs');
const { PrismaClient } = require('@prisma/client')
const prisma = new PrismaClient()
//const userMana=require("./userManagement");
const { User,role,tryToConnect,echo, usersComunnication,register,giveUserData,addAppliance,removeAppliance,all } = require("./commandAndRoles");

const loadGroups=async ()=>{
    try{
        const groups =await prisma.groups.findMany();
        return groups.map((group)=>group.name);
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

const loadUsers=async ()=>{
    try{
        var users =await prisma.users.findMany();
        return users.map((user)=>{return new User(user.username,user.password,user.roleType) });
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}
const loadUserData=async (_username)=>{
    try{
        const users =await prisma.userData.findUnique({
            where: {
                username:_username
            },
        });
        var strDataArray=users.data.split(";").filter((item)=>item!='');
        var JsonDataArray=strDataArray.map((str)=>JSON.parse(str));
        return JsonDataArray;
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}
const loadScenario= async (_username)=>{
    try{
        const users =await prisma.userScenarios.findUnique({
            where: {
                username:_username
            },
        });
        var strScenariosArray=users.scenMap.split(";").filter((item)=>item!='');
        var ScenariosArray=strScenariosArray.map((str)=>{
            var keyStr=str.split(',',1)[0];
            var valStr=str.slice(keyStr.length+1);
            return {key:keyStr,val:JSON.parse(valStr)};
        })
        return ScenariosArray;
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}


module.exports={loadScenario,loadUserData,loadUsers,loadGroups}