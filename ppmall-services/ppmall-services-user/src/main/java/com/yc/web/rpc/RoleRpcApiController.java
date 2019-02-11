package com.yc.web.rpc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.common.response.ServerResponse;
import com.yc.pojo.Role;
import com.yc.pojo.RoleUrl;
import com.yc.service.IRoleService;

@RestController
@RequestMapping("/rpc/role")
public class RoleRpcApiController {
	
	@Autowired
	private IRoleService iRoleService;
	
	@GetMapping("/findRoleUrlByRoleid/{roleid}")
	public ServerResponse<List<RoleUrl>> findRoleUrlByRoleid(@PathVariable("roleid") Integer roleid){
		return iRoleService.findRoleUrlByRoleid(roleid);
	}
	
	@GetMapping("/findRoleByUserid/{userid}")
	public ServerResponse<List<Role>> findRoleByUserid(@PathVariable("userid") Integer userid){
		return iRoleService.findRoleByUserid(userid);
	}
}
