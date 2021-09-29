let allLogs=[];
module.exports=interactWithLoger={
    addMsgToPrint:(msg)=>{//input string
        allLogs.push("on "+(new Date())+" "+msg);
        interactWithLoger.printPendingLogs();
    },
    printPendingLogs:()=>{
        allLogs.forEach(element => {
            console.log(element);
        });
        allLogs=[];
    }
}