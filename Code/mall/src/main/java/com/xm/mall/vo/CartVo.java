package com.xm.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mintFM
 * @create 2022-01-27 17:07
 */
@Data
public class CartVo {
    //前端页面中，购物车需要显示的内容

    //购物车中的商品
    private List<CartProductVo> cartProductVoList;

    //是否全选
    private Boolean selectAll;

    //购物车中商品总价格
    private BigDecimal cartTotalPrice;

    //购物车中的商品总数量
    private Integer cartTotalQuantity;
}
