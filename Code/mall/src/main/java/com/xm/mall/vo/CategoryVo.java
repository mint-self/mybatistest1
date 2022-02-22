package com.xm.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author mintFM
 * @create 2022-01-25 16:55
 */
@Data
public class CategoryVo {
    //分类模块的java对象

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    //自己又调用自己，是递归
    //列出每个分类的细节
    private List<CategoryVo> subCategories;

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

}
