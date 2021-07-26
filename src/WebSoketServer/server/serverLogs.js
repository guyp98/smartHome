let allLogs=[];
module.exports=interactWithLoger={
    addMsgToPrint:(msg)=>{//input string
        allLogs.push("on "+(new Date())+" "+msg);
    },
    printPendingLogs:()=>{
        allLogs.forEach(element => {
            console.log(element);
        });
        allLogs=[];
    }
}