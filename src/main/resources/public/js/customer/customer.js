layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form;

    //客户列表展示
    var  tableINS = table.render({
        elem: '#customerList',
        url : ctx+'/customer/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "customerListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'name', title: '客户名',align:"center"},
            {field: 'fr', title: '法人',  align:'center'},
            {field: 'khno', title: '客户编号', align:'center'},
            {field: 'area', title: '地区', align:'center'},
            {field: 'cusManager', title: '客户经理',  align:'center'},
            {field: 'myd', title: '满意度', align:'center'},
            {field: 'level', title: '客户级别', align:'center'},
            {field: 'xyd', title: '信用度', align:'center'},
            {field: 'address', title: '详细地址', align:'center'},
            {field: 'postCode', title: '邮编', align:'center'},
            {field: 'phone', title: '电话', align:'center'},
            {field: 'webSite', title: '网站', align:'center'},
            {field: 'fax', title: '传真', align:'center'},
            {field: 'zczj', title: '注册资金', align:'center'},
            {field: 'yyzzzch', title: '营业执照', align:'center'},
            {field: 'khyh', title: '开户行', align:'center'},
            {field: 'khzh', title: '开户账号', align:'center'},
            {field: 'gsdjh', title: '国税', align:'center'},
            {field: 'dsdjh', title: '地税', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '更新时间', align:'center'},
            {title: '操作', templet:'#customerListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    //多条件查询按钮绑定事件
    $(".search_btn").click(function () {
        /**
         * 表格重载
         */
        tableINS.reload({
            where: { //设定异步数据接口的额外参数，任意设
                cusName:$("#name").val(),
                cusNo:$("#khno").val(),
                cusLevel:$("#level").val()

            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });


    });

    //头部工具栏监听事件
    table.on('toolbar(customers)',function (obj) {
        //选择的字段信息
        var checkStatus = table.checkStatus(obj.config.id);
        var event=obj.event;
        switch (event) {
            case 'add':
                openAddUpdateCustomerDialog();
                break;
            case 'link':
                openLinkmanDialog(checkStatus.data);
                break;
            case 'recode':
                openCustomerContactDialog(checkStatus.data);
                break;
            case 'order':
                openOrderInfoDialog(checkStatus.data);
                break;

        }

    });

    //行工具栏监听事件
    table.on('tool(customers)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'edit':
                openAddUpdateCustomerDialog(obj.data.id);
                break;
            case 'del':
                deleteCustomer(obj.data.id);
                break;

        }

    });

    /**
     * 打开添加更新客户对象框
     */
    function openAddUpdateCustomerDialog(customerId) {
        var title="客户信息管理 - 添加客户";
        var url=ctx+"/customer/toAddUpdateCustomerPage";

        if(customerId){
            title="客户信息管理 - 更新客户";
            url+="?customerId="+customerId;
        }

        layui.layer.open({
            title:title,
            type:2,
            content:url,
            maxmin:true,
            area:["700px","500px"]

        });

    }

    /**
     * 删除客户
     * @param customerId
     */
    function deleteCustomer(customerId) {
        layer.confirm("确定要删除当前客户吗？",{icon:3,title:"客户管理"},function (index) {

            $.post(ctx+"/customer/delete",{customerId:customerId},function (data) {
                if(data.code==200){
                    layer.msg("删除成功！",{icon:6});
                    tableINS.reload();
                }else{
                    layer.msg(data.msg,{icon:5});
                }
            });


        });


    }

    /**
     * 打开订单查看界面
     * @param datas
     */
    function openOrderInfoDialog(datas) {
        if(datas.length==0){
            layer.msg("请选择需要查看的订单！",{icon:5});
            return;
        }
        if(datas.length>1){
            layer.msg("暂不支持批量查看！",{icon:5});
            return;
        }

        var url=ctx+"/customer/toOrderInfoPage?cId="+datas[0].id;
        layui.layer.open({
            title:"客户管理 - 订单信息展示",
            content:url,
            type:2,
            maxmin:true,
            area:["900px","550px"]
        });
    }

    /**
     * 打开联系人管理界面
     */
    function openLinkmanDialog(datas) {
        if(datas.length==0){
            layer.msg("请选择需要查看的客户！",{icon:5});
            return;
        }
        if(datas.length>1){
            layer.msg("暂不支持批量查看！",{icon:5});
            return;
        }

        var url=ctx+"/customer/toLinkmanPage?cusId="+datas[0].id;
        layui.layer.open({
            title:"客户管理 - 联系人管理",
            content:url,
            type:2,
            maxmin:true,
            area:["700px","400px"]
        });
    }

    /**
     * 打开客户交往记录界面
     * @param datas
     */
    function openCustomerContactDialog(datas) {
        if(datas.length==0){
            layer.msg("请选择需要查看的客户！",{icon:5});
            return;
        }
        if(datas.length>1){
            layer.msg("暂不支持批量查看！",{icon:5});
            return;
        }
        var title="客户管理 - 交往记录";
        var url=ctx+"/customer/toCustomerContactPage?cusId="+datas[0].id;

        layui.layer.open({
            title:title,
            content:url,
            type:2,
            maxmin:true,
            area:["900px","550px"]
        });


    }


});
