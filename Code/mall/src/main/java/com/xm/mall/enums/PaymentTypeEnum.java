package com.xm.mall.enums;

import lombok.Getter;

/**
 * @author mintFM
 * @create 2022-01-31 8:10
 */
@Getter
public enum  PaymentTypeEnum {
    //支付的方式：在线支付和线下支付，因为我们线上网站所以是在线支付
    PAY_ONLINE(1),
    ;
    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}
