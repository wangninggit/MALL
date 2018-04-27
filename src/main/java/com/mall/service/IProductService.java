package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Product;

public interface IProductService {

    ServerResponse saveAndUpdate(Product product);

    ServerResponse productPower (Integer productId, Integer Status);

    ServerResponse getDetail(Integer productId);

    ServerResponse getListProduct(Integer pageNum,Integer pageSize);

    ServerResponse searchProduct(String productName,Integer productId,Integer pageNum,Integer pageSize);

    ServerResponse Detail(Integer productId);

    ServerResponse listByKeywordAndCategoryId (String keyword,Integer categoryId,int pageNum,int pageSize ,String orderBy);
}
