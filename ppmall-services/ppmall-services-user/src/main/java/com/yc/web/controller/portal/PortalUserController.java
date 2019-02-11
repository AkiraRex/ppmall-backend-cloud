package com.yc.web.controller.portal;

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yc.common.response.ResponseCode;
import com.yc.common.response.ServerResponse;
import com.yc.pojo.User;
import com.yc.service.IUserService;
import com.yc.service.rpc.IUserRpcService;

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
	//
	// @GetMapping("/forgetGetQuestion")
	// public ServerResponse<String> getPassQuestion(String username) {
	// return iUserService.getPassQuestion(username);
	// }
	//
	// @PostMapping("/forgetCheckAnswer")
	// public ServerResponse<String> checkAnswer(User user, HttpSession session)
	// {
	//
	// ServerResponse response = iUserService.checkAnswer(user);
	// if (response.isSuccess()) {
	// session.setAttribute(Const.FORGET_TOKEN, response.getData());
	// }
	//
	// return response;
	// }
	//
	//
	// @PostMapping("/forgetResetPassword")
	// public ServerResponse<String> forgetPassword(User user, String
	// forgetToken, HttpSession session) {
	// String forgetTokenS =
	// session.getAttribute(Const.FORGET_TOKEN).toString();
	//
	// if (forgetToken.equals(forgetTokenS))
	// return iUserService.resetPasswordByQues(user);
	//
	// return
	// ServerResponse.createErrorStatus(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
	// ResponseCode.ILLEGAL_ARGUMENT.getDesc());
	// }
	//
	// @PostMapping("/updatePassword")
	// public ServerResponse<String> forgetPassword(String passwordOld, String
	// passwordNew, HttpSession session) {
	// User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
	//
	// if (currentUser == null) {
	// return ServerResponse.createErrorStatus(ResponseCode.NOT_LOGIN.getCode(),
	// ResponseCode.NOT_LOGIN.getDesc());
	// }
	// ServerResponse response = iUserService.resetPasswordByPass(currentUser,
	// passwordOld, passwordNew);
	// if (response.isSuccess()) {
	// currentUser.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
	// }
	// return response;
	// }
	//
	// @GetMapping("/checkValid")
	// public ServerResponse<String> checkValid(String str) {
	//
	// return iUserService.checkValid(str);
	// }
	//
	// @GetMapping("/getInformation")
	// public ServerResponse<User> getInformation(HttpSession session) {
	//
	// return getUserInfo(session);
	// }
	//
	// @PostMapping("/updateInformation")
	// public ServerResponse<String> updateInformation(User user, HttpSession
	// session) {
	// User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
	// user.setUsername(currentUser.getUsername());
	// user.setId(currentUser.getId());
	// session.setAttribute(Const.CURRENT_USER, user);
	//
	// return iUserService.updateInformation(user);
	// }

}
