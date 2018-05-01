package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;

public interface IShippingService {

    ServerResponse addShipping(Integer userId, Shipping shipping);

    ServerResponse delShipping(Integer userId, Integer shippingId);

    ServerResponse updateShipping(Integer userId, Shipping shipping);

    ServerResponse selectShipping(Integer userId, Integer shippingId);

    ServerResponse listShipping(Integer userId,Integer pageNum,Integer pageSize);
}
