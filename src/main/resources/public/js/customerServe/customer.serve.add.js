layui.use(['form', 'layer','jquery_cookie','table'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
        table = layui.table;

        /*表单提交submit事件*/
        form.on("submit(addCustomerServe)",function (data) {
            console.log(data.field);
            var index = top.layer.msg('数据提交中，请稍候...', {icon: 16, time: false, shade: 0.8});
            $.post(ctx + "/customer_serve/add", data.field, function (data) {
                if(data.code==200){
                    setTimeout(function () {
                        top.layer.close(index);
                        top.layer.msg("添加成功！",{icon:6});
                        layer.closeAll("iframe");
                        //刷新父页面
                        parent.location.reload();
                    }, 500);
                }else{
                    layer.msg(data.msg,{icon:5})
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
