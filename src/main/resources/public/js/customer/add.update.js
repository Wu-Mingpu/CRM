layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 监听表单submit事件
     */
    form.on('submit(addOrUpdateCustomer)', function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });

        var url=ctx+"/customer/add";
        //获取隐藏域的值
        if($("[name='id']").val()){
            url=ctx+"/customer/update";
        }

        //发送ajax请求


        var params=data.field;

        $.post(url,params,function (data) {
            if(data.code==200){
                console.log(params);
                layer.msg("操作成功！",{icon:6});
                //关闭加载层
                layer.close(index);
                //关闭所有iframe层
                layer.closeAll("iframe");
                //刷新父窗口
                parent.location.reload();

            }else{

                layer.msg(data.msg,{icon:5});
            }

        });
        return false;//阻止表单提交
    });

    //关闭弹出层
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭

    });

    


});