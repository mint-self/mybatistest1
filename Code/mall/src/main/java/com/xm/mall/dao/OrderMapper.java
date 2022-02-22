package com.xm.mall.dao;

import com.xm.mall.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    //查询订单的用户ID
    List<Order> selectByUid(Integer uid);

    //查询订单编号orderNo
    Order selectByOrderNo(Long orderNo);
}