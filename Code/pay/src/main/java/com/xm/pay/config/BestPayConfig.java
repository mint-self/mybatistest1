package com.xm.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author mintFM
 * @create 2022-01-21 21:26
 */
@Component
public class BestPayConfig {
    //微信支付配置把它抽出来，可以共用，加@Bean表示项目启动时来执行

    @Autowired
    private WxAccountConfig wxAccountConfig;

    @Autowired
    private AlipayAccountConfig alipayAccountConfig;

    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig) {
//        //设置微信支付的配置
//        WxPayConfig wxPayConfig = new WxPayConfig();
//        //公众账号APPID
//        wxPayConfig.setAppId("wx3e6b9f1c5a7ff034");//自己在做的时候，写多了个App
//        //商家id
//        wxPayConfig.setMchId("1614433647");
//        //商家密钥
//        wxPayConfig.setMchKey("Aa111111111122222222223333333333");
//        //接收平台异步通知的地址
//        //wxPayConfig.setNotifyUrl("http://127.0.0.1");
//        wxPayConfig.setNotifyUrl("http://6vppqt.natappfree.cc/pay/notify");

        //设置支付宝的支付配置
//        AliPayConfig aliPayConfig = new AliPayConfig();
//        //支付id
//        aliPayConfig.setAppId("2021003115605474"); //2021003115605474
//        //私钥
//        aliPayConfig.setPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLB/0LyITaV5Wkw9hkx6HHhM3dKWoY00+qj5Q0IGvFOxn947tojIsRJG4sGNhjxAvDab4StUP4B2Nymdf05TmZiA4yuf0av+lmbIGI+SP6rBMBE2DAGna/VaUI6d/ICcJqn1oAsY3tIZKse10K7mrvyWSVPY8Ugb1yEEudmztlj60wgdbMRLRaQmdXR4njK3joGV1bLQ1Q7Z0lLoeSizrqFzk7BZhfLRGaCwMNG26RZ3dNG61TAB1VsZdhubImOauXE2D7lIJKODGyMtZr6GXNiBYQqBw+bPSVrFXNky+3lcyal/adfVeHlXWpwKf8e2erEICqdPRh3FsBX48eEpfPAgMBAAECggEAC8NRuN4MPG44oEwsfGJIkDu0hazBleCgS/x3lLGQLOQotFn+YKdL41ld/LYPy2/vUooM1h6kanlbHKenKGwjcy67y9qUvHkD98gvlRUCLaLMkxJhV5/w41L99c8/BOLz3aOP3lxrIaMBUXbwRBLeJThg2HixknmV1Ub2apI01cRrx34/oQBM7vZHtVNcDw33mF64rchyJUG1yleKsYT/CWRV3W6+Q7iHTiIhgV2626/d6gXjb6c8bNPTAVbvgxr2X3jBnT8P4XfqgL84c2n94QcXpd8vFX4uHFZbJFDFFj8NQC+g2tsMzoakVpAKNJnLbcV94JTiPG462r1tuaumeQKBgQDyiS62x28DY/FVu7z/8Vupd1HZtN+bzJ8j9fg2fYePYx6Wpq1yhJmALbttGbdVrj8TLWq8oMzX1xNrYkmH8b5XJMGQwSqyZbfrTXFK6piJcpa5gI7cIMEkTBPrETeUnkN56LaiGX5pmo6ptsJrAzw3hb072KcC/QKVDLmOTXZZ+wKBgQDWTWHh1tug9y3DFoOARn3ZZW5hKcQtgiili1rsnqSRaKx/q7LZtBe2ddr4fNkPBAK6s1ue8c0OEdmU5RSMgMVk3kC/+rjBF2sKAao0Jrwew6aLoCgEzoAVbV1kBAhypRPKJmSY6nuupUJ5WAhd94+wAaohsBwY0H3bvCFJOBPFPQKBgQDDT4W221Oiv1ERYWYKFitihRv83ZNUu6tMIkAIYX5REdkIKKF0nyOd/TX53cOt0+wnXWOfEz7icejJiRT9zMcjFg5qq6PeldtOOccKNXEw/n4yVDy3OAlRWZKQsgS4QdrEyYqJb05FbJmGQg6+bpSHgcOp9gZIeccikGS9G9Q7EQKBgDaHkjSJG3Gl87sEa4mkZjl/GcsXeZeq2vTktMoow+9MFziVU/kZXj9LVeCMxB6SAq/HG7UiCe9ek2LTt5rY2WGFPCyde42Gi/IL4mdKw+uAFUzJ/+TZNrY9Edp1PEEPyC1T/5z5bWdPHc8Km3Ztm95JTiUsTrnxs1TKVh3vavhxAoGBAPAFUCA62+gTzdL1mvQs/N2E0l/ntGYKL2OyFImOIrMUcLEpm0sSNLtc8whzNdFA+rOGCxhtsIIMyQ7KK2NWEku57ZTGcUnU/iL6IImcjqXHzSsbG7Ab/mejVrU16CqarXq+em2D1MvUiplknC2lexeBU/rbm6Smma4CCo+9/wgJ");
//        //公钥
//        aliPayConfig.setAliPayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhEqcd8WMIpk6b6ru4NZ6eHZh22WKd1UaK6efULU/dTG2KOmKAypm+ZxaN1v36G/DlKZCYZxuoQAViY9CEeUX/hPAaM3pGW1Aji5NCIaRqQ+vZ+GXwI13+afepTkdxRCTW4cC3AOgL3HA1QgX3CCZ+WYP4oZFRICK2P3ssOXlvqZscA6IiQTKHSNIYM7p56jsv0JSekKR2A9l3tRb22EsouKFPZu2LjB9PqHhFe6gB9VemSldQ1aMSqG7ut5EvIt7ZCghoJixnN4xMAH+4Bo2boJl/OZQK0pWdAQ0fzlyOedxWpUWj3zyvWoeDBp0eIBrIFB83ad4iXbhl8zKDLlnyQIDAQAB");
//        //请求的url
//        aliPayConfig.setNotifyUrl("http://6vppqt.natappfree.cc/pay/notify");
//        //异步通知返回的URL
//        aliPayConfig.setReturnUrl("http://127.0.0.1");

        AliPayConfig aliPayConfig = new AliPayConfig();
        //支付id
        aliPayConfig.setAppId(alipayAccountConfig.getAppId()); //2021003115605474
        //私钥
        aliPayConfig.setPrivateKey(alipayAccountConfig.getPrivateKey());
        //公钥
        aliPayConfig.setAliPayPublicKey(alipayAccountConfig.getPublicKey());
        //请求的url
        aliPayConfig.setNotifyUrl(alipayAccountConfig.getNotifyUrl());
        //异步通知返回的URL
        aliPayConfig.setReturnUrl(alipayAccountConfig.getReturnUrl());

        //发起支付
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        //微信
        bestPayService.setWxPayConfig(wxPayConfig);
        //支付宝
        bestPayService.setAliPayConfig(aliPayConfig);
        return bestPayService;
    }

    //把微信的配置给提取出来
    @Bean
    public WxPayConfig wxPayConfig() {
        //设置微信支付的配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        //公众账号APPID
        wxPayConfig.setAppId(wxAccountConfig.getAppId());//自己在做的时候，写多了个App
        //商家id
        wxPayConfig.setMchId(wxAccountConfig.getMchId());
        //商家密钥
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());
        //接收平台异步通知的地址
        //wxPayConfig.setNotifyUrl("http://127.0.0.1");
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        //支付完成后返回的界面地址
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());
        return wxPayConfig;

    }

}
