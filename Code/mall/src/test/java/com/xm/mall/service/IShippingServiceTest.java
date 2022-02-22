package com.xm.mall.service;

import com.xm.mall.MallApplicationTests;
import com.xm.mall.enums.ResponseEnum;
import com.xm.mall.form.ShippingForm;
import com.xm.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author mintFM
 * @create 2022-01-29 10:17
 */
@Slf4j
public class IShippingServiceTest extends MallApplicationTests {

    @Autowired
    private IShippingService shippingService;

    //以下三个变量单独定义出来，就不用每个测试方法都再定义一次
    private Integer uid = 1;

    private ShippingForm form;

    private Integer shippingId = 10;

    //先给出一些参数
    @Before
    public void before() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("夏木");
        form.setReceiverAddress("地球");
        form.setReceiverCity("广东");
        form.setReceiverMobile("18813312795");
        form.setReceiverPhone("0663456");
        form.setReceiverProvince("广东");
        form.setReceiverDistrict("广州");
        form.setReceiverZip("000000");
        this.form = form;

        //让每个方法执行前先添加上地址
        //add();
    }

    @Test
    public void add() {
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid,form);
        log.info("result = {}",responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void delete() {
        //Integer shippingId = 6;
        ResponseVo responseVo = shippingService.delete(uid,shippingId);
        log.info("result = {}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void update() {
        //Integer shippingId = 8;
        //Integer uid = 2;
        form.setReceiverCity("上海");
        ResponseVo responseVo = shippingService.update(uid,shippingId,form);
        log.info("result = {}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo responseVo = shippingService.list(uid,1, 10);
        log.info("result = {}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}