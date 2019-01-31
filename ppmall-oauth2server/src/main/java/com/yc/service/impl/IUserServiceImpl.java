package com.yc.service.impl;
import org.springframework.stereotype.Service;

import com.yc.pojo.User;
import com.yc.response.ServerResponse;
import com.yc.service.IUserService;

@Service("iUserService")
public class IUserServiceImpl implements IUserService{
	@Override
    public ServerResponse<User> findByUsername(String username) {
//        log.info("调用{}失败","findByUsername");
        return ServerResponse.createErrorMessage("调用findByUsername接口失败");
    }
}
