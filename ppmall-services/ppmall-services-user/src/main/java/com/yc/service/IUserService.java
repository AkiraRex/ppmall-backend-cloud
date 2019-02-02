package com.yc.service;

import org.apache.commons.lang.ObjectUtils.Null;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.User;

public interface IUserService {

	ServerResponse<User> findByUsername(String username);
	
	ServerResponse<User> getUserInfo();
	
	ServerResponse<Null> register(User user);
}
