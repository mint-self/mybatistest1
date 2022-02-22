//package com.xm.mall.dao;
//
//import com.xm.mall.MallApplicationTests;
//import com.xm.mall.pojo.Category;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author mintFM
// * @create 2022-01-16 16:05
// * 测试类最好是自己在项目中单独建立一个，而不要要项目中自动生成的测试基类，方便使用
// */
////直接让测试类继承MallApplicationTests基类，这样就不用在每个测试类上写上测试注解
////@RunWith(SpringRunner.class)
////@SpringBootTest
//public class CategoryMapperTest extends MallApplicationTests {
//
//    @Autowired
//    private CategoryMapper categoryMapper;
//
//    @Test
//    public void findById() {
//        Category category = categoryMapper.findById(100001);
//        System.out.println("查找结果是：" + category.toString());
//    }
//
//    @Test
//    public void queryById() {
//        Category category = categoryMapper.queryById(100001);
//        System.out.println("查找结果是：" + category.toString());
//    }
//}