package com.xm.mall.service;

import com.xm.mall.pojo.User;
import com.xm.mall.vo.ResponseVo;

/**
 * @author mintFM
 * @create 2022-01-24 16:24
 */
public interface IUserService {
    /**
     * 注册功能
     * @param user 用户
     * @return
     */
    ResponseVo<User> register(User user);

    /**
     * 登录功能：登录时只需要用户名和密码，所以就不传整个user对象
     * @param username
     * @param password
     * @return
     */
    ResponseVo<User> login(String username,String password);

}
