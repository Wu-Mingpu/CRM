layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载用户管理数据表格
     */
    var tableINS = table.render({
        id:'userTable'
        // 容器元素的ID属性值
        ,elem: '#userList'
        // 容器的高度 full-差值
        ,height: 'full-125'
        // 单元格最小的宽度
        ,cellMinWidth:95
        // 访问数据的URL（后台的数据接口）
        ,url: ctx + '/user/list'
        // 开启分页
        ,page: true
        // 默认每页显示的数量
        ,limit:10
        // 每页页数的可选项
        ,limits:[10,20,30,40,50]
        // 开启头部工具栏
        ,toolbar:'#toolbarDemo'
        // 表头
        ,cols: [[
            // field：要求field属性值与返回的数据中对应的属性字段名一致
            // title：设置列的标题
            // sort：是否允许排序（默认：false）
            // fixed：固定列
            {type:'checkbox', fixed:'left',width:50}
            ,{field: 'id', title: '编号',  fixed: 'true',width:80}
            ,{field: 'userName', title: '用户名', minWidth: 50,align:'center'}
            ,{field: 'email', title: '用户邮箱',  minWidth: 100,align:'center'}
            ,{field: 'phone', title: '用户电话',  minWidth: 100,align:'center'}
            ,{field: 'trueName', title: '真实名字', align:'center'}
            ,{field: 'createDate', title: '创建时间',  minWidth: 150,align:'center'}
            ,{field: 'updateDate', title: '更新时间',  minWidth: 150,align:'center'}
            ,{title:'操作',templet:'#userListBar', fixed: 'right', align:'center', minWidth:150}
        ]]
    });

    $(".search_btn").click(function () {
        /**
         * 表格重载
         */
        tableINS.reload({
            where: { //设定异步数据接口的额外参数，任意设
                userName:$("#userName").val(),
                email:$("#email").val(),
                phone:$("#phone").val()

            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });


    });

    /**
     * 监听头部工具栏
     */

    table.on('toolbar(users)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'add':
                openAddUpdateUserDialog();

                break;
            case 'del':
                var checkStatus=table.checkStatus(obj.config.id);
                deleteUser(checkStatus);

                break;

        }

    });

    /**
     * 删除用户
     * @param checkStatus
     */
    function deleteUser(checkStatus) {
        var len=checkStatus.data.length;
        if(len<1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }

        //得到要删除记录的id
        var ids="ids=";
        //遍历选择的记录
        for(var i=0;i<len;i++){
            var user =checkStatus.data[i];
            //拼接id值
            if(i==len-1){
                ids+=user.id;
            }else{
                ids+=user.id+"&ids=";
            }
            layer.confirm("确定要删除选中的记录吗？",{icon:3,title:"用户管理"},function (index) {
                layer.close(index);
                //发送ajax请求
                $.ajax({
                    type:"post",
                    url:ctx+"/user/delete",
                    data:ids,
                    success:function (data) {
                        if(data.code==200){
                            layer.msg("删除成功！",{icon:6});
                            tableINS.reload();

                        }else{
                            layer.msg(data.msg,{icon:5});
                        }

                    }

                });

            });

        }
    }


    /**
     * 监听行工具栏
     */

    table.on('tool(users)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'edit':
                openAddUpdateUserDialog(obj.data.id);

                break;
            case 'del':
                layer.confirm("确定要删除这条记录吗？",{icon:3,title:"用户管理"},function (index) {
                    $.ajax({
                        type:"post",
                        url:ctx+"/user/delete",
                        data:{
                            ids:obj.data.id
                        },
                        success:function (data) {
                            if(data.code==200){
                                layer.msg("删除成功！",{icon:6});
                                tableINS.reload();

                            }else{
                                layer.msg(data.msg,{icon:5});
                            }

                        }
                    });
                });

                break;

        }

    });
    function openAddUpdateUserDialog(userId) {
        var title="用户管理 - 添加用户";
        var url=ctx+"/user/toAddUpdateUserPage";
        if(userId!=null){
            title="用户管理 - 编辑用户"
            url+="?userId="+userId;
        }
        //打开弹出层
        layui.layer.open({
            title:title,
            content:url,
            type:2,
            maxmin:true,
            area:["650px","400px"]

        });

    }

});