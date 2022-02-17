layui.use(['table','layer',"form"],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        form = layui.form;

    //获取顾客id
    var cusId = $("input[name='cusId']").val();
    //客户列表展示
    var  tableIns = table.render({
        elem: '#customerList',
        url : ctx+'/customer_contact/list?cusId='+cusId,
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "customerListTable",
        cols : [[
            {field: "id", title:'编号',fixed:"true"},
            {field: 'contactTime', title: '交往时间',  align:'center'},
            {field: 'address', title: '交往地址', align:'center'},
            {field: 'overview', title: '概况', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '更新时间', align:'center'},
            {title: '操作', templet:'#customerListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    //头部工具栏监听事件
    table.on('toolbar(customers)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'add':
                openAddUpdateContactDialog();
                break;

        }


    });

    //头部工具栏监听事件
    table.on('tool(customers)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'edit':
                openAddUpdateContactDialog(obj.data.id);
                break;
            case 'del':
                deleteCustomerContact(obj.data.id);
                break;

        }


    });

    /**
     * 打开添加更新客户交往记录框
     * @param contactId
     */

    function openAddUpdateContactDialog(contactId) {
        var title="客户管理 - 添加交往记录";
        var url=ctx+"/customer_contact/toAddUpdateContactPage?cusId="+cusId;

        if(contactId){
            title="客户管理 - 更新交往记录";
            url+="&contactId="+contactId;
        }

        layui.layer.open({
            title:title,
            content:url,
            type:2,
            maxmin:true,
            area:["650px","450px"]
        });

    }


    /**
     * 删除客户交往记录
     * @param contactId
     */
    function deleteCustomerContact(contactId) {
        // 弹出确认框，询问用户是否确认删除
        layer.confirm('您确认要删除该记录吗？',{icon:3, title:'客户管理'}, function (index) {
            //发送ajax请求
            var url = ctx + "/customer_contact/delete";
            $.post(url, {contactId: contactId}, function (data) {
                if (data.code == 200) {
                    layer.msg("删除成功！", {icon: 6});
                    tableIns.reload();


                } else {
                    layer.msg(data.msg, {icon: 5});
                }


            });
        });


    }


});
