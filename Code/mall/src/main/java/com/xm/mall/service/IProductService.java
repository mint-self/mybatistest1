package com.xm.mall.service;

import com.github.pagehelper.PageInfo;
import com.xm.mall.vo.ProductDetailVo;
import com.xm.mall.vo.ResponseVo;

/**
 * @author mintFM
 * @create 2022-01-26 22:21
 */
public interface IProductService {
    //商品列表接口

    //TODO

    /**
     * 查询呈现出商品列表的方法
     * @param categoryId 根据商品ID来查询
     * @param pageNum  分页的页数
     * @param pageSize  每页显示的数据数
     * @return  返回查询到的商品列表
     */
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    /**
     * 显示出商品列表中商品的详细信息
     * @param productId 商品的ID
     * @return 返回商品的详细信息
     */
    ResponseVo<ProductDetailVo> detail(Integer productId);

}
