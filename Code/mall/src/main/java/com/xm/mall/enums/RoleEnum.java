package com.xm.mall.enums;

import lombok.Getter;

/**
 * @author mintFM
 * @create 2022-01-24 22:03
 */
@Getter
public enum RoleEnum {
    //用户角色：0代表管理员；1代表普通用户

    ADMIN(0),
    CUSTOMR(1),
    ;
    Integer code;

    RoleEnum(Integer code) {
        this.code = code;
    }
}
