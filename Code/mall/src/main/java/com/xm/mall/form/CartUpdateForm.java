package com.xm.mall.form;

import lombok.Data;

/**
 * @author mintFM
 * @create 2022-01-27 17:25
 */
@Data
public class CartUpdateForm {
    //更新购物车中商品的表单
    private Integer quantity;

    private Boolean selected;
}
