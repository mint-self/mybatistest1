package com.xm.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xm.mall.MallApplicationTests;
import com.xm.mall.enums.ResponseEnum;
import com.xm.mall.form.CartAddForm;
import com.xm.mall.vo.CartVo;
import com.xm.mall.vo.OrderVo;
import com.xm.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mintFM
 * @create 2022-01-31 16:05
 */
@Slf4j
//@Transactional 加了这个注解，每次test完成后，事务就会回滚，就不会在数据库中留下数据，避免在数据库中留下脏数据。
public class IOrderServiceTest extends MallApplicationTests {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICartService cartService;

    private Integer uid = 1;

    private Integer shippingId = 4;

    private Integer productId = 26;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void before() {
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid,form);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void createTest() {
        ResponseVo<OrderVo> responseVo = create();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    //创建订单的方法
    private ResponseVo<OrderVo> create() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid,shippingId);
        log.info("result = {}",gson.toJson(responseVo));
        return responseVo;
    }


    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = orderService.list(uid,1,4);
        //将对象转化为JSon字符
        log.info("result = {}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail() {
        //创建订单对象
        ResponseVo<OrderVo> vo = create();
        //订单详情
        ResponseVo<OrderVo> responseVo = orderService.detail(uid, vo.getData().getOrderNo());
        log.info("result = {}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void cancel() {
        //创建订单对象
        ResponseVo<OrderVo> vo = create();
        //取消订单
        ResponseVo responseVo = orderService.cancel(uid,vo.getData().getOrderNo());
        log.info("result = {}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }


}