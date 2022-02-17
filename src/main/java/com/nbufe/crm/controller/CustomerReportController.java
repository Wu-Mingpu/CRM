package com.nbufe.crm.controller;

import com.nbufe.crm.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("report")
public class CustomerReportController extends BaseController {

    @RequestMapping("{type}")
    public String index(@PathVariable Integer type){
        if(type!=null){
            if(type==0){
                //客户贡献分析
                return "report/customer_contri";

            }else if(type==1){
                //客户构成分析
                return "report/customer_make";

            }else if(type==2){
                //客户服务分析
                return "report/customer_serve";

            }else if(type==3){
                //客户流失分析
                return "report/customer_loss";

            }
        }
        return "";
    }


}
