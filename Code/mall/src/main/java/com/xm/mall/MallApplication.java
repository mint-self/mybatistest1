package com.xm.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//在启动类上写上扫描Mapper包的注解，就不用在每个Mapper上都写上Mapper注解了
@MapperScan(basePackages = "com.xm.mall.dao")
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }

}
