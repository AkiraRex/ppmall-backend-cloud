package com.yc.service.rpc;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.yc.common.response.ServerResponse;
import com.yc.service.rpc.impl.IUserRpcServiceImpl;

@FeignClient(name = "ppmall-oauth2server", fallbackFactory = IUserRpcServiceImpl.class)
public interface IUserRpcService {
	@GetMapping("/principal")
	ServerResponse<Map<String, Object>> getUserInfo();
}

