layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on('submit(login)', function(data){

        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}

        if(isEmpty(data.field.username)){
            layer.msg("用户名称不能为空！",{icon:5})
            return false;
        }
        if(isEmpty(data.field.password)){
            layer.msg("用户密码不能为空！",{icon:5})
            return false;
        }

        //发送ajax请求
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:data.field.username,
                userPwd:data.field.password
            },
            success:function(data){
                //console.log(data)
                if(data.code==200){
                    //判断用户是否选择记住密码
                    if($("#rememberMe").prop("checked")){
                        //设置缓存
                        //设置七天免登录
                        $.cookie("userIdStr",data.result.userIdStr,{expires:7});
                        $.cookie("trueName",data.result.trueName,{expires:7});
                        $.cookie("userName",data.result.userName,{expires:7});
                    }else {
                        //设置缓存
                        $.cookie("userIdStr",data.result.userIdStr);
                        $.cookie("trueName",data.result.trueName);
                        $.cookie("userName",data.result.userName);
                    }



                    window.location.href=ctx+"/main";

                }else {
                    layer.msg(data.msg,{icon:5});
                }
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。


    });

    
    
});