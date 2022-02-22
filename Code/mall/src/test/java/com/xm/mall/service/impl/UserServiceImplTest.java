package com.xm.mall.service.impl;

import com.xm.mall.MallApplicationTests;
import com.xm.mall.enums.ResponseEnum;
import com.xm.mall.enums.RoleEnum;
import com.xm.mall.pojo.User;
import com.xm.mall.service.IUserService;
import com.xm.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mintFM
 * @create 2022-01-24 22:18
 */
@Transactional
public class UserServiceImplTest extends MallApplicationTests {

    public static final String USENAME = "stefanie";

    public static final String PASSWORD = "123456";

    @Autowired
    private IUserService userService;

    //因为设置了拦截器，然后只用注册了用户才可以登录，所以每次都要先注册再登录，因此直接采用@Before的注解
    @Before
    public void register() {
        User user = new User(USENAME,PASSWORD,"stefanie@qq.com", RoleEnum.CUSTOMR.getCode());
        userService.register(user);
    }

    @Test
    public void login() {
        ResponseVo<User> responseVo = userService.login(USENAME,PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}