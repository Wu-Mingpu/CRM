package com.nbufe.crm.dao;

import com.nbufe.crm.base.BaseMapper;
import com.nbufe.crm.query.CustomerQuery;
import com.nbufe.crm.vo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    //通过客户姓名查询客户对象
    Customer queryCustomerByName(String name);

    //删除客户
    Integer deleteCustomer(Integer customerId);

    //查询流失客户
    List<Customer> queryLossCustomers();

    // 通过客户ID批量更新客户流失状态
    int updateCustomerStateByIds(List<Integer> lossCustomerIds);

    //通过客户名查询客户对象
    Customer selectCustomerByName(String customer);

    //查询客户贡献
    List<Map<String, Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);

    // 查询客户构成
    List<Map<String,Object>> countCustomerMake();

    //统计客户服务
    public List<Map<String,Object>> countCustomerServe();
}