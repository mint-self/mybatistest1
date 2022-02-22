package com.xm.mall.controller;

import com.github.pagehelper.PageInfo;
import com.xm.mall.service.IProductService;
import com.xm.mall.vo.ProductDetailVo;
import com.xm.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mintFM
 * @create 2022-01-26 23:40
 */
@RestController
public class ProductController {
    @Autowired
    private IProductService productService;

    //查询商品列表
    @GetMapping("/products")
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return productService.list(categoryId,pageNum,pageSize);
    }

    //查询商品的详细信息
    @GetMapping("/products/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable Integer productId) {
        return productService.detail(productId);
    }

}
