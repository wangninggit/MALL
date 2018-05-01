package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.ServerResponse;
import com.mall.dao.ShippingMapper;
import com.mall.pojo.Shipping;
import com.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service//("iShippingService")
public class IShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse addShipping(Integer userId, Shipping shipping){
        if(shipping == null){
            return ServerResponse.creatByErrorMessage("商品信息不能为空");
        }
        shipping.setUserId(userId);
        int rows = shippingMapper.insert(shipping);
        if(rows > 0){
            Map shippingMap = Maps.newHashMap();
            shippingMap.put("shippingId",shipping.getId());
            return ServerResponse.creatBySuccess("地址信息添加成功",shippingMap);
        }
        return ServerResponse.creatByErrorMessage("地址信息添加失败");
    }

    public ServerResponse delShipping(Integer userId, Integer shippingId){
        if(shippingId == null){
            return ServerResponse.creatByErrorMessage("商品信息不能为空");
        }
        int rows = shippingMapper.deleteByUserIdShipping(userId,shippingId);
        if(rows > 0){
            return ServerResponse.creatBySuccess("地址信息删除成功");
        }
        return ServerResponse.creatByErrorMessage("地址信息删除失败");
    }

    public ServerResponse updateShipping(Integer userId, Shipping shipping){
        if(shipping == null){
            return ServerResponse.creatByErrorMessage("商品信息不能为空");
        }
        shipping.setUserId(userId);
        int rows = shippingMapper.updateByShipping(shipping);
        if(rows > 0){
            return ServerResponse.creatBySuccess("地址信息更新成功");
        }
        return ServerResponse.creatByErrorMessage("地址信息更新失败");
    }

    public ServerResponse selectShipping(Integer userId, Integer shippingId){
        if(shippingId == null){
            return ServerResponse.creatByErrorMessage("商品信息不能为空");
        }
        Shipping shipping = shippingMapper.selectByUserIdShippingId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.creatByErrorMessage("查找不到地址信息");
        }
        return ServerResponse.creatBySuccess("地址信息查找完成",shipping);
    }

    public ServerResponse listShipping(Integer userId,Integer pageNum,Integer pageSize){

        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.listByShipping(userId);
        if(shippingList == null){
            return ServerResponse.creatByErrorMessage("查找不到地址信息");
        }
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.creatBySuccess("地址信息查找完成",pageInfo);
    }
}
