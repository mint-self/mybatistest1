package com.xm.mall.service.impl;

import com.xm.mall.dao.UserMapper;
import com.xm.mall.enums.RoleEnum;
import com.xm.mall.pojo.User;
import com.xm.mall.service.IUserService;
import com.xm.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.xm.mall.enums.ResponseEnum.*;

/**
 * @author mintFM
 * @create 2022-01-24 16:37
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     * @param user 用户
     * @return
     */
    @Override
    public ResponseVo<User> register(User user) {
        //用户注册时，得保证用户名不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
            //return ResponseVo.error(ResponseEnum.USERNAME_EXIST);可以直接引入枚举的包，就不用在前面写上枚举了
            return ResponseVo.error(USERNAME_EXIST);
        }

        //邮箱email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            return ResponseVo.error(EMAIL_EXIST);
        }

        //否则则可以设置用户：设为普通用户
        user.setRole(RoleEnum.CUSTOMR.getCode());
        //MD5加密（Spring自带）
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //将数据写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0) {
            return ResponseVo.error(ERROR);
        }

        //返回登录成功
        return ResponseVo.success();
    }



    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            //如果用户不存在，则返回用户名或密码错误
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }

        if (!user.getPassword().equalsIgnoreCase(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            //如果密码错误,则返回用户名或密码错误
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }

        //否则到了这里则表示登录成功
        //不将用户的密码显示的露出来，所以设为空
        user.setPassword("");
        //返回登录成功的对象
        return ResponseVo.success(user);
    }

    //以下方法只是用于自己模拟错误用的
    private void error() {
        throw new RuntimeException("意外错误");
    }
}
