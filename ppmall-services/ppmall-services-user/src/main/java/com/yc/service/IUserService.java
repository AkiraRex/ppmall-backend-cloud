package com.yc.service;

import org.apache.commons.lang.ObjectUtils.Null;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.User;

public interface IUserService {

	ServerResponse<User> findByUsername(String username);

	ServerResponse<User> getUserInfo();

	ServerResponse<Null> register(User user);

	ServerResponse<String> getPassQuestion(String username);

	ServerResponse<String> checkAnswer(String username, String answer);

	ServerResponse<String> resetPasswordByQues(User user, String forgetToken);

	ServerResponse<Null> resetPasswordByPass(User currentUser, String password, String passwordNew);

	ServerResponse<Null> checkValid(String str);

    ServerResponse<Null> updateInformation(User user);
}
