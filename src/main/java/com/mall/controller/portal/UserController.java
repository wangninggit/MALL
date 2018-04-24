package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username , String password , HttpSession session){
        ServerResponse<User> response =  iUserService.login(username, password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.creatBySuccess();
    }
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }
    @RequestMapping(value = "checkValid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }
    @RequestMapping(value = "getUserInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
                return ServerResponse.creatBySuccess(user);
        }
        return ServerResponse.creatByErrorMessage("用户未登录，无法获取信息");
    }
    @RequestMapping(value = "get_User_Question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getUserQuestion(String  username){
        return iUserService.getUserQuestion(username);
    }

    @RequestMapping(value = "check_Answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswer(String username ,String question, String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forget_Rest_Password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String password,String forgetToken){
        return iUserService.forgetRestPassword(username,password,forgetToken);
    }
    @RequestMapping(value = "rest_Password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> restPassword(HttpSession session ,String oldPassword ,String newPasswordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorMessage("用户未登录");
        }
        return iUserService.restPassword(user,oldPassword,newPasswordNew);
    }

    @RequestMapping(value = "update_UserMessage.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserMessage(HttpSession session,User user){
        User currentU = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentU == null){
            return ServerResponse.creatByErrorMessage("用户未登录");
        }
        user.setId(currentU.getId());
        user.setUsername(currentU.getUsername());
        ServerResponse<User> response = iUserService.updateUserMessage(user);
        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @RequestMapping(value = "getInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录重新强制登录，status=10");
        }
        int id = user.getId();
        return iUserService.getInfo(id);
    }


}
