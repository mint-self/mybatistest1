package com.xm.mall.vo;

import com.xm.mall.pojo.Shipping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author mintFM
 * @create 2022-01-31 6:53
 */
@Data
public class OrderVo {
    //返回给前端的订单对象vo

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    //整个vo对象是订单的详细信息，而这个属性是订单里的商品的详细信息
    private List<OrderItemVo> orderItemVoList;

    private Integer shippingId;

    private Shipping shippingVo;
}
