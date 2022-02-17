package com.nbufe.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbufe.crm.base.BaseService;
import com.nbufe.crm.dao.CustomerLossMapper;
import com.nbufe.crm.dao.CustomerMapper;
import com.nbufe.crm.dao.CustomerOrderMapper;
import com.nbufe.crm.query.CustomerQuery;
import com.nbufe.crm.utils.AssertUtil;
import com.nbufe.crm.utils.PhoneUtil;
import com.nbufe.crm.vo.Customer;
import com.nbufe.crm.vo.CustomerLoss;
import com.nbufe.crm.vo.CustomerOrder;
import com.sun.org.apache.bcel.internal.generic.DMUL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomerService extends BaseService<Customer,Integer> {

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;


    /**
     * 客户添加
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer) {
        //参数校验
        checkCustomerParams(customer.getName(),customer.getPhone(),customer.getFr());

        //设置默认值
        customer.setIsValid(1);
        customer.setState(0);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());

        String khno="KH_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        customer.setKhno(khno);

        //执行添加操作
        AssertUtil.isTrue(customerMapper.insertSelective(customer)<1,"客户添加失败！");
    }


    /**
     * 客户更新
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer) {
        //参数校验

        AssertUtil.isTrue(customer.getId()==null||customerMapper.selectByPrimaryKey(customer.getId())==null,"客户更新失败！");
        checkCustomerParams(customer.getName(),customer.getPhone(),customer.getFr());

        //设置默认值
        Customer temp=customerMapper.queryCustomerByName(customer.getName());
        AssertUtil.isTrue(null!=temp&&!(temp.getId().equals(customer.getId())),"客户已存在！");

        customer.setUpdateDate(new Date());


        //执行添加操作
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer)<1,"客户添加失败！");
    }

    /**
     * 客户添加更新参数校验
     * @param name
     * @param phone
     * @param fr
     */
    private void checkCustomerParams(String name, String phone, String fr) {
        AssertUtil.isTrue(StringUtils.isBlank(name),"客户名称不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"请指定法人！");
    }

    /**
     * 删除客户
     * @param customerId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer customerId) {
        customerMapper.deleteCustomer(customerId);
    }

    /**
     * 通过定时任务添加流失客户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerState() {
        /*1.查询待流失客户数据*/
        List<Customer> lossCustomerList=customerMapper.queryLossCustomers();

        /*2.将流失客户数据批量添加到客户流失表*/
        if(lossCustomerList!=null&&lossCustomerList.size()>0){
            //流失客户id列表
            List<Integer> lossCustomerIds=new ArrayList<>();
            //流失客户列表
            List<CustomerLoss> customerLossList=new ArrayList<>();
            //遍历查询到的流失客户
            lossCustomerList.forEach(customer->{
                CustomerLoss customerLoss=new CustomerLoss();

                customerLoss.setCreateDate(new Date());
                customerLoss.setCusManager(customer.getCusManager());
                customerLoss.setCusName(customer.getName());
                customerLoss.setCusNo(customer.getKhno());
                customerLoss.setIsValid(1);
                // 0-暂缓流失 1-确认流失
                customerLoss.setState(0);
                customer.setUpdateDate(new Date());

                //通过客户id查询最后的订单记录
                CustomerOrder customerOrder=customerOrderMapper.queryLossCustomerOrderByCustomerId(customer.getId());
                if(customerOrder!=null){
                    customerLoss.setLastOrderTime(customerOrder.getOrderDate());
                }
                customerLossList.add(customerLoss);
                lossCustomerIds.add(customer.getId());


            });
            //批量添加流失客户记录
            AssertUtil.isTrue(customerLossMapper.insertBatch(customerLossList)!=customerLossList.size(),"客户流失数据转移失败！");

            /*3.批量更新客户的流失状态*/
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(lossCustomerIds) != lossCustomerIds.size(), "客户流失数据转移失败！");


        }
    }

    /**
     * 客户贡献分析
     * @param customerQuery
     * @return
     */
    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        Map<String,Object> result = new HashMap<String,Object>();
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        List<Map<String,Object>> list=customerMapper.queryCustomerContributionByParams(customerQuery);
        PageInfo<Map<String,Object>> pageInfo =new PageInfo<Map<String,Object>>(list);
        result.put("count",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        result.put("code",0);
        result.put("msg","");
        return result;
    }

    /**
     * 查询客户构成 （折线图数据处理）
     * @return
     */
    public Map<String, Object> countCustomerMake() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerMapper.countCustomerMake();
        // 折线图X轴数据  数组
        List<String> data1 = new ArrayList<>();
        // 折线图Y轴数据  数组
        List<Integer> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            // 遍历集合
            dataList.forEach(m -> {
                // 获取"level"对应的数据，设置到X轴的集合中
                data1.add(m.get("level").toString());
                // 获取"total"对应的数据，设置到Y轴的集合中
                data2.add(Integer.parseInt(m.get("total").toString()));
            });
        }

        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1",data1);
        map.put("data2",data2);

        return map;
    }



    /**
     * 查询客户构成 （饼状图数据处理）
     * @return
     */
    public Map<String, Object> countCustomerMake02() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerMapper.countCustomerMake();
        // 饼状图数据   数组（数组中是字符串）
        List<String> data1 = new ArrayList<>();
        // 饼状图的数据  数组（数组中是对象）
        List<Map<String, Object>> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            // 遍历集合
            dataList.forEach(m -> {
                // 饼状图数据   数组（数组中是字符串）
                data1.add(m.get("level").toString());
                // 饼状图的数据  数组（数组中是对象）
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("name", m.get("level"));
                dataMap.put("value", m.get("total"));
                data2.add(dataMap);
            });
        }

        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1",data1);
        map.put("data2",data2);

        return map;
    }

    /**
     * 查询客户服务 （折线图数据处理）
     * @return
     */
    public Map<String, Object> countCustomerServe() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerMapper.countCustomerServe();
        // 折线图X轴数据  数组
        List<String> data1 = new ArrayList<>();
        // 折线图Y轴数据  数组
        List<Integer> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            // 遍历集合
            dataList.forEach(m -> {
                // 获取"level"对应的数据，设置到X轴的集合中
                data1.add(m.get("data_dic_value").toString());
                // 获取"total"对应的数据，设置到Y轴的集合中
                data2.add(Integer.parseInt(m.get("total").toString()));
            });
        }

        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1",data1);
        map.put("data2",data2);

        return map;

    }

    /**
     * 查询客户服务 （饼状图数据处理）
     * @return
     */
    public Map<String, Object> countCustomerServe02() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerMapper.countCustomerServe();
        // 饼状图数据   数组（数组中是字符串）
        List<String> data1 = new ArrayList<>();
        // 饼状图的数据  数组（数组中是对象）
        List<Map<String, Object>> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            // 遍历集合
            dataList.forEach(m -> {
                // 饼状图数据   数组（数组中是字符串）
                data1.add(m.get("data_dic_value").toString());
                // 饼状图的数据  数组（数组中是对象）
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("name", m.get("data_dic_value"));
                dataMap.put("value", m.get("total"));
                data2.add(dataMap);
            });
        }

        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1",data1);
        map.put("data2",data2);

        return map;
    }
}
