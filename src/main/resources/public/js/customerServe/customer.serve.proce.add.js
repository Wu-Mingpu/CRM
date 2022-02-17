layui.use(['form', 'layer','jquery_cookie'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
        $ = layui.jquery_cookie($);

    $.post(ctx+"/user/queryAllCustomerManager",function (res) {
        for (var i = 0; i < res.length; i++) {
            if($("input[name='man']").val() == res[i].id ){
                $("#assigner").append("<option value=\"" + res[i].id + "\"selected='selected' >" + res[i].uname + "</option>");
            }else {
                $("#assigner").append("<option value=\"" + res[i].id + "\">" + res[i].uname + "</option>");
            }
        }
        //重新渲染
        layui.form.render("select");
    });

    /*submit事件监听*/
    form.on("submit(addOrUpdateCustomerServe)",function (data) {
        console.log(data)
        var index = top.layer.msg('数据提交中，请稍候...', {icon: 16, time: false, shade: 0.8});

        // 设置处理人
        data.field.serviceProcePeople = $.cookie("trueName");
        data.field.assignTime=new Date(Date.parse(data.field.assignTime));
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