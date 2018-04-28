package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse listCart(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.listCart(user.getId()));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse addCart(HttpSession session, Integer productId , Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.addCart(user.getId() ,productId ,count));
    }
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse updateCart(HttpSession session, Integer productId , Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.updateCart(user.getId() ,productId ,count));
    }

    @RequestMapping("delete_update.do")
    @ResponseBody
    public ServerResponse deleteCart(HttpSession session, String productIds){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.deleteCart(user.getId() ,productIds ));
    }

    //全选
    @RequestMapping("select_All.do")
    @ResponseBody
    public ServerResponse selectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.selectAll(user.getId() ,null,Const.checkedProduct.CHECKED ));
    }
    //全反选
    @RequestMapping("select_Un_All.do")
    @ResponseBody
    public ServerResponse selectUnAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.selectAll(user.getId() ,null,Const.checkedProduct.UN_CHECKED ));
    }

    //单选
    @RequestMapping("select_One.do")
    @ResponseBody
    public ServerResponse selectOne(HttpSession session,Integer productId ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.selectAll(user.getId(),productId ,Const.checkedProduct.CHECKED ));
    }
    //单反选
    @RequestMapping("select_Un_One.do")
    @ResponseBody
    public ServerResponse selectUnOne(HttpSession session,Integer productId ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.creatBySuccess(iCartService.selectAll(user.getId(),productId ,Const.checkedProduct.UN_CHECKED ));
    }
    //获取产品数量
    @RequestMapping("get_product_count.do")
    @ResponseBody
    public ServerResponse getProductCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatBySuccess(0);
        }
        return ServerResponse.creatBySuccess(iCartService.getProductCount(user.getId()));
    }

}
