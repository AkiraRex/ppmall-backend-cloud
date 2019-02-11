package com.yc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.Role;
import com.yc.pojo.RoleUrl;
import com.yc.service.IRoleService;
import feign.hystrix.FallbackFactory;

@Service("iRoleService")
public class IRoleServiceImpl implements IRoleService, FallbackFactory<IRoleService> {
	
	@Override
	public ServerResponse<List<RoleUrl>> findRoleUrlByRoleid(Integer roleid) {
		// TODO Auto-generated method stub
		return ServerResponse.createErrorMessage("调用findRoleUrlByRoleid接口失败");
	}

	@Override
	public ServerResponse<List<Role>> findRoleByUserid(Integer userid) {
		// TODO Auto-generated method stub
		return ServerResponse.createErrorMessage("调用findRoleByUserid接口失败");
	}

	@Override
	public IRoleService create(Throwable cause) {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		return this;
	}
	

}
