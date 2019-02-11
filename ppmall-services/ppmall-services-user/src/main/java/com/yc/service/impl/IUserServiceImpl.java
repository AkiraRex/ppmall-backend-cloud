package com.yc.service.impl;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yc.common.constant.Const;
import com.yc.common.response.ResponseCode;
import com.yc.common.response.ServerResponse;
import com.yc.common.utils.RedisUtil;
import com.yc.common.utils.StringUtil;
import com.yc.common.utils.UUIDUtil;
import com.yc.dao.RoleUserMapper;
import com.yc.dao.UserMapper;
import com.yc.pojo.RoleUser;
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

	@Autowired
	private RoleUserMapper roleUserMapper;

	@Autowired
	private RedisUtil redisUtil;

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

	@Transactional
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

		int insertCount = userMapper.insert(user);

		user.setRole(Const.Role.ROLE_CUSTOMER);
		user.setPassword(BCryptUtils.encode(user.getPassword()));

		RoleUser roleUser = new RoleUser();
		roleUser.setRoleId(Const.Role.ROLE_CUSTOMER);
		roleUser.setUserId(user.getId());

		roleUserMapper.insert(roleUser);

		if (insertCount > 0) {
			return ServerResponse.createSuccessMessage("注册成功");
		} else {
			return ServerResponse.createErrorMessage("注册失败");
		}
	}

	@Override
	public ServerResponse<String> getPassQuestion(String username) {
		String question = userMapper.selectPassQuestionByUsername(username);
		if (question != null) {
			return ServerResponse.createSuccess(question);
		}
		return ServerResponse.createErrorMessage("获取密码保护问题失败");
	}

	@Override
	public ServerResponse<String> checkAnswer(String username, String answer) {
		String userAnswer = userMapper.selectAnswerByUsername(username);

		if (answer.equals(userAnswer)) {
			String forgetToken = UUIDUtil.getUUID();
			redisUtil.set(username + "_" + Const.FORGET_TOKEN, forgetToken, Const.ExpiredType.ONE_MINUTE * 15);
			return ServerResponse.createSuccess("答案正确", forgetToken);
		}
		return ServerResponse.createErrorMessage("答案错误");
	}

	@Override
	public ServerResponse<String> resetPasswordByQues(User user, String forgetToken) {

		String forgetTokenRe = (String) redisUtil.get(user.getUsername() + "_" + Const.FORGET_TOKEN);

		if (StringUtil.isBlank(forgetTokenRe) || !forgetToken.equals(forgetTokenRe)) {
			return ServerResponse.createErrorStatus(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		int updateCount = userMapper.updatePasswordByUsername(user.getUsername(),
				BCryptUtils.encode(user.getPassword()));
		

		if (updateCount > 0) {
			redisUtil.del(user.getUsername() + "_" + Const.FORGET_TOKEN);// 重置成功后删除重置token
			return ServerResponse.createSuccess("密码重置成功");
		}

		return ServerResponse.createErrorMessage("问题答案错误");
	}

	@Override
	public ServerResponse<Null> resetPasswordByPass(User currentUser, String password, String passwordNew) {
		String userPassword = userMapper.selectPasswordByUsername(currentUser.getUsername());

		if (!BCryptUtils.checkPassword(password, userPassword)) {
			return ServerResponse.createErrorMessage("原密码错误");
		}

		currentUser.setPassword(BCryptUtils.encode(passwordNew));
		int updateCount = userMapper.updateByPrimaryKeySelective(currentUser);

		if (updateCount > 0) {
			return ServerResponse.createSuccessMessage("修改成功");
		}
		return ServerResponse.createErrorMessage("修改失败");
	}

	@Override
	public ServerResponse<Null> checkValid(String str) {
		int userCount = userMapper.selectCountByUsername(str);
		int phoneCount = userMapper.selectCountByPhone(str);
		int mailCount = userMapper.selectCountByEmail(str);

		if (userCount > 0)
			return ServerResponse.createErrorMessage("用户名已存在");
		if (phoneCount > 0)
			return ServerResponse.createErrorMessage("手机号已注册");
		if (mailCount > 0)
			return ServerResponse.createErrorMessage("邮箱已注册");

		return ServerResponse.createSuccess();
	}

	@Override
	public ServerResponse<Null> updateInformation(User user) {
		int updateCount = userMapper.updateUserByUsername(user);

		if (updateCount > 0) {
			return ServerResponse.createSuccessMessage("修改成功");
		}
		return ServerResponse.createErrorMessage("修改失败");
	}

}
