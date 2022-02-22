package com.xm.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xm.mall.dao.ProductMapper;
import com.xm.mall.pojo.Product;
import com.xm.mall.service.ICategoryService;
import com.xm.mall.service.IProductService;
import com.xm.mall.vo.ProductDetailVo;
import com.xm.mall.vo.ProductVo;
import com.xm.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xm.mall.enums.ProductStatusEnum.Delete;
import static com.xm.mall.enums.ProductStatusEnum.OFF_SALE;
import static com.xm.mall.enums.ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE;

/**
 * @author mintFM
 * @create 2022-01-26 22:50
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    //查询商品列表
    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null) {
            //如果ID不为空，就进行查询，这样查出来的是子目录
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            //所以还要将父目录给加进去
            categoryIdSet.add(categoryId);
        }

        //商品列表分页
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        //进行属性赋值
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e,productVo);
                    return productVo;
                })
                .collect(Collectors.toList());
        //productList是数据库中查出来的信息，
        //这行代码可以查出一些商品的详细信息
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        return ResponseVo.success(pageInfo);
    }

    //查询商品详细信息
    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);

        //这里不采用反向if了，因为商品的状态有可能还有不确定的，if判断只对确定性的条件进行判断
        if (product.getStatus().equals(OFF_SALE.getCode()) || product.getStatus().equals(Delete.getCode())) {
            //如果商品的状态是下架或者是删除，则返回错误的提示信息msg
            return ResponseVo.error(PRODUCT_OFF_SALE_OR_DELETE);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product,productDetailVo);

        //敏感数据处理（不一定需要）
        //比如说库存是比较敏感的，不想让用户看到真实的，所以进行处理 假设对于库存超过100的就不显示真实的
        productDetailVo.setStock(product.getStock() > 100 ? 100 : product.getStock());

        return ResponseVo.success(productDetailVo);
    }
}
