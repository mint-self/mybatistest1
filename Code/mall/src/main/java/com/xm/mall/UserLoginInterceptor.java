package com.xm.mall;

import com.xm.mall.consts.MallConst;
import com.xm.mall.exception.UserLoginException;
import com.xm.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mintFM
 * @create 2022-01-25 11:24
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    //定义一个用户登录的拦截器类，返回false表示拦截，返回true表示继续，不拦截

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        //获取普通用户
        User user = (User) request.getSession().getAttribute(MallConst.CURRENT_USER);
        if (user == null) {
            //如果没有这个用户，也就是用户没有登录，则进行拦截
            log.info("user = null");
            throw new UserLoginException();
            //return false;
        }
        //否则有登录则放行
        return true;
    }
}
