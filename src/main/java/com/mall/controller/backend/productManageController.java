package com.mall.controller.backend;

import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.IFileService;
import com.mall.service.IProductService;
import com.mall.service.IUserService;
import com.mall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Response;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class productManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping("saveProduct.do")
    @ResponseBody
    public ServerResponse saveProduct (HttpSession session , Product product){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.saveAndUpdate(product);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员，没有权限");
        }
    }
    @RequestMapping("productPower.do")//产品上下架
    @ResponseBody
    public ServerResponse productPower (HttpSession session , Integer productId, Integer Status){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.productPower(productId,Status);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员，没有权限");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail (HttpSession session , Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.getDetail(productId);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员，没有权限");
        }
    }
    @RequestMapping("list.do")//分页查询所有商品的list，
    @ResponseBody
    public ServerResponse listProduct (HttpSession session ,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum ,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.getListProduct(pageNum,pageSize);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员，没有权限");
        }
    }
    @RequestMapping("search.do")//分页查询所有商品的list，
    @ResponseBody
    public ServerResponse searchProduct (HttpSession session ,String productName ,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum ,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员，没有权限");
        }
    }

    @RequestMapping("upload.do")//mvc上传
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file" ,required = false) MultipartFile file , HttpServletRequest request){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"管理员未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String filename = iFileService.uploadFile(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+filename;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",filename);
            fileMap.put("url",url);
            return ServerResponse.creatBySuccess(fileMap);
        }else{
            return ServerResponse.creatByErrorMessage("不是管理员，没有权限");
        }
    }

    @RequestMapping("richtextload.do")//富文本上传
    @ResponseBody
    public Map richtextload(HttpSession session, @RequestParam(value = "upload_file" ,required = false)MultipartFile file , HttpServletRequest request, HttpServletResponse response){
        Map fileMap = Maps.newHashMap();
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            fileMap.put("success",false);
            fileMap.put("msg","管理员没登录，心里没数吗");
            return fileMap;
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String filename = iFileService.uploadFile(file,path);
            if(StringUtils.isBlank(filename)){
                fileMap.put("success",false);
                fileMap.put("msg","上传失败");
                return fileMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+filename;
            fileMap.put("success",true);
            fileMap.put("msg","上传成功");
            fileMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return fileMap;
        }else{
            fileMap.put("success",false);
            fileMap.put("msg","管理员没权限，心里没数吗");
            return fileMap;
        }
    }

}
