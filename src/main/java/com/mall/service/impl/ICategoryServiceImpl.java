package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.common.ServerResponse;
import com.mall.dao.CategoryMapper;
import com.mall.pojo.Category;
import com.mall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;
    public ServerResponse<String> addCategory(String categoryName,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.creatByErrorMessage("参数有误，无法存储");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultCount = categoryMapper.insert(category);
        if(resultCount > 0){
            return ServerResponse.creatBySuccessMessage("数据存储成功");
        }
        return ServerResponse.creatByErrorMessage("数据存储失败");
    }

    public ServerResponse<String> updateCategory(String categoryName,Integer categoryId){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.creatByErrorMessage("参数有误，无法更新");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount > 0){
            return ServerResponse.creatBySuccessMessage("数据更新成功");
        }
        return ServerResponse.creatByErrorMessage("数据更新失败");
    }

    public ServerResponse<List<Category>> getChildParallelCategory(Integer parentId){
        List<Category> categoryList = categoryMapper.getChildParallelCategory(parentId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("此节点没有子节点");
        }
        return ServerResponse.creatBySuccess(categoryList);
    }

    /**
     * 查询所有子节点
     * @param
     * @return
     */
    public ServerResponse selectAllChildByParent(Integer parentId){
        Set<Category> categorySet = Sets.newHashSet();
        findChild(categorySet,parentId);
        List<Integer> categoryList = Lists.newArrayList();
        for (Category list: categorySet) {
            categoryList.add(list.getId());
        }
        return ServerResponse.creatBySuccess(categoryList) ;
    }


    private Set<Category> findChild(Set<Category> categorySet, Integer parentId){
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        if(category != null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.getChildParallelCategory(parentId);
        for (Category item :categoryList) {
            findChild(categorySet,item.getId());
        }
        return categorySet;
    }


}
