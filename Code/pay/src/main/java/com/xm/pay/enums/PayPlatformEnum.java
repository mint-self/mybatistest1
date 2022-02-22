package com.xm.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * @author mintFM
 * @create 2022-01-22 16:04
 */
@Getter
public enum  PayPlatformEnum {
    //建一个支付方式的枚举，这样方便公用和使用,枚举一般使用大写
    //1 代表支付宝； 2 代表微信
    ALIPAY(1),
    WX(2),
    ;
    Integer code;

    //构造方法
    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    //将需要使用到枚举的方法放在这里，就不会在service里显示代码太多太乱
    //获取支付的方式
    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
        //可以使用if语句来判断是支付包还是微信
//        if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.ALIPAY)) {
//            //如果是支付宝返回支付宝的支付方式
//            return PayPlatformEnum.ALIPAY;
//        } else if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.WX)) {
//            //微信方式
//            return PayPlatformEnum.WX;
//        }

        //用上面的方式代码太不简洁，所以可以直接使用for循环来在支付方式中遍历，如果是我们枚举中列举的支付方式，直接选择相应的方式，否则抛出异常
        for (PayPlatformEnum payPlatformEnum: PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())) {
                //返回对应的支付方式
                return payPlatformEnum;
            }

        }

        //否则不在支付平台的抛出异常
        throw new RuntimeException("错误的支付平台：" + bestPayTypeEnum.name());
    }
}
