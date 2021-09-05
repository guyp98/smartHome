const { addMsgToPrint } = require('./serverLogs');
const { PrismaClient } = require('@prisma/client')
const prisma = new PrismaClient()
const userMana=require("./userManagement");
const { User,role,tryToConnect,echo, usersComunnication,register,giveUserData,addAppliance,removeAppliance,all } = require("./commandAndRoles");

loadGroups=async ()=>{
    try{
        const groups =await prisma.groups.findMany();
        return groups.map((group)=>group.name);
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

loadUsers=async ()=>{
    try{
        const users =await prisma.users.findMany();
        return users.map((user)=>{new User(user.username,user.password,user.roleType) });
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}
loadUserData=async (_username)=>{
    try{
        const users =await prisma.userData.findUnique({
            where: {
                username:_username
            },
        });
        users
        
    }
    catch(err){
        addMsgToPrint("databaseErorr  "+err);
    }
}

loadUserData("1234")
