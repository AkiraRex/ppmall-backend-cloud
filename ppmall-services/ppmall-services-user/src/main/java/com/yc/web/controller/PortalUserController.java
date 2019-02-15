package com.yc.web.controller;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.User;
import com.yc.service.IUserService;

@RestController()
@RequestMapping("/portal/user")
/**
 *
 */
public class PortalUserController {

	@Autowired
	private IUserService iUserService;

	@PostMapping("/register")
	public ServerResponse<Null> register(User user) {
		ServerResponse<Null> response = iUserService.register(user);
		return response;
	}

	@GetMapping("/getUserInfo")
	public ServerResponse<User> getUserInfo() {
		return iUserService.getUserInfo();
	}

	@GetMapping("/forgetGetQuestion")
	public ServerResponse<String> getPassQuestion(String username) {
		return iUserService.getPassQuestion(username);
	}

	@PostMapping("/forgetCheckAnswer")
	public ServerResponse<String> checkAnswer(String username, String answer) {
		return iUserService.checkAnswer(username, answer);
	}

	@PostMapping("/forgetResetPassword")
	public ServerResponse<String> forgetResetPassword(User user, String forgetToken) {
		return iUserService.resetPasswordByQues(user, forgetToken);
	}

	@PostMapping("/updatePassword")
	public ServerResponse<Null> updatePassword(String passwordOld, String passwordNew) {
		User currentUser = iUserService.getUserInfo().getData();
		return iUserService.resetPasswordByPass(currentUser, passwordOld, passwordNew);
	}
	
	@GetMapping("/checkValid")
	public ServerResponse<Null> checkValid(String str) {
		return iUserService.checkValid(str);
	}
	
	@GetMapping("/getInformation")
	public ServerResponse<User> getInformation() {
		return this.getUserInfo();
	}
	
	@PostMapping("/updateInformation")
	public ServerResponse<Null> updateInformation(User user) {
		User currentUser = this.getUserInfo().getData();
		
		user.setUsername(currentUser.getUsername());
		user.setId(currentUser.getId());

		return iUserService.updateInformation(user);
	}

}
