layui.use(['table','layer',"form"],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //客户流失列表展示
    var tableIns = table.render({
        elem: '#customerLossList',
        url: ctx + '/customer_loss/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "customerLossListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'cusNo', title: '客户编号', align: "center"},
            {field: 'cusName', title: '客户名称', align: "center"},
            {field: 'cusManager', title: '客户经理', align: "center"},
            {field: 'lastOrderTime', title: '最后下单时间', align: "center"},
            {field: 'lossReason', title: '流失原因', align: "center"},
            {field: 'confirmLossTime', title: '确认流失时间', align: "center"},
            {field: 'createDate', title: '创建时间', align: "center"},
            {field: 'updateDate', title: '更新时间', align: "center"},
            {title: '操作', fixed: "right", align: "center", minWidth: 150, templet: "#op"}
        ]]
    });

    //多条件查询按钮绑定事件
    $(".search_btn").click(function () {
        /**
         * 表格重载
         */
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                cusName: $("#cusName").val(),
                cusNo: $("#cusNo").val(),
                state: $("#state").val()

            }
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        });


    });

    /*行工具栏事件监听*/
    table.on('tool(customerLosses)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'add':
                openCustomerReprieveDialog("流失管理 - 暂缓措施维护",obj.data.id);
                break;
            case 'info':
                openCustomerReprieveDialog("流失管理 - 暂缓数据查看",obj.data.id);
                break;
        }

    });

    /**
     * 打开客户暂缓列表
     * @param lossId
     */
    function openCustomerReprieveDialog(title,lossId) {
        layui.layer.open({
            title:title,
            content:ctx+"/customer_loss/toCustomerReprievePage?lossId="+lossId,
            type:2,
            maxmin:true,
            area:["750px","550px"]
        });

    }
});
