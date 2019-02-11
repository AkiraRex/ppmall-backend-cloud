package com.yc.service.rpc.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.yc.common.response.ServerResponse;
import com.yc.service.rpc.IUserRpcService;

import feign.hystrix.FallbackFactory;

@Service("iUserRpcService")
public class IUserRpcServiceImpl implements IUserRpcService, FallbackFactory<IUserRpcService> {

	@Override
	public ServerResponse<Map<String, Object>> getUserInfo() {
		return ServerResponse.createErrorMessage("获取用户信息失败");
	}
	
	
	@Override
	public IUserRpcService create(Throwable cause) {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		return this;
	}

	

}
