package com.xm.mall.service.impl;

import com.xm.mall.dao.CategoryMapper;
import com.xm.mall.pojo.Category;
import com.xm.mall.service.ICategoryService;
import com.xm.mall.vo.CategoryVo;
import com.xm.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xm.mall.consts.MallConst.ROOT_PARENT_ID;

/**
 * @author mintFM
 * @create 2022-01-25 17:02
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    /**
     * 注：
     * 耗时：http(请求微信api) > 磁盘 > 内存dengx
     * mysql(内网+磁盘)
     * @return
     */
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 查询全部分类
     * @return
     */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<Category> categories = categoryMapper.selectAll();
        //List<CategoryVo> categoryVoList = new ArrayList<>();
        //查询出父目录：也就是parent_id = 0的
//        for (Category category : categories) {
//            if (category.getParentId().equals(ROOT_PARENT_ID)) {
//               //是父目录
//                CategoryVo categoryVo = new CategoryVo();
//                //BeanUtils.copyProperties：对属性进行赋值
//                BeanUtils.copyProperties(category,categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }

        //可以采用lambda+stream的方式来循环遍历
        //查询父目录
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());

        /*
        上面的公式拆开如下：
        Stream<Category> categoryStream = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID));
        Stream<CategoryVo> categoryVoStream = categoryStream.map(this::category2CategoryVo);
        Comparator<CategoryVo> reversed = Comparator.comparing(CategoryVo::getSortOrder).reversed();
        Stream<CategoryVo> sorted = categoryVoStream.sorted(reversed);
        List<CategoryVo> collect = (List<CategoryVo>) sorted.collect(Collectors.toList());
         */
        //查询子目录mei
        findSubCategory(categoryVoList,categories);
        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        //先将列表中的目录都查找出来
        //这一条是查询数据库的代码，如果不重写一个findSubCategoryId重载的方法，那么每次调用findSubCategoryId都会执行list这条查数据库的方法，这样会速度变慢
        List<Category> categories = categoryMapper.selectAll();
        //然后再获取它们的ID（根据 这个方法，里面也有将父ID set进去
        findSubCategoryId(id,resultSet,categories);
    }


    //方法重载
    //商品列表模块的：查找子目录的ID
    private void findSubCategoryId(Integer id,Set<Integer> resultSet,List<Category> categories) {
        for (Category category:categories) {
            if (category.getParentId().equals(id)) {
                //如果ID是父ID，就放进set集合中
                resultSet.add(category.getId());
                //然后继续往下查出子目录的ID
                //递归
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }


    //查询子目录的方法
    private void findSubCategory(List<CategoryVo> categoryVoList,List<Category> categories) {
        for(CategoryVo categoryVo: categoryVoList) {
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (Category category: categories) {
                //如果查得到内容，则设置为子目录subCategory，并对它们进行属性赋值，然后继续循环往下查，直到查不到为止
                if (categoryVo.getId().equals(category.getParentId())) {
                    //ID和父ID一样则表示查到了
                    //赋值
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                //倒序排序
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);
                //一轮结束查找好后，继续调用自身的方法往下查，递归
                findSubCategory(subCategoryVoList,categories);
            }
        }

    }

    //对属性进行赋值
    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
