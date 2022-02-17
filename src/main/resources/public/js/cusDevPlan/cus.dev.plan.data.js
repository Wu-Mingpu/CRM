layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载计划项数据表格
     */
    var tableINS = table.render({
        id:'cusDevPlanTable'
        // 容器元素的ID属性值
        ,elem: '#cusDevPlanList'
        // 容器的高度 full-差值
        ,height: 'full-125'
        // 单元格最小的宽度
        ,cellMinWidth:95
        // 访问数据的URL（后台的数据接口）
        ,url: ctx + '/cus_dev_plan/list?sId=' + $("input[name='id']").val()
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
            {type:'checkbox', fixed:'center'}
            ,{field: 'id', title: '编号',  sort: true, fixed: 'left'}
            ,{field: 'planItem', title: '计划项', align:'center'}
            ,{field: 'planDate', title: '计划时间', align:'center'}
            ,{field: 'exeAffect', title: '执行效果', align:'center'}
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'updateDate', title: '修改时间', align:'center'}
            ,{title:'操作',templet:'#cusDevPlanListBar', fixed: 'right', align:'center', minWidth:150}
        ]]
    });

    /*头部工具栏事件监听*/
    //触发事件
    table.on('toolbar(cusDevPlans)', function(obj){
        var event = obj.event;
        switch(event){
            case 'add':
                openAddUpdateCusDevPlanDialog();
                break;
            case 'success':
                updateSaleChanceDevResult(2);

                break;
            case 'failed':
                updateSaleChanceDevResult(3);

                break;
        }
    });

    /*行工具栏事件监听*/
    //触发事件
    table.on('tool(cusDevPlans)', function(obj){
        var event = obj.event;
        switch(event){
            case 'edit':
                openAddUpdateCusDevPlanDialog(obj.data.id);
                break;
            case 'del':
                deleteCusDevPlan(obj.data.id);

                    break;

        }
    });
    function deleteCusDevPlan(id) {
        // 弹出确认框，询问用户是否确认删除
        layer.confirm('您确认要删除该记录吗？',{icon:3, title:'开发项数据管理'}, function (index) {
            //发送ajax请求
            var url = ctx + "/cus_dev_plan/delete";
            $.post(url, {id: id}, function (data) {
                if (data.code == 200) {
                    layer.msg("删除成功！", {icon: 6});
                    tableINS.reload();


                } else {
                    layer.msg(data.msg, {icon: 5});
                }


            });
        });

    }

    /**
     * 打开添加更新客户开发计划计划项对话框
     */
    function openAddUpdateCusDevPlanDialog(id){
        var title = "计划项管理 - 添加计划项";
        var url = ctx + "/cus_dev_plan/toAddUpdateCusDevPlanPage?sId="+$("[name='id']").val();

        // 判断计划项的ID是否为空 （如果为空，则表示添加；不为空则表示更新操作）
        if (id) {
            // 更新计划项
            title = "计划项管理 - 更新计划项";
            url+="&id="+id;
        }

        // iframe层
        layui.layer.open({
            type: 2,
            title: title,
            area: ['500px', '300px'],
            content: url,
            maxmin:true
        });

    }

    /**
     * 更新营销机会的开发状态
     * @param devResult
     */
    function updateSaleChanceDevResult(devResult) {
        layer.confirm('您确认要更新该记录吗？',{icon:3, title:'计划项维护'}, function (index) {
            //从隐藏域中获取营销机会的id
            var saleChanceId=$("[name='id']").val();
            url=ctx+"/sale_chance/updateSaleChanceDevResult";

            $.post(url,{saleChanceId:saleChanceId,devResult:devResult},function (data) {
                if(data.code==200){
                    layer.msg("更新成功！",{icon:6});
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else{
                    layer.msg(data.msg,{icon:5})
                }

            });

        });

    }






});
