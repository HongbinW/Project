package com.mmall.task;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mmall.service.IOrderService;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    //每分钟执行一次，关闭两个小时未付款的订单
    @Scheduled(cron = "0 */1 * * * ?")  //每min执行一次
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt("2");
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }

}
