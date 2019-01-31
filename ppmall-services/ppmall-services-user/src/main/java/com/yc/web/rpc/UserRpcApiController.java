package com.yc.web.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.pojo.User;
import com.yc.response.ServerResponse;
import com.yc.service.IUserService;

@RestController
@RequestMapping("/rpc/user")
public class UserRpcApiController {
	
	@Autowired
	private IUserService iUserService;
	
	@GetMapping("/findByUsername/{username}")
	public ServerResponse<User> findByUsername(@PathVariable("username") String username){
		return iUserService.findByUsername(username);
	}
}
