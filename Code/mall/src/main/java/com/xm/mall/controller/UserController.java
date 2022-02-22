package com.xm.mall.controller;

import com.xm.mall.consts.MallConst;
import com.xm.mall.form.UserLoginForm;
import com.xm.mall.form.UserRegisterForm;
import com.xm.mall.pojo.User;
import com.xm.mall.service.IUserService;
import com.xm.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author mintFM
 * @create 2022-01-24 22:38
 */
@RestController
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;

    /*
    注册
     */
    //是@RequestMapping(method = RequestMethod.POST)的快捷方式  ;@Vaild用于验证注解是否符合要求,在变量中添加验证信息的要求，当不符合要求时就会在方法中返回message 的错误提示信息。
    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userForm) {
        User user = new User();
        //对表单中的属性进行逐一的赋值，就可以用下面的方法（“转换后的类”，"要转换的类”）
        BeanUtils.copyProperties(userForm,user);
        return userService.register(user);
    }

    /*
    登录
     */
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session) {
        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(),userLoginForm.getPassword());
        //创建一个session
        session.setAttribute(MallConst.CURRENT_USER,userResponseVo.getData());
        log.info("/login sessionId = {}",session.getId());
        return userResponseVo;
    }

    //设置session：客户端向服务器端发送请求，服务器端在控制器controller中就会创建一个session，将用户的信息保存在session中，同时将sessionId放入到cookie中
    //而客户端在收到服务器端的响应后，自己的cookie就会保存有sessionId。而且客户端给服务器端发送请求时，自身的cookie中就携带有sessionId，服务器端就可以根据这个sessionId来查找响应的session，从而获得保存在session中的用户信息
    //session是有存活时间的，可以自己对session的时间进行设置。session失效的情况：1.sessionId变了；2.session或者sessionId被删除；3.设置session的存活时间
    //session保存在内存中，这样项目一重启就会清除数据，所以高级的做法，是将session保存到redis中，然后通过token（本质是cookie的Id）来建立session和redis的关联，token+redis，也就是分布式session。
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session) {
        log.info("/user sessionId = {}",session.getId());
        //获取session(sessionId)
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success(user);
    }

    /*
    退出登录
     */
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session) {
        log.info("/user/logout sessionId = {}",session.getId());
        //退出就直接删保存着的session内容即可
        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }
}
