package com.xm.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xm.mall.enums.ResponseEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * @author mintFM
 * @create 2022-01-24 16:27
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
//因为响应到界面的模块对象有很多，像用户对象、订单、购物车等，所以就使用泛型
public class ResponseVo<T> {
    //返回给前端的是对象，一般把它们单独划分出来，放到vo包下
    //登录状态
    private Integer status;
    //错误信息
    private String msg;
    //返回呈现出来在界面上的对象
    private T data;

    //两个不同用途的构造方法
    private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    //前端显示出data对象时，有不同的信息，为了不让代码是字符型的编码（不规范），所以就用常量型的类来代替
    public static <T> ResponseVo<T> successByMsg(String msg) {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(), data);
    }

    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum) {
        return new ResponseVo<>(responseEnum.getCode(),responseEnum.getDesc());
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, String msg) {
        return new ResponseVo<>(responseEnum.getCode(),msg);
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult) {
        return new ResponseVo<>(responseEnum.getCode(), Objects.requireNonNull(bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage()));
    }

}
