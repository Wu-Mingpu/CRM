layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /*加载下拉框*/

    $.post(ctx+"/user/queryAllCustomerManager",function (data) {
        for(var i=0;i<data.length;i++){
            $("#assigner").append("<option value=\""+data[i].id+"\">"+data[i].uname+"</option>");

            layui.form.render("select");

        }
    });

    /*submit事件监听*/
    form.on("submit(addOrUpdateCustomerServe)",function (data) {
        console.log(data.field);
        var index = top.layer.msg('数据提交中，请稍候...', {icon: 16, time: false, shade: 0.8});
        $.post(ctx+"/customer_serve/update",data.field,function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！",{icon:6});
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        });
        return false;

    });

    //关闭弹出层
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭

    });


});