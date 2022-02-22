package com.xm.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mintFM
 * @create 2022-01-27 17:22
 */
@Data
public class CartAddForm {
    //添加购物车商品的表单

    @NotNull
    private Integer productId;

    //是否选中：是
    private Boolean selected = true;
}
