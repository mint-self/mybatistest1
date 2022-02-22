package com.xm.pay.controller;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.xm.pay.pojo.PayInfo;
import com.xm.pay.service.impl.PayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mintFM
 * @create 2022-01-20 22:32
 */
@Controller
//@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private PayServiceImpl payService;

    @Autowired
    private WxPayConfig wxPayConfig;

    //通过浏览器渲染，将支付的链接转换为二维码
    //写完之后，浏览器地址是这样拼接：http://localhost:8080/pay/create?orderId=123456789456321&amount=0.01
    //支付宝：http://22pcmu.natappfree.cc/pay/create?orderId=123456789456321&amount=0.01&payType=ALIPAY_PC
    //微信：http://xy2g8x.natappfree.cc/pay/create?orderId=123456789456321&amount=0.01&payType=WXPAY_NATIVE
    //每次更新的：http://sf7e2v.natappfree.cc
    @GetMapping("/create")
    @ResponseBody
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum) {
       PayResponse response = payService.create(orderId,amount, bestPayTypeEnum);

       //支付方式不同，渲染出来的结果就不同，微信使用的是codeURL，最后转换为二维码；支付宝使用的是body，返回的是一个表单form
        //让微信支付二维码可以自动刷新
        Map<String,String> map = new HashMap<>();
        //map.put("codeUrl","weixin://wxpay/bizpayurl?pr=n1DPEwpzz");

        //判断是哪个支付平台的
        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE) {
            //微信平台的不管哪种支付方式都返回以下的
            map.put("codeUrl",response.getCodeUrl());
            //拿到订单号，渲染到主页上
            map.put("orderId",orderId);
            //微信支付完成后的返回地址
            map.put("returnUrl",wxPayConfig.getReturnUrl());
            //返回到微信渲染页面
            return new ModelAndView("createForWxNative",map);
        } else if (bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC) {
            //支付宝平台的不过哪种支付方式都返回以下的
            map.put("body",response.getBody());
            //返回到支付宝渲染页面
            return new ModelAndView("createForAlipayPc",map);
        }

        //不是以上支付平台的，暂不支持，抛出异常
        throw new RuntimeException("暂不支持的支付类型");
    }

    //微信异步通知, 因为接受的参数不止一个，所以用@RequestBody
    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
       return payService.asyncNotify(notifyData);
        //log.info("notifyData = {}", notifyData);
    }

    //实现支付之后的页面跳转，通过查询订单号来查询支付状态，如果支付状态为支付成功则实现跳转
    @GetMapping("/queryByOrderId") //获取订单号get
    @ResponseBody //因为方法最后返回的是对象，如果不是用@RestController注解，则需要在方法上加上@ResponseBody注解
    public PayInfo queryByOrderId(@RequestParam String orderId) {
        //返回查询到的订单号
        log.info("查询支付记录：");
        return payService.queryByOrderId(orderId);
    }
}
