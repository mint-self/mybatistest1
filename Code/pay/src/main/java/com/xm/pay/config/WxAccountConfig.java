package com.xm.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mintFM
 * @create 2022-01-23 14:30
 */
@Component
@ConfigurationProperties(prefix = "wx") //在yml中配置的前缀
@Data
public class WxAccountConfig {
    //将yml中的配置加入到java代码中，因此写这个微信的Java配置类
    private String appId;

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String returnUrl;
}
