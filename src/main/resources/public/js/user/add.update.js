layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        //引入formSelects模块
        formSelects=layui.formSelects;

    /**
     * 表单提交submit事件
     */
    form.on('submit(addOrUpdateUser)',function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = top.layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        var url=ctx+"/user/add";
        var userId=$("[name='id']").val();

        if(userId!=null&&userId!=''){
            url=ctx+"/user/update";
        }

        $.ajax({
            type:"post",
            url:url,
            data:data.field,
            success:function (data) {
                if(data.code==200){
                    top.layer.close(index);
                    top.layer.msg("操作成功！",{icon:6});
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else{
                    layer.msg(data.msg,{icon:5});
                }

            }

        });

        return false;
    });

    /**
     * 加载下拉框
     */
    formSelects.config("selectId",{
        type:"post",
        searchUrl:ctx+"/role/queryAllRoles?userId="+$("[name='id']").val(),
        keyName: "roleName",
        keyVal: "id"

    },true);


});