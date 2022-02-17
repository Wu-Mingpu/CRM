layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on('submit(saveBtn)', function(data){

        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}

        var userPwd=data.field.old_password;
        var newPwd=data.field.new_password;
        var repeatPwd=data.field.again_password;

        if(userPwd==newPwd){
            layer.msg("新密码不能与原始密码相同！",{icon:5})
            return ;
        }
        if(repeatPwd!=newPwd){
            layer.msg("两次新密码输入不一致！",{icon:5})
            return ;
        }

        $.ajax({
            type:"post",
            url:ctx+"/user/updateUserPwd",
            data:{
                userPwd:userPwd,
                newPwd:newPwd,
                repeatPwd:repeatPwd
            },
            success:function (data) {
                if(data.code==200){
                    layer.msg("修改密码成功，将在3s后跳转至登录界面...",function () {
                        //删除用户登录的Cookie信息
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转回首页
                        window.parent.location.href=ctx+"/index";
                    });
                }else{
                    layer.msg(data.msg,{icon:5});
                }

            }
        });




        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。


    });



});