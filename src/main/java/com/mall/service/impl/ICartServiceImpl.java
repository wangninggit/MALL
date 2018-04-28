package com.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.dao.CartMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Cart;
import com.mall.pojo.Product;
import com.mall.service.ICartService;
import com.mall.util.BigDecimalUtil;
import com.mall.util.PropertiesUtil;
import com.mall.vo.CartProductVo;
import com.mall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class ICartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;


    public ServerResponse listCart(Integer userId){
        CartVo cartVo =getCartVo(userId);
        return ServerResponse.creatBySuccess(cartVo);
    }

    public ServerResponse addCart(Integer userId ,Integer productId ,Integer count){
        if(productId == null || count == null){
            return ServerResponse.creatByErrorMessage("缺少参数");
        }
         Cart cart = cartMapper.selectByUserIdProductId(userId,productId);
        if(cart == null){//购物车为空证明没有，要新创建一个购物车
            Cart itemcart = new Cart();
            itemcart.setProductId(productId);
            itemcart.setUserId(userId);
            itemcart.setQuantity(count);
            itemcart.setChecked(Const.checkedProduct.CHECKED);
            cartMapper.insert(itemcart);
        }else{//证明购物车已经存在更新产品数量
            cart.setQuantity(cart.getQuantity()+count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return this.listCart(userId);
    }
    public ServerResponse updateCart(Integer userId ,Integer productId ,Integer count){
        if(productId == null || count == null){
            return ServerResponse.creatByErrorMessage("缺少参数");
        }
        Cart cart = cartMapper.selectByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.listCart(userId);
    }

    public ServerResponse deleteCart(Integer userId ,String productIds){
        List<String> StringProductId = Splitter.on(",").splitToList(productIds);//这里用的是list的集合
        if(CollectionUtils.isEmpty(StringProductId)){
            return ServerResponse.creatByErrorMessage("缺少参数");
        }
        cartMapper.deleteByUserIdProductId(userId,StringProductId);
        return this.listCart(userId);
    }


    private CartVo getCartVo(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList =  cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart itemCartList : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(itemCartList.getId());
                cartProductVo.setUserId(itemCartList.getUserId());
                cartProductVo.setProductId(itemCartList.getProductId());
                //进行库存与购物车的产品数量判断
                Product product = productMapper.selectByPrimaryKey(itemCartList.getProductId());
                if(product != null){
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductStock(product.getStock());
                    //根据库存判断
                    if(product.getStock() >= itemCartList.getQuantity()){
                        cartProductVo.setQuantity(itemCartList.getQuantity());
                        cartProductVo.setLimitQuantity(Const.checkedProduct.LIMIT_QUANTITY_SUCCESS);
                    }else{
                        cartProductVo.setLimitQuantity(Const.checkedProduct.LIMIT_QUANTITY_FAIL);
                        cartProductVo.setQuantity(product.getStock());
                        //购物车中的数量超出库存需要重新修改购物车中的数量
                        Cart tempCart = new Cart();
                        tempCart.setId(itemCartList.getId());
                        tempCart.setQuantity(product.getStock());
                        cartMapper.updateByPrimaryKeySelective(tempCart);
                    }
                    //计算商品总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(itemCartList.getChecked());
                }
                if(cartProductVo.getProductChecked() == Const.checkedProduct.CHECKED ){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }

        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(this.getAllChecked(userId));
        return cartVo;

    }
    private boolean getAllChecked(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectAllChecked(userId) == 0;
    }

    public ServerResponse selectAll(Integer userId ,Integer productId ,Integer checked ){
        cartMapper.selectAll(userId, productId, checked);
        return this.listCart(userId);
    }

    public ServerResponse getProductCount(Integer userId ){
        if(userId == null){
            return ServerResponse.creatBySuccess(0);
        }
        return ServerResponse.creatBySuccess(cartMapper.getProductCount(userId));
    }

}
