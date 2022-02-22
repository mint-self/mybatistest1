package com.xm.mall.listener;

import com.google.gson.Gson;
import com.xm.mall.pojo.PayInfo;
import com.xm.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mintFM
 * @create 2022-02-01 15:28
 */
@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {
    //编写一个listener监听器来接收mq消息

    @Autowired
    private IOrderService orderService;

    @RabbitHandler
    public void process(String msg) {
        log.info("【接收到消息】 => {}",msg);

        //接收Mq消息，并将接收到的JSon字符串转化为对象
        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if (payInfo.getPlatformStatus().equals("SUCCESS")) {
            //支付成功则修改订单里的状态
            orderService.paid(payInfo.getOrderNo());
        }

    }

}
