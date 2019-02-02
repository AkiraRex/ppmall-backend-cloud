package com.yc.service.impl;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yc.common.constant.Const;
import com.yc.common.response.ServerResponse;
import com.yc.dao.UserMapper;
import com.yc.pojo.User;
import com.yc.service.IUserService;
import com.yc.service.rpc.IUserRpcService;
import com.yc.utils.BCryptUtils;

@Service("iUserService")
public class IUserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private IUserRpcService iUserRpcService;

	@Override
	public ServerResponse<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		User user = userMapper.selectByUsername(username);
		return ServerResponse.createSuccess(user);
	}

	@Override
	public ServerResponse<User> getUserInfo() {
		// TODO Auto-generated method stub
		String username = (String) iUserRpcService.getUserInfo().getData().get("name");
		User user = userMapper.selectByUsername(username);
		return ServerResponse.createSuccess(user);
	}

	@Override
	public ServerResponse<Null> register(User user) {
		// TODO Auto-generated method stub
		int userCount = userMapper.selectCountByUsername(user.getUsername());
		if (userCount > 0)
			return ServerResponse.createErrorMessage("用户名已存在");

		int phoneCount = userMapper.selectCountByPhone(user.getPhone());
		if (phoneCount > 0)
			return ServerResponse.createErrorMessage("该手机号码已注册");

		int emailCount = userMapper.selectCountByEmail(user.getEmail());
		if (emailCount > 0)
			return ServerResponse.createErrorMessage("该邮箱已注册");
		

		user.setRole(Const.Role.ROLE_CUSTOMER);
		user.setPassword(BCryptUtils.encode(user.getPassword()));
		int insertCount = userMapper.insert(user);

		if (insertCount > 0) {
			return ServerResponse.createSuccessMessage("注册成功");
		} else {
			return ServerResponse.createErrorMessage("注册失败");
		}
	}

}
