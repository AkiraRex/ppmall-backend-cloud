package com.yc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yc.dao.UserMapper;
import com.yc.pojo.User;
import com.yc.response.ServerResponse;
import com.yc.service.IUserService;

@Service("iUserService")
public class IUserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		User user = userMapper.selectByUsername(username);
		return ServerResponse.createSuccess(user);
	}

}
