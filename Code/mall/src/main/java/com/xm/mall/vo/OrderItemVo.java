package com.xm.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mintFM
 * @create 2022-01-31 6:56
 */
@Data
public class OrderItemVo {
    //因为订单返回的商品信息和之前的商品的信息不完全一样，所以为了方便，重新再建一个对象

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;
}
