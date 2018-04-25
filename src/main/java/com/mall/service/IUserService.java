package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse<String> getUserQuestion(String  username);

    ServerResponse<String> checkAnswer(String username ,String question, String answer);

    ServerResponse<String> forgetRestPassword(String username,String password,String forgetToken);

    ServerResponse<String> restPassword(User user , String oldPassword , String newPasswordNew);

    ServerResponse<User> updateUserMessage(User user);

    ServerResponse<User> getInfo(int id);

    ServerResponse<String> checkAdmin(User user);
}
