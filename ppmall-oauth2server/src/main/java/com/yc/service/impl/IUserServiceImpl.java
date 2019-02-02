package com.yc.service.impl;
import org.springframework.stereotype.Service;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.User;
import com.yc.service.IUserService;
import feign.hystrix.FallbackFactory;

@Service("iUserService")
public class IUserServiceImpl implements IUserService, FallbackFactory<IUserService> {
	@Override
    public ServerResponse<User> findByUsername(String username) {
//        log.info("调用{}失败","findByUsername");
        return ServerResponse.createErrorMessage("调用findByUsername接口失败");
    }

	@Override
	public IUserService create(Throwable cause) {
		cause.printStackTrace();
		// TODO Auto-generated method stub
		return this;
	}
}
