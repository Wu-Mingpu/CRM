layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 客户开发列表展示
     */

    var tableINS=table.render({
        id:"saleChanceListTable"
        ,elem: '#saleChanceList'
        ,height: 'full-125'
        ,cellMinWidth:95
        ,url: ctx+'/sale_chance/list?state=1&flag=1' //数据接口
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
            ,{field: 'createDate', title: '创建时间', align:'center'}
            ,{field: 'devResult', title: '开发状态', align:'center', templet: function (d) {
                    // 调用函数，返回格式化的结果
                    return formatDevResult(d.devResult);
                }}
            ,{title:'操作',templet:'#op', fixed: 'right', align:'center', minWidth:150}
        ]]
    });

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
                devResult:$("#devResult").val()

            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });


    });

    /*行工具栏事件监听*/
    //触发事件
    table.on('tool(saleChances)', function(obj){
        var event = obj.event
        switch(event){
            case 'info':
                openCusDevPlanDialog("计划项数据维护",obj.data.id);
                break;
            case 'dev':
                openCusDevPlanDialog("计划项数据开发",obj.data.id);
                break;
        }
    });

    /**
     * 打开客户开发计划对话框
     * @param title
     * @param id
     */
    function openCusDevPlanDialog(title, id) {
        layui.layer.open({
            title:title,
            type:2, //iframe层
            content:ctx+"/cus_dev_plan/toCusDevPlanDataPage?sId="+id,
            area:["750px","550px"],
            maxmin:true
        });

    }





});
