package com.yc.service;

import com.yc.pojo.User;
import com.yc.response.ServerResponse;

public interface IUserService {

	ServerResponse<User> findByUsername(String username);
}
