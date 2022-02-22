package com.xm.mall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallApplicationTests {

//    @Autowired
//    private CategoryMapper categoryMapper;
//
//    @Test
//    void contextLoads() {
//        Category category = categoryMapper.findById(100001);
//        System.out.println(category.toString());
//    }

    @Test
    public void load() {
        //得在主测试类中写一个初始化方法，不然启动测试会报错
    }

}
