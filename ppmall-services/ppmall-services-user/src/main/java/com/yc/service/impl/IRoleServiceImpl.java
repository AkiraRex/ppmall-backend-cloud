package com.yc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yc.common.response.ServerResponse;
import com.yc.dao.RoleMapper;
import com.yc.dao.RoleUrlMapper;
import com.yc.pojo.Role;
import com.yc.pojo.RoleUrl;
import com.yc.service.IRoleService;

@Service("iRoleService")
public class IRoleServiceImpl implements IRoleService {
	
	@Autowired
	private RoleUrlMapper roleUrlMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public ServerResponse<List<RoleUrl>> findRoleUrlByRoleid(Integer roleid) {
		// TODO Auto-generated method stub
		List<RoleUrl> roleUrls = roleUrlMapper.selectByRoleid(roleid);
		return ServerResponse.createSuccess(roleUrls);
	}
	
	@Override
	public ServerResponse<List<Role>> findRoleByUserid(Integer userid) {
		// TODO Auto-generated method stub
		List<Role> roles = roleMapper.selectByUserid(userid);
		return ServerResponse.createSuccess(roles);
	}

}
