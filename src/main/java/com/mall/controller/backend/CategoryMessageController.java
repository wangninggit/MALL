package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryMessageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("addCategory.do")
    @ResponseBody
    public ServerResponse<String> addCategory (HttpSession session, String categoryName , @RequestParam(value = "parentId",defaultValue = "0") int parentId  ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
             return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员,没有权限");
        }
    }
    @RequestMapping("updateCategoryName.do")
    @ResponseBody
    public ServerResponse<String> updateCategoryName (HttpSession session, String categoryName ,Integer categoryId  ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iCategoryService.updateCategory(categoryName,categoryId);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员,没有权限");
        }
    }
    @RequestMapping("getChildParallelCategory.do")
    @ResponseBody
    //查询平级节点
    public ServerResponse getChildParallelCategory(HttpSession session,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iCategoryService.getChildParallelCategory(parentId);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员,没有权限");
        }
    }
    @RequestMapping("getAllChildByParent.do")
    @ResponseBody
    //查询父节点下的所有子节点
    public ServerResponse getAllChildByParent (HttpSession session,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iCategoryService.selectAllChildByParent(parentId);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员,没有权限");
        }
    }

}
