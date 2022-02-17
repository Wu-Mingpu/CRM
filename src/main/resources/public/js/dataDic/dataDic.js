layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    //字典列表展示
    var  tableIns = table.render({
        elem: '#dataDicList',
        url : ctx+'/data_dic/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "dataDicListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'dataDicName', title: '数据类型',align:"center"},
            {field: 'dataDicValue', title: '数据值',  align:'center'},
            {title: '操作', templet:'#dataDicListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });



    // 头工具栏事件
    table.on('toolbar(dataDic)',function (obj) {
        switch (obj.event) {
            case "add":
                openAddOrUpdateDataDicDialog();
                break;
            case "del":
                var checkStatus=table.checkStatus(obj.config.id);
                delDic(checkStatus);
                break;
        }
    });


    /**
     * 删除用户
     * @param checkStatus
     */
    function delDic(checkStatus) {
        var len=checkStatus.data.length;
        if(len<1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }

        //得到要删除记录的id
        var ids="ids=";
        //遍历选择的记录
        for(var i=0;i<len;i++){
            var dataDic =checkStatus.data[i];
            //拼接id值
            if(i==len-1){
                ids+=dataDic.id;
            }else{
                ids+=dataDic.id+"&ids=";
            }
            layer.confirm("确定要删除选中的记录吗？",{icon:3,title:"字典管理"},function (index) {
                layer.close(index);
                //发送ajax请求
                $.ajax({
                    type:"post",
                    url:ctx+"/data_dic/delete",
                    data:ids,
                    success:function (data) {
                        if(data.code==200){
                            layer.msg("删除成功！",{icon:6});
                            tableIns.reload();

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

    table.on('tool(dataDic)',function (obj) {
        var event=obj.event;
        switch (event) {
            case 'edit':
                openAddOrUpdateDataDicDialog(obj.data.id);
                break;

            case 'del':
                layer.confirm("确定要删除这条记录吗？",{icon:3,title:"字典管理"},function (index) {
                    $.ajax({
                        type:"post",
                        url:ctx+"/data_dic/delete",
                        data:{
                            ids:obj.data.id
                        },
                        success:function (data) {
                            if(data.code==200){
                                layer.msg("删除成功！",{icon:6});
                                tableIns.reload();

                            }else{
                                layer.msg(data.msg,{icon:5});
                            }

                        }
                    });
                });

                break;

        }

    });
    function openAddOrUpdateDataDicDialog(dataDicId) {
        var title="字典管理 - 添加字典";
        var url=ctx+"/data_dic/addOrUpdateDataDicPage";
        if(dataDicId!=null){
            title="字典管理 - 更新字典"
            url+="?dataDicId="+dataDicId;
        }
        //打开弹出层
        layui.layer.open({
            title:title,
            content:url,
            type:2,
            maxmin:true,
            area:["650px","250px"]

        });

    }

});
