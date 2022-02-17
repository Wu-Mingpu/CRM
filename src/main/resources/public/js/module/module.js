layui.use(['table', 'treetable'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var treeTable = layui.treetable;

    // 渲染表格
    treeTable.render({
        treeColIndex: 1,
        treeSpid: -1,
        treeIdName: 'id',
        treePidName: 'parentId',
        elem: '#munu-table',
        url: ctx+'/module/list',
        toolbar: "#toolbarDemo",
        treeDefaultClose:true,
        page: true,
        cols: [[
            {type: 'numbers'},
            {field: 'moduleName', minWidth: 100, title: '菜单名称'},
            {field: 'optValue', title: '权限码'},
            {field: 'url', title: '菜单url'},
            {field: 'createDate', title: '创建时间'},
            {field: 'updateDate', title: '更新时间'},
            {
                field: 'grade', width: 80, align: 'center', templet: function (d) {
                    if (d.grade == 0) {
                        return '<span class="layui-badge layui-bg-blue">目录</span>';
                    }
                    if(d.grade==1){
                        return '<span class="layui-badge-rim">菜单</span>';
                    }
                    if (d.grade == 2) {
                        return '<span class="layui-badge layui-bg-gray">按钮</span>';
                    }
                }, title: '类型'
            },
            {templet: '#auth-state', width: 220, align: 'center', title: '操作'}
        ]],
        done: function () {
            layer.closeAll('loading');
        }
    });

    /*头部工具栏事件监听*/
    table.on("toolbar(munu-table)",function (data) {
        var event=data.event;
        switch (event) {
            case 'expand':
                //全部展开
                treeTable.expandAll('#munu-table');
                break;
            case 'fold':
                //全部折叠
                treeTable.foldAll('#munu-table');
                break;
            case 'add':
                openAddModuleDialog(0,-1);
                break;

        }


    });

    /*行工具栏事件监听*/
    table.on("tool(munu-table)",function (data) {
        switch (data.event) {
            case 'add':
                if(data.data.grade==2){
                    layer.msg("暂不支持添加四级菜单！",{icon:5});
                    return;
                }
                openAddModuleDialog(data.data.grade+1,data.data.id);
                break;
            case 'edit':
                openUpdateModuleDialog(data.data.id);
                break;
            case 'del':
                deleteModule(data.data.id,data);
                break;

        }

    });

    /**
     * 打开菜单添加界面
     * @param grade
     * @param parentId
     */
    function openAddModuleDialog(grade,parentId) {
        layui.layer.open({
            type:2,
            title:"资源管理-资源添加",
            content:ctx+"/module/toAddModulePage?grade="+grade+"&parentId="+parentId,
            maxmin:true,
            area:["750px","500px"]
        });

    }

    /**
     * 打开菜单添加界面
     */
    function openUpdateModuleDialog(moduleId) {
        layui.layer.open({
            type:2,
            title:"资源管理-资源编辑",
            content:ctx+"/module/toUpdateModulePage?moduleId="+moduleId,
            maxmin:true,
            area:["750px","500px"]
        });


    }

    /**
     * 删除资源
     */
    function deleteModule(id,obj) {
        layer.confirm("确定要删除这条记录吗？",{icon:5,title:"菜单管理"},function (index) {
            $.ajax({
                type:"post",
                url:ctx+"/module/delete",
                data:{
                    id:id
                },
                success:function (data) {
                    if(data.code==200){
                        layer.msg("删除成功！",{icon:6});
                        obj.del();

                    }else{
                        layer.msg(data.msg,{icon:5});
                    }

                }
            });
        });

    }

    

    
});