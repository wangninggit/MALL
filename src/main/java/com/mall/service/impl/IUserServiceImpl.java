package com.mall.service.impl;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class IUserServiceImpl implements IUserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
       int resultCode =  userMapper.checkUsername(username);
       if(resultCode == 0){
            return ServerResponse.creatByErrorMessage("用户名不存在");
       }
        String md5Password= MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
       if(user == null ){
            return ServerResponse.creatByErrorMessage("密码错误");
       }
        user.setPassword(StringUtils.EMPTY);//不明白为什么要把密码置空
        return ServerResponse.creatBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user){
        ServerResponse<String> valid = this.checkValid(user.getUsername(),Const.USERNAME);
        if(! valid.isSuccess()){
            return valid;
        }
        valid = this.checkValid(user.getEmail(),Const.EMAIL);
        if(! valid.isSuccess()){
            return valid;
        }
        user.setRole(Const.role.ROLE_CUSTOMER);//设置为普通用户
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int result = userMapper.insert(user);//返回值为数据插入的行数
        if(result == 0){
            return ServerResponse.creatByErrorMessage("注册失败");
        }
        return ServerResponse.creatBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(str)){
            if(Const.EMAIL.equals(type)){
                int result = userMapper.checkEmail(str);
                if(result > 0){
                    return ServerResponse.creatByErrorMessage("邮箱已经被使用");
                }
            }
            if(Const.USERNAME.equals(type) ){
                int result = userMapper.checkUsername(str);
                if(result > 0){
                    return ServerResponse.creatByErrorMessage("用户已存在");
                }
            }
            return ServerResponse.creatBySuccessMessage("校验成功");
        }else{
            return ServerResponse.creatByErrorMessage("参数错误");
        }
    }

    @Override
    public ServerResponse<String> getUserQuestion(String username) {
        ServerResponse<String> valid = this.checkValid(username,Const.USERNAME);
        if(valid.isSuccess()){
                return ServerResponse.creatByErrorMessage("用户不存在");
        }
        String question =userMapper.getUserQuestion(username);
        if(StringUtils.isNotBlank(question)){//todo 此处的不用工具类是否可以
            return ServerResponse.creatBySuccess(question);
        }
        return ServerResponse.creatByErrorMessage("问题为空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int result = userMapper.checkAnswer(username,question,answer);
        if(result > 0){
                String forgetToken = UUID.randomUUID().toString();
                TokenCache.setKey(Const.TOKEN+username,forgetToken);
                return ServerResponse.creatBySuccessMessage(forgetToken);
        }
        return ServerResponse.creatByErrorMessage("输入的答案和问题不一致");
    }

    @Override
    public ServerResponse<String> forgetRestPassword(String username, String password, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.creatByErrorMessage("参数有误");
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.creatByErrorMessage("密码是空的");
        }
        int result = userMapper.checkUsername(username);
        if(result == 0){
            return ServerResponse.creatByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(Const.TOKEN+username);
        if(StringUtils.equals(token,forgetToken)){
            String md5Password = MD5Util.MD5EncodeUtf8(password);
            int resultRestP = userMapper.updatePasswordByUsername(username,md5Password);
            if(resultRestP > 0){
                return ServerResponse.creatBySuccessMessage("修改密码成功");
            }
            return ServerResponse.creatByErrorMessage("修改失败");
        }else{
            return ServerResponse.creatByErrorMessage("信息过期，已经失效");
        }
    }

    @Override
    public ServerResponse<String> restPassword(User user, String oldPassword, String newPasswordNew) {
        if(oldPassword.equals(newPasswordNew)){
            return ServerResponse.creatByErrorMessage("新旧密码相同");
        }
        int CheckR = userMapper.checkPassword(user.getId(),MD5Util.MD5EncodeUtf8(oldPassword));
        if(CheckR == 0){
            return ServerResponse.creatByErrorMessage("旧密码不对");
        }
        int resultRestP = userMapper.updatePasswordByUsername(user.getUsername(),MD5Util.MD5EncodeUtf8(newPasswordNew));
        if(resultRestP > 0){
            return ServerResponse.creatBySuccessMessage("修改密码成功");
        }
        return ServerResponse.creatByErrorMessage("修改失败");
    }


    @Override
    public ServerResponse<User> updateUserMessage(User user) {
        //对email进行校验
        int resultCount =userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.creatByErrorMessage("邮箱已经被用，请重新添加");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int resultBPKS = userMapper.updateByPrimaryKeySelective(updateUser);
        if(resultBPKS > 0 ){
            return ServerResponse.creatBySuccess("更新成功",updateUser);
        }
        return ServerResponse.creatByErrorMessage("更新失败");
    }

    @Override
    public  ServerResponse<User> getInfo(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user == null){
            return ServerResponse.creatByErrorMessage("未找到用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.creatBySuccess(user);
    }


}
