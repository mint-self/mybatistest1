package com.xm.pay.service.impl;

import com.google.gson.Gson;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;

import com.xm.pay.dao.PayInfoMapper;
import com.xm.pay.enums.PayPlatformEnum;
import com.xm.pay.pojo.PayInfo;
import com.xm.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author mintFM
 * @create 2022-01-20 16:32
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {

    //定义Mq消息的队列名称
    private final static String QUEUE_PAY_NOTIFY = "payNotify";

    @Autowired
    private BestPayService bestPayService;

    //这个错误只是idea的提示问题，并不是错误
    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 创建或发起支付
     * @param orderId 支付的订单号
     * @param amount 支付的金额
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
//        //设置微信支付的配置
//        WxPayConfig wxPayConfig = new WxPayConfig();
//        //公众账号APPID
//        wxPayConfig.setAppId("wx3e6b9f1c5a7ff034");//自己在做的时候，写多了个App
//        //商家id
//        wxPayConfig.setMchId("1614433647");
//        //商家密钥
//        wxPayConfig.setMchKey("Aa111111111122222222223333333333");
//        //接收平台异步通知的地址
//        //wxPayConfig.setNotifyUrl("http://127.0.0.1");
//        //异步通知地址
//        wxPayConfig.setNotifyUrl("http://3f63tg.natappfree.cc/pay/notify");
//        //发起支付
//        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
//        bestPayService.setWxPayConfig(wxPayConfig);

        //对支付的方式进行判断：如果直接正面写会使得if条件里过多嵌套，所以采用反向写，如果不是微信和支付宝支付，则抛出异常
//        if (bestPayTypeEnum != BestPayTypeEnum.WXPAY_NATIVE
//                && bestPayTypeEnum != BestPayTypeEnum.ALIPAY_PC) {
//            //则抛出异常
//            throw new RuntimeException("暂不支持的支付类型");
//        }


        //写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        payInfoMapper.insertSelective(payInfo);

        //设置配置
        PayRequest request = new PayRequest();
        //订单名称
        request.setOrderName("4559066-最好的支付sdk");
        //订单id
        request.setOrderId(orderId);
        //订单支付金额
        request.setOrderAmount(amount.doubleValue());
        //订单支付方式
        request.setPayTypeEnum(bestPayTypeEnum);

        PayResponse response = bestPayService.pay(request);
        log.info("发起支付 response = {}", response);
        return response;

    }

    /**
     * 接受处理异步通知
     * @param notifyData
     */
    @Override
    public String asyncNotify(String notifyData) {
        //1.签名验证
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("异步通知：payResponse = {}",payResponse);

        //2.金额校验（从数据库中查询订单）
        //实际中：比较严重（正常情况下是不会发生的）发出告警：钉钉、短信
        //转换为long类型
       PayInfo payInfo =  payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        //对于不同的支付情况进行判断
        if (payInfo == null) {
            //没有支付
            throw new RuntimeException("通过orderNo查询到的为null");
        }

        //对已经支付的进行判断，为了代码简介，采用反向来判断
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {
            //对double类型进行比较大小
            //这个支付的判断：-1为小于数据库的，0为和数据中的相等，1为大于数据库中的数据，只有0才是正确的，所以采用反向的判断，将不为0的抛出警告
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                //如果金额和数据中的不同，则抛异常
                throw new RuntimeException("异步通知中的金额和数据库里的不一致，orderNo=" + payResponse.getOrderId());
            }
        }

        //3.修改订单状态
        payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
        //显示交易的流水号
        payInfo.setPlatformNumber(payResponse.getOutTradeNo());
        //更改时间自己设置或者将xml中的SQL语句删除，让更改时间交给数据库自己来更改
       // payInfo.setUpdateTime(null);
        //修改
        payInfoMapper.updateByPrimaryKeySelective(payInfo);

        //TODO pay项目发送mq消息，mall项目接收mq消息
        /*
        发送mq消息
         */
        //发送消息，转换为JSon字符传送过去，而不直接传送对象，因为这样才可以显示得更加清晰
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY, new Gson().toJson(payInfo));

        //4.告诉微信或者支付宝不要再通知了，只通知一次就可以了，采用以下的固定格式
        if(payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            //微信
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            //支付宝
            return "success";
        }
        //不是以上两种则抛出异常
        throw new RuntimeException("异步通知中暂不支持的支付平台");

    }

    //查询支付订单号
    @Override
    public PayInfo queryByOrderId(String orderId) {
        return payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
    }
}
