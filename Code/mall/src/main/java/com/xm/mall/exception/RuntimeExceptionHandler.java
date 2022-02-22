package com.xm.mall.exception;

import com.xm.mall.enums.ResponseEnum;
import com.xm.mall.vo.ResponseVo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

import static com.xm.mall.enums.ResponseEnum.ERROR;

/**
 * @author mintFM
 * @create 2022-01-25 11:08
 */
@ControllerAdvice
public class RuntimeExceptionHandler {
    //统一异常处理：因为后端发送给前端的数据格式不一样，所以可以让前端显示弹窗确认，统一对数据格式进行处理
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ERROR,e.getMessage());
    }

    //对用户未登录的异常进行统一的处理
    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle() {
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }

    //表单在进行验证时，出现一些参数的错误，通过异常将它们统一拦住
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        //判断是否为空
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage());
    }
}
