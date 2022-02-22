package com.xm.mall.service;

import com.github.pagehelper.PageInfo;
import com.xm.mall.form.ShippingForm;
import com.xm.mall.vo.ResponseVo;

import java.util.Map;

/**
 * @author mintFM
 * @create 2022-01-29 8:09
 */
public interface IShippingService {

    /**
     * 添加收货地址
     * @param uid
     * @param form
     * @return
     */
    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form);

    /**
     * 删除收货地址
     * @param uid
     * @param shippingId
     * @return
     */
    ResponseVo delete(Integer uid, Integer shippingId);

    /**
     * 更新地址
     * @param uid
     * @param shippingId
     * @param form
     * @return
     */
    ResponseVo update(Integer uid, Integer shippingId, ShippingForm form);

    /**
     * 获取收获地址列表
     * @param uid
     * @param pageNum
     * @param pgaeSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pgaeSize);

}
