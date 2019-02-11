package com.yc.service;

import java.util.List;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.Role;
import com.yc.pojo.RoleUrl;

public interface IRoleService {
	ServerResponse<List<RoleUrl>> findRoleUrlByRoleid(Integer roleid);
	
	ServerResponse<List<Role>> findRoleByUserid(Integer userid);
}
