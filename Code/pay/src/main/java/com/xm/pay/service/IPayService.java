package com.xm.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.xm.pay.pojo.PayInfo;

import java.math.BigDecimal;

/**
 * @author mintFM
 * @create 2022-01-20 16:30
 */
public interface IPayService {

    /**
     * 创建或者说发起支付
     * @param orderId 支付的订单号
     * @param amount 支付的金额
     */
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    /**
     * 接受到异步通知的处理
     * @param notifyData
     */
    String asyncNotify(String notifyData);

    /**
     * 查询支付记录的订单号，通过订单号来查看支付的状态
     * @param orderId
     * @return
     */
    PayInfo queryByOrderId(String orderId);

}
