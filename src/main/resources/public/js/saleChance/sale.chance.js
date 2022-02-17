layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 营销机会列表展示
     */

    var tableINS=table.render({
        id:"saleChanceListTable"
        ,elem: '#saleChanceList'
        ,height: 'full-125'
        ,cellMinWidth:95
        ,url: ctx+'/sale_chance/list' //数据接口
        ,page: true //开启分页
        ,limits:[10,15,20,25]
        ,limit:10
        ,toolbar:"#toolbarDemo"
        ,cols: [[ //表头
            {type:'checkbox', fixed:'center'}
            ,{field: 'id', title: '编号',  sort: true, fixed: 'left'}
            ,{field: 'chanceSource', title: '机会来源', align:'center'}
            ,{field: 'customerName', title: '客户名称', align:'center'}
            ,{field: 'cgjl', title: '成功几率', align:'center'}
            ,{field: 'overview', title: '概要', align:'center'}
            ,{field: 'linkMan', title: '联系人', align:'center'}
            ,{field: 'linkPhone', title: '联系号码', align:'center'}
            ,{field: 'description', title: '描述', align:'center'}
            ,{field: 'createMan', title: '创建人', align:'center'}
            ,{field: 'uname', title: '分配人', align:'center'}
            ,{field: 'assignTime', title: '分配时间', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{field: 'state', title: '分配状态', align:'center',templet: function (d) {
                    // 调用函数，返回格式化的结果
                    return formatState(d.state);
                }}
            ,{field: 'devResult', title: '开发状态', align:'center', templet: function (d) {
                    // 调用函数，返回格式化的结果
                    return formatDevResult(d.devResult);
                }}
            ,{title:'操作',templet:'#saleChanceListBar', fixed: 'right', align:'center', minWidth:150}
        ]]
    });


    /**
     * 格式化分配状态值
     *  0 = 未分配
     *  1 = 已分配
     *  其他 = 未知
     * @param state
     */
    function formatState(state) {
        if (state == 0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     *  0 = 未开发
     *  1 = 开发中
     *  2 = 开发成功
     *  3 = 开发失败
     *  其他 = 未知
     * @param devResult
     */
    function formatDevResult(devResult) {
        if (devResult == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if (devResult == 1) {
            return "<div style='color: orange'>开发中</div>";
        } else if (devResult == 2) {
            return "<div style='color: green'>开发成功</div>";
        } else if (devResult == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: blue'>未知</div>";
        }
    }

    $(".search_btn").click(function () {
        /**
         * 表格重载
         */
        tableINS.reload({
            where: { //设定异步数据接口的额外参数，任意设
                customerName:$("#customerName").val(),
                createMan:$("#createMan").val(),
                state:$("#state").val()

            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });


    });

    /*头部工具栏事件监听*/


    //触发事件
    table.on('toolbar(saleChances)', function(obj){
        var event = obj.event;
        switch(event){
            case 'add':
                openAddUpdateSaleChanceDialog();
                break;
            case 'del':
                //获取选中数据的记录
                var checkStatus=table.checkStatus(obj.config.id);
                deleteSaleChance(checkStatus);

                break;
        }
    });

    /**
     * 删除营销机会
     * @param checkStatus
     */
    function deleteSaleChance(checkStatus) {
        var len=checkStatus.data.length;
        if(len<1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }

        //得到要删除记录的id
        var ids="ids=";
        //遍历选择的记录
        for(var i=0;i<checkStatus.data.length;i++){
            var saleChance =checkStatus.data[i];
            //拼接id值
            if(i==checkStatus.data.length-1){
                ids+=saleChance.id;
            }else{
                ids+=saleChance.id+"&ids=";
            }
            layer.confirm("确定要删除选中的记录吗？",{icon:5,title:"营销机会管理"},function (index) {
                layer.close(index);
                //发送ajax请求
                $.ajax({
                    type:"post",
                    url:ctx+"/sale_chance/delete",
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


    /*行工具栏事件监听*/
    //触发事件
    table.on('tool(saleChances)', function(obj){
        var event = obj.event;
        var saleChanceId=obj.data.id;

        switch(event){
            case 'edit':
                openAddUpdateSaleChanceDialog(saleChanceId);
                break;
            case 'del':
                layer.confirm("确定要删除这条记录吗？",{icon:5,title:"营销机会管理"},function (index) {
                    $.ajax({
                        type:"post",
                        url:ctx+"/sale_chance/delete",
                        data:{
                            ids:saleChanceId
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

    /**
     * 打开添加更新营销机会对话框
     */
    function openAddUpdateSaleChanceDialog(saleChanceId) {
        var title="<h3>营销机会管理-添加营销机会</h3>";
        var url=ctx+"/sale_chance/toAddUpdateSaleChancePage";
        if(saleChanceId!=null){
            title="<h3>营销机会管理-更新营销机会</h3>";
            url+="?saleChanceId="+saleChanceId;

        }

        layui.layer.open({
            title:title,
            type:2, //iframe层
            content:url,
            area:["500px","620px"],
            maxmin:true
        });

    }







});
