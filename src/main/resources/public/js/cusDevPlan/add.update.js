layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /**
     * 表单Submit监听
     */
    form.on('submit(addOrUpdateCusDevPlan)', function (data) {
        //弹出loading层
        var index = top.layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });

        // 请求的地址
        var url = ctx + "/cus_dev_plan/add";
        if($("[name='id']").val()){
            url=ctx+"/cus_dev_plan/update";
        }

        //发送ajax请求
        $.post(url,data.field,function (data) {
            if(data.code==200){
                //延时0.5秒
                setTimeout(function () {
                    // 提示成功
                    top.layer.msg("操作成功！",{icon:6});
                    // 关闭加载层
                    top.layer.close(index);
                    // 关闭弹出层
                    layer.closeAll("iframe");
                    // 刷新父窗口，重新加载数据
                    parent.location.reload();

                },500);
            }else{
                layer.msg(data.msg,{icon:5});
            }

        });




        // 阻止表单提交
        return false;
    });


    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
        parent.layer.close(index); // 再执行关闭
    });





});