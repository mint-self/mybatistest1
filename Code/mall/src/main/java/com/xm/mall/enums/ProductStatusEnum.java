package com.xm.mall.enums;

import lombok.Getter;

/**
 * @author mintFM
 * @create 2022-01-26 23:22
 */
@Getter
public enum ProductStatusEnum {
    //商品的状态

    //商品在售
    ON_SALE(1),
    //下架
    OFF_SALE(2),
    //删除
    Delete(3),
    ;
    Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }
}
