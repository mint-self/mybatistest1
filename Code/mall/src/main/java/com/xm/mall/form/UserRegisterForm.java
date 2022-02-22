package com.xm.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author mintFM
 * @create 2022-01-24 22:41
 */
@Data
public class UserRegisterForm {
    //将表单中的对象单独划分出来
    //@NotBlank 用于 String 判断空格
    //@NotEmpty 用于集合
    //@NotNull 单纯设置字段不能为空
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
