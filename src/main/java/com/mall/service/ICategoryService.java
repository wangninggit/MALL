package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> updateCategory(String categoryName,Integer categoryId);

    ServerResponse<List<Category>> getChildParallelCategory(Integer parentId);

    ServerResponse selectAllChildByParent(Integer parentId);
}
