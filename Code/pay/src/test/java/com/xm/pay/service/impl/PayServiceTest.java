package com.xm.pay.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.xm.pay.PayApplicationTests;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @author mintFM
 * @create 2022-01-20 17:03
 */
public class PayServiceTest extends PayApplicationTests {
    @Autowired
    private PayServiceImpl payServiceImpl;

    //辅助发送mq的工具
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void create() {
        //后来多加了多种支付方式，再加上判断是哪种类型的支付
        payServiceImpl.create("123456987456322", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }

    @Test
    public void sendMQMsg() {
        //先在test中测试下发送消息是否成功，不直接在项目中进行发送，避免设计出错难以调试
        //发送Mq消息
        amqpTemplate.convertAndSend("payNotify", "hello");
    }
}