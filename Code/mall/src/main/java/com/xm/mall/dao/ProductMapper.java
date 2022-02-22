package com.xm.mall.dao;

import com.xm.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    //根据类别ID查询出商品列表;得加@Param，不然set就类似一个基本数据类型
    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    //根据商品ID查询出商品详细信息
    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);
}