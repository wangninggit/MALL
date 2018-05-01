package com.mall.dao;

import com.mall.common.ServerResponse;
import com.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByUserIdShipping(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    int updateByShipping(Shipping record);

    Shipping selectByUserIdShippingId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    List<Shipping> listByShipping(Integer userId );
}