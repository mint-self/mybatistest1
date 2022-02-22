package com.xm.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author mintFM
 * @create 2022-01-26 22:09
 */
@Data
public class ProductVo {
    //前端要显示的内容，封装为一个Vo对象，这样可以与数据库的pojo对象分隔开，因为数据库有可能会增加一些字段，而增加的这些在前端页面不一定会显示，所以pojo和Vo分开为两个对象来写，且这里只写前端需要显示的内容就可以了
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;
}
