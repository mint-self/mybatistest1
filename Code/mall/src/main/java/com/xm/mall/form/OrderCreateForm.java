package com.xm.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mintFM
 * @create 2022-02-01 11:14
 */
@Data
public class OrderCreateForm {
    //创建一个订单的form对象，来作为controller层的传入参数

    @NotNull
    private Integer shippingId;
}
