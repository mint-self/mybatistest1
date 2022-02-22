package com.xm.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author mintFM
 * @create 2022-01-24 22:53
 */
@Data
public class UserLoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
