package com.xm.mall.service;

import com.xm.mall.form.CartAddForm;
import com.xm.mall.form.CartUpdateForm;
import com.xm.mall.pojo.Cart;
import com.xm.mall.vo.CartVo;
import com.xm.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author mintFM
 * @create 2022-01-27 17:32
 */
public interface ICartService {

    /**
     * 添加商品
     * @param uid  商品的ID
     * @param form  添加商品的表单
     * @return
     */
    ResponseVo<CartVo> add(Integer uid, CartAddForm form);

    /**
     * 显示购物车列表
     * @param uid
     * @return
     */
    ResponseVo<CartVo> list(Integer uid);

    /**
     * 更新购物车信息
     * @param uid
     * @param productId
     * @param form
     * @return
     */
    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    /**
     * 删除购物车
     * @param uid
     * @param productId
     * @return
     */
    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    /**
     * 购物车商品全选
     * @param uid
     * @return
     */
    ResponseVo<CartVo> selectAll(Integer uid);

    /**
     * 不全选
     * @param uid
     * @return
     */
    ResponseVo<CartVo> unSelectAll(Integer uid);

    /**
     * 购物车中商品数量的总和
     * @param uid
     * @return
     */
    ResponseVo<Integer> sum(Integer uid);

    /**
     * 遍历整个购物车，因为要对是否全选和数量和进行判断，所以需要遍历购物车
     * @param uid
     * @return
     */
    List<Cart> listForCart(Integer uid);
}
