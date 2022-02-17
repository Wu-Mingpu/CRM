layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //服务列表展示
    var tableIns=table.render({
        elem: '#customerServeList',
        url : ctx+'/customer_serve/list?state=fw_001',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "customerServeListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'customer', title: '客户名', minWidth:50, align:"center"},
            {field: 'dicValue', title: '服务类型', minWidth:100, align:'center'},
            {field: 'overview', title: '概要信息', align:'center'},
            {field: 'createPeople', title: '创建人', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
        ]]
    });

    $(".search_btn").click(function () {
        /**
         * 表格重载
         */
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                customer:$("#customer").val(),
                serveType:$("#type").val()

            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    /*头部工具栏监听事件*/
    table.on('toolbar',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'add':
                openAddCustomerServeDialog();
                break;

        }

    })

    /**
     * 打开添加客户服务框
     */
    function openAddCustomerServeDialog() {
        var title="服务管理 - 创建服务";
        var url=ctx+"/customer_serve/toAddCustomerServePage";

        layui.layer.open({
            title:title,
            content:url,
            type: 2,
            maxmin:true,
            area:["700px","500px"]
        });

    }

});
