package com.yc.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.Role;
import com.yc.pojo.RoleUrl;
import com.yc.service.impl.IRoleServiceImpl;

@FeignClient(name = "ppmall-service-user", fallbackFactory = IRoleServiceImpl.class)
public interface IRoleService {
	@GetMapping("/rpc/role/findRoleUrlByRoleid/{roleid}")
	ServerResponse<List<RoleUrl>> findRoleUrlByRoleid(@PathVariable("roleid") Integer roleid);
	
	@GetMapping("/rpc/role/findRoleByUserid/{userid}")
	ServerResponse<List<Role>> findRoleByUserid(@PathVariable("userid") Integer userid);
}
