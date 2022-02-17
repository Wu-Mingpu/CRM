<!DOCTYPE html>
<html>
<head>
    <title>订单详情查看</title>
    <#include "../common.ftl">
</head>
<body class="childrenBody">
<div class="layui-col-md12">
    <div class="layui-card">
        <div class="layui-card-body">
            <form class="layui-form" >
                <input name="id" type="hidden" value="${(order.id)!}"/>
                <div class="layui-form-item layui-row">
                    <div class="layui-col-xs6">
                        <label class="layui-form-label">订单编号</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input"
                                   name="orderNo" id="orderNo"  value="${(order.orderNo)!}" readonly="readonly">
                        </div>
                    </div>
                    <div class="layui-col-xs6">
                        <label class="layui-form-label">下单时间</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input"
                                   name="orderDate" id="orderDate" value="${(order.orderDate?string('yyyy-MM-dd HH:mm:ss'))!}" readonly="readonly">
                        </div>
                    </div>
                </div>

                <div class="layui-form-item layui-row">
                    <div class="layui-col-xs6">
                        <label class="layui-form-label">物流地址</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input"
                                   name="address"  value="${(order.address)!}" readonly="readonly">
                        </div>
                    </div>
                    <#if order.state=1>
                        <div class="layui-col-xs6">
                        <label class="layui-form-label">支付状态</label>
                        <div class="layui-input-block">
                            <input type="text" class="layui-input"
                                   name="state" value="已支付"  readonly="readonly">
                        </div>
                        <#elseif order.state=0>
                            <div class="layui-col-xs6">
                                <label class="layui-form-label">支付状态</label>
                                <div class="layui-input-block">
                                    <input type="text" class="layui-input"
                                           name="state" value="未支付"  readonly="readonly">
                                </div>
                            </div>
                        <#else>
                        <div class="layui-col-xs6">
                            <label class="layui-form-label">支付状态</label>
                            <div class="layui-input-block">
                                <input type="text" class="layui-input"
                                       name="state" value="${(order.state)!}"  readonly="readonly">
                            </div>
                        </div>

                    </#if>

                </div>
            </form>
        </div>
    </div>
</div>

<div class="layui-col-md12">
    <table id="orderDetailList" class="layui-table"  lay-filter="orderDetails"></table>
</div>


<script type="text/javascript" src="${ctx}/js/customer/customer.order.details.js"></script>
</body>
</html>