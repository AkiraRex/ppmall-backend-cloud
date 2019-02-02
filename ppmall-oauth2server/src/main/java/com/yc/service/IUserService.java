package com.yc.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.User;
import com.yc.service.impl.IUserServiceImpl;

@FeignClient(name = "ppmall-service-user", fallbackFactory = IUserServiceImpl.class)
public interface IUserService {
	@GetMapping("/rpc/user/findByUsername/{username}")
	ServerResponse<User> findByUsername(@PathVariable("username") String username);
}
