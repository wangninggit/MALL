package com.mall.service;


import com.mall.common.ServerResponse;

public interface ICartService {

    ServerResponse addCart(Integer userId , Integer productId , Integer count);

    ServerResponse updateCart(Integer userId ,Integer productId ,Integer count);

    ServerResponse deleteCart(Integer userId ,String productIds);

    ServerResponse listCart(Integer userId);

    ServerResponse selectAll(Integer userId ,Integer productId ,Integer checked );

    ServerResponse getProductCount(Integer userId );
}
