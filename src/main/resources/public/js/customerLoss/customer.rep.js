layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
        
    // 暂缓列表展示
    var  tableIns = table.render({
        elem: '#customerRepList',
        url : ctx+'/customer_reprieve/list?lossId='+$("input[name='id']").val(),
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "customerRepListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'measure', title: '暂缓措施',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#customerRepListBar"}
        ]]
    });

    /*头部工具栏监听事件*/
    table.on('toolbar(customerReps)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'add':
                openAddUpdateCustomerReprieveDialog();
                break;
            case 'confirm':
                updateCustomerLossState();
                break;

        }

    });

    /*行工具栏监听事件*/
    table.on('tool(customerReps)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'edit':
                openAddUpdateCustomerReprieveDialog(obj.data.id);
                break;
            case 'del':
                deleteCustomerReprieve(obj.data.id);
                break;

        }

    });

    /**
     * 添加更新暂缓信息
     * @param repId
     */
    function openAddUpdateCustomerReprieveDialog(repId) {
        var title="客户流失管理 - 添加暂缓";
        var url=ctx+"/customer_loss/toAddUpdateCustomerReprievePage?lossId="+$("input[name='id']").val();
        if(repId){
            title="客户流失管理 - 更新暂缓";
            url+="&repId="+repId;
            console.log(url);
        }
        layui.layer.open({
            title:title,
            content:url,
            type:2,
            maxmin:true,
            area:["450px","250px"]
        });
    }

    /**
     * 删除暂缓信息
     * @param repId
     */
    function deleteCustomerReprieve(repId) {
        // 弹出确认框，询问用户是否确认删除
        layer.confirm('您确认要删除该记录吗？',{icon:3, title:'暂缓流失管理'}, function (index) {
            //发送ajax请求
            var url = ctx + "/customer_reprieve/delete";
            $.post(url, {repId: repId}, function (data) {
                if (data.code == 200) {
                    layer.msg("删除成功！", {icon: 6});
                    tableIns.reload();


                } else {
                    layer.msg(data.msg, {icon: 5});
                }


            });
        });
    }

    /**
     * 更新流失客户的流失状态
     */
    function updateCustomerLossState() {
        // 弹出确认框，询问用户是否确认流失
        layer.confirm('确定标记当前客户为确认流失吗？',{icon:3, title:"客户流失管理"}, function (index) {
            // 关闭确认框
            layer.close(index);

            // prompt层  输入框
            layer.prompt({title: '请输入流失原因', formType: 2}, function(text, index){
                // 关闭输入框
                layer.close(index);

                /**
                 * 发送请求给后台，更新指定流失客户的流失状态
                 *  1. 指定流失客户   流失客户ID （隐藏域）
                 *  2. 流失原因      输入框的内容（text）
                 */
                $.ajax({
                    type:"post",
                    url:ctx + "/customer_loss/updateCustomerLossStateById",
                    data:{
                        id:$("[name='id']").val(), // 流失客户的ID
                        lossReason:text // 流失原因
                    },
                    dataType:"json",
                    success:function (result) {
                        if (result.code == 200) {
                            layer.msg('确认流失成功！', {icon:6});
                            // 关闭窗口
                            layer.closeAll("iframe");
                            // 刷新父页面
                            parent.location.reload();
                        } else {
                            layer.msg(result.msg, {icon:5});
                        }
                    }
                });

            });

        });
    }


});
