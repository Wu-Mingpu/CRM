$(function(){
    //加载资源树
    loadModuleData();
});
// 定义树形结构对象
var zTreeObj;
/**
 * 发送ajax请求，获取资源，加载资源树
 */
function loadModuleData() {
    //发送ajax请求
    $.ajax({
        type: "get",
        url: ctx + "/module/queryAllModules?roleId="+$("[name='roleId']").val(),
        dataType: "json",
        success: function (data) {

            // 配置信息对象  zTree的参数配置
            var setting = {
                // 使用复选框
                check: {
                    enable: true
                },
                // 使用简单的JSON数据
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                // 绑定函数
                callback: {
                    // onCheck函数：当 checkbox/radio 被选中或取消选中时触发的函数
                    onCheck: zTreeOnCheck
                }
            };
            // 加载zTree树插件
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);

        }

    });

    function zTreeOnCheck(event, treeId, treeNode) {

        // getCheckedNodes(checked):获取所有被勾选的节点集合。
        // 如果checked=true，表示获取勾选的节点；如果checked=false，表示获取未勾选的节点。
        var nodes = zTreeObj.getCheckedNodes(true);

        if (nodes.length >=0) {
            var mIds = "mIds=";
            //遍历选择的记录
            for (var i = 0; i < nodes.length; i++) {
                var module = nodes[i];
                //拼接id值
                if (i == nodes.length- 1) {
                    mIds += module.id;
                } else {
                    mIds += module.id + "&mIds=";
                }



            }
            console.log(mIds);
            // 获取需要授权的角色ID的值（隐藏域）
            var roleId = $("[name='roleId']").val();

            // 发送ajax请求，执行角色的授权操作
            $.ajax({
                type:"post",
                url:ctx + "/role/addGrant",
                data:mIds+"&roleId="+roleId,
                dataType:"json",
                success:function (data) {
                    console.log(data);
                }
            });

        }
    }
}

