package com.mall.dao;

import com.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart>  selectCartByUserId(Integer userId);

    int selectAllChecked(Integer userId);

    int deleteByUserIdProductId(@Param("userId")Integer userId,@Param("productId")List<String> StringProductId);

    int selectAll(@Param("userId")Integer userId ,@Param("productId") Integer productId,@Param("checked")Integer checked);

    int getProductCount(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}