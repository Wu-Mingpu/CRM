layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /*表单监听submit事件*/
    form.on("submit(saveBtn)", function (data) {
        console.log(data.field);
        $.ajax({
            type:"post",
            url:ctx+"/user/updateBasicInformation",
            data:data.field,
            success:function (data) {
                if(data.code==200){
                    layer.msg("保存成功！",{icon:6});
                }else{
                    layer.msg(data.msg,{icon:5});
                }

            }
        });
        return false;
    });



});