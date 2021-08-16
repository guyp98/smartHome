

//all functions names that users can use
const tryToConnect="login"; //{"messageType":"","username":"","password":""} try to connect user
const echo="echo";//{"messageType":"","toEcho":""} echo user msg
const usersComunnication="userCommand";//{"messageType":"","sendTo":"","msg":""} user send command to smartXXXX
const register="register";//{messageType:"registerResponse",registered:true,errorDetails:"register successful"}
const giveUserData="itemsDataInitialise";//{messageType:":",username:"",password:"",type:"" }
const addAppliance="addAppliance";//{messageType:":",details:"" }
const removeAppliance="removeAppliance";//{messageType:":",id:"" }
const getAllAppliances="getAllAppliances";//{messageType:"" }
const flipTheSwitch="flipTheSwitch";//{messageType:"",sendToUsername:"",msg:true/false}
const statusToServer="statusResponse";//{messageType:"",status:"on"/"off"}
const all=[tryToConnect,statusToServer,echo,usersComunnication,register,giveUserData,addAppliance,removeAppliance,getAllAppliances,flipTheSwitch];//all- can use all functions

class role{//all the user permissions
    constructor(type){
        switch(type){
            case "smartSwitch":
                this.permissions=[tryToConnect,usersComunnication,register,statusToServer];
                this.power=false;//false=off,true=on
                break;
            case "smartSensor":
                this.permissions=[tryToConnect,usersComunnication,register,statusToServer];
                this.power=false;
                break;
            case "user":
                this.permissions=[tryToConnect,usersComunnication,echo,register,
                    giveUserData,addAppliance,removeAppliance,getAllAppliances,flipTheSwitch];
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
class User{
    constructor(username,password,roleName){
        this.username=username;
        this.password=password;
        this.Connected=false;
        this.role=new role(roleName);
        this.userData=[];
    }
}



module.exports={User,role,tryToConnect,echo,statusToServer, usersComunnication,register,flipTheSwitch,giveUserData,addAppliance,removeAppliance,getAllAppliances,all
};