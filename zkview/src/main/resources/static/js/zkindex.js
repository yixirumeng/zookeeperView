$(function(){
    var service = new init_service();

    service.run();
})

function init_service(){
}

init_service.prototype.run = function(){
    var obj = this;
    $("#cpZKData").click(function(){
        obj.copyZkData();
    })
}

init_service.prototype.copyZkData = function(){
    data = {};
    data["srcZKAddr"] = $("#srcZKAddr").val();
    data["targetZKAddr"] = $("#targetZKAddr").val();
    data["zkPath"] = $("#zkPathId").val();

    $.ajax({
        url:"/zk/copydata",
        type:"post",
        data:data,

        success:function(res){
            alert("finish");
        }
    });
}
