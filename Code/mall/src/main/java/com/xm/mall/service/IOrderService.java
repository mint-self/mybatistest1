package com.xm.mall.service;

import com.github.pagehelper.PageInfo;
import com.xm.mall.vo.OrderVo;
import com.xm.mall.vo.ResponseVo;

/**
 * @author mintFM
 * @create 2022-01-31 6:58
 */
public interface IOrderService {

    /**
     * 创建订单
     * @param uid  用户的ID
     * @param shippingId  收货地址的ID
     * @return
     */
    ResponseVo<OrderVo> create(Integer uid, Integer shippingId);

    /**
     * 订单列表页：需要进行分页操作
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    /**
     * 订单详情页
     * @param uid 用户的ID
     * @param orderNo 订单的编号
     * @return
     */
    ResponseVo<OrderVo> detail(Integer uid, Long orderNo);

    /**
     * 取消订单
     * @param uid
     * @param orderNo 订单编号
     * @return
     */
    ResponseVo cancel(Integer uid, Long orderNo);

    /**
     * 修改订单的支付状态：本项目只给出支付的订单状态
     * @param orderNo 订单的编号
     */
    void paid(Long orderNo);

}
