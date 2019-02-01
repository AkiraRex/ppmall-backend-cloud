package com.yc.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yc.pojo.Role;
import com.yc.pojo.RoleUrl;
import com.yc.response.ResponseCode;
import com.yc.response.ServerResponse;
import com.yc.service.IRoleService;
import com.yc.service.IUserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IRoleService iRoleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ServerResponse<com.yc.pojo.User> userResult = iUserService.findByUsername(username);
		if (userResult.getStatus() != ResponseCode.SUCCESS.getCode()) {
			throw new UsernameNotFoundException("用户:" + username + ",不存在!");
		}
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		boolean enabled = true; // 可用性 :true:可用 false:不可用
		boolean accountNonExpired = true; // 过期性 :true:没过期 false:过期
		boolean credentialsNonExpired = true; // 有效性 :true:凭证有效 false:凭证无效
		boolean accountNonLocked = true; // 锁定性 :true:未锁定 false:已锁定
		com.yc.pojo.User user = new com.yc.pojo.User();
		BeanUtils.copyProperties(userResult.getData(), user);

		ServerResponse<List<Role>> roleResult = iRoleService.findRoleByUserid(user.getId());
		if (roleResult.getStatus() == ResponseCode.SUCCESS.getCode()) {
			List<Role> roleList = roleResult.getData();
			for (Role role : roleList) {
				// 角色必须是ROLE_开头，可以在数据库中设置
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleDesc());
				grantedAuthorities.add(grantedAuthority);
				// 获取权限
				ServerResponse<List<RoleUrl>> perResult = iRoleService.findRoleUrlByRoleid(role.getId());
				if (perResult.getStatus() == ResponseCode.SUCCESS.getCode()) {
					List<RoleUrl> permissionList = perResult.getData();
					for (RoleUrl roleUrl : permissionList) {
						GrantedAuthority authority = new SimpleGrantedAuthority(roleUrl.getPermissionUrl());
						grantedAuthorities.add(authority);
					}
				}
			}
		}

		User detailUser = new User(user.getUsername(), user.getPassword(), enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, grantedAuthorities);

		return detailUser;
	}

}
