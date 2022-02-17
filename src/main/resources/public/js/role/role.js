layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


       /**
        * 加载数据表格
        */
       var tableINS = table.render({
              id:'roleTable'
              // 容器元素的ID属性值
              ,elem: '#roleList'
              // 容器的高度 full-差值
              ,height: 'full-125'
              // 单元格最小的宽度
              ,cellMinWidth:95
              // 访问数据的URL（后台的数据接口）
              ,url: ctx + '/role/list'
              // 开启分页
              ,page: true
              // 默认每页显示的数量
              ,limit:10
              // 每页页数的可选项
              ,limits:[10,15,20,25]
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
                     ,{field: 'roleName', title: '角色名称', align:'center'}
                     ,{field: 'roleRemark', title: '角色备注', align:'center'}
                     ,{field: 'createDate', title: '创建时间', align:'center'}
                     ,{field: 'updateDate', title: '修改时间', align:'center'}
                     ,{title:'操作',templet:'#roleListBar', fixed: 'right', align:'center', minWidth:150}
              ]]
       });

       /**
        * 搜索按钮的点击事件
        */
       $(".search_btn").click(function () {

              /**
               * 表格重载
               *  多条件查询
               */
              tableINS.reload({
                     // 设置需要传递给后端的参数
                     where: { //设定异步数据接口的额外参数，任意设
                            // 通过文本框，设置传递的参数
                            roleName: $("[name='roleName']").val() // 角色名称
                     }
                     ,page: {
                            curr: 1 // 重新从第 1 页开始
                     }
              });
       });

       /*头部工具栏事件监听*/
       table.on('toolbar(roles)',function (obj) {
              var event=obj.event;
              switch (event) {
                     case 'add':
                            openAddUpdateRolePage();
                            break;
                     case 'grant':
                            //获取选中数据的记录
                            var checkStatus=table.checkStatus(obj.config.id);
                            openSaleGrantDialog(checkStatus);
                            break;

              }

       });

       /*行工具栏事件监听*/
       table.on('tool(roles)',function (obj) {
              var event=obj.event;
              switch (event) {
                     case 'edit':
                            openAddUpdateRolePage(obj.data.id);
                            break;
                     case 'del':
                            deleteRole(obj.data.id);
                            break;

              }

       });

       /**
        * 添加或更新角色
        * @param roleId
        */
       function openAddUpdateRolePage(roleId) {
              var title="角色管理 - 角色添加";
              var url=ctx+"/role/toAddUpdateRolePage";
              if(roleId!=null){
                     title="角色管理 - 角色更新";
                     url+="?roleId="+roleId;
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
        * 删除角色
        */
       function deleteRole(roleId) {
              layer.confirm("确定要删除当前角色吗？",{icon:3,title:"角色管理"},function (index) {

                     $.post(ctx+"/role/delete",{roleId:roleId},function (data) {
                            if(data.code==200){
                                   layer.msg("角色删除成功！",{icon:6});
                                   tableINS.reload();

                            }else{
                                   layer.msg(data.msg,{icon:5});
                            }

                     });

              });

       }

       /**
        * 角色授权
        */
       function openSaleGrantDialog(data) {
              if(data.data.length==0){
                     layer.msg("请选择要授权的角色！",{icon:5});
                     return;
              }

              if(data.data.length>1){
                     layer.msg("请选择单个角色进行授权！",{icon:5});
                     return;
              }

              layui.layer.open({
                     title:"角色管理 - 角色授权",
                     content:ctx+"/role/toRoleGrantPage?roleId="+data.data[0].id,
                     type:2,
                     maxmin:true,
                     area:["400px","500px"]
              });

       }




});
