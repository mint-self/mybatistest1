package com.xm.mall.pojo;

import lombok.Data;

/**
 * @author mintFM
 * @create 2022-01-27 17:39
 */
@Data
public class Cart {
    //这些是数据库层面的，但是这些可以放到redis中，其他的会实时变化，所以就不要放到redis中，而是让其他的直接在数据库中读取

    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
