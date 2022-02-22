package com.xm.mall.service;

import com.xm.mall.vo.CategoryVo;
import com.xm.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
 * @author mintFM
 * @create 2022-01-25 16:58
 */
public interface ICategoryService {
    //分类模块的接口

    //查出全部的列表目录
    ResponseVo<List<CategoryVo>> selectAll();

    //商品列表模块用的，查出子类ID，子子类ID等，最后的结果用set集合来存放，因为id不重复，所以用set集合
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
