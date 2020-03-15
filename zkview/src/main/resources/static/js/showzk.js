$(function(){
    $(".closeNodeWnd").click(function(){
        $(this).parent().parent().hide();

    });
})

function zkservice(){

}

var service = new zkservice();

zkservice.prototype.refresh = function (path) {
        var tempForm = $("<form>");
        tempForm.attr({
            "action":"/zk/get",
            "method":"post"
        });
        tempForm.css("display:node");

        var p = $("<input>");
        p.attr({
            "type":"text",
            "name":"path",
            "value":path,
        });

        tempForm.append(p);
        document.body.appendChild(tempForm[0]);
        tempForm.submit();
}


zkservice.prototype.path_click = function(obj){
    var path = $(obj).text();
    if(path != "/"){
        var path=$("#path").text();
        path = path.substr(0, path.lastIndexOf("/"));
        if(path == "")
            path = "/";
        service.refresh(path);
    }
}

zkservice.prototype.node_click=function(obj){
    var tmpForm = $("<form>");
    tmpForm.attr({
        "action":"/zk/data",
        "method":"post"
    });
    tmpForm.css("display:none");
    
    var path = $("<input>");
    path.attr({
        "type":"text",
        "name":"path",
        "value":$("#path").text()
    });

    var node = $("<input>");
    node.attr({
        "type":"text",
        "name":"node",
        "value":$(obj).text()
    });

    tmpForm.append(path);
    tmpForm.append(node);

    document.body.appendChild(tmpForm[0]);
    tmpForm.submit();
}

zkservice.prototype.fXML = function(obj){
    var fxml = formatXml($(obj).parent().prev().text());
    $(obj).parent().prev().html("<xmp>"+fxml+"</xmp>");
}

zkservice.prototype.delete_node = function(obj){
    if(!confirm("delete the specified path?"))
        return;
    var data = {};
    data["path"] = $("#path").text();
    data["node"] = $(obj).parent().prev().prev().text();

    $.post("/zk/delete", data, function(res){
        service.refresh(res);
    });
}

zkservice.prototype.change_node = function(obj){
    var winWidth = $(window).width();
    var winHeight = $(window).height();
    var popWidth = $(".kvWin").width();
    var popHeight=$(".kvWin").height();

    var popX = (winWidth - popWidth)/2;
    var popY = (winHeight - popHeight)/2;

    $("#changeNode").css("top", popY).css("left", popX).show();

    var p = $("#path").text();
    p = p=="/"?p:p+"/";
    p += $(obj).parent().prev().prev().text();
    $("#changeDataPath").text(p);
    $("#changeDataValue").val($(obj).parent().prev().text());
}

zkservice.prototype.win_add_node = function(obj){
    var data = {};
    data["path"] = $("#path").text();
    data["node"] = $("#pathName").val();
    data["data"] = $("#pathData").val();

    $.ajax({
        url:"/zk/create",
        type:"post",
        data:data,
        success: function(res){
            $("#createNode").hide();
            service.refresh(res);
        }
    });
}

zkservice.prototype.win_change_node = function(obj){
    var data = {};
    data["path"] = $("#changeDataPath").text();
    data["data"] = $("#changeDataValue").val();

    $.ajax({
        url:"/zk/change",
        type:"post",
        data:data,
        success: function(res){
            $("#changeNode").hide();
            var path = $("#path").text();
            service.refresh(path);
        }
    })
}

zkservice.prototype.path_create = function(obj){
    var winWidth = $(window).width();
    var winHeight = $(window).height();
    var popWidth = $(".kvWin").width();
    var popHeight = $(".kvWin").height();

    var popX = (winWidth - popWidth)/2;
    var popY = (winHeight - popHeight)/2;

    $("#createNode").css("top", popY).css("left", popX).show();
}