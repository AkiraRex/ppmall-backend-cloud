package com.yc.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yc.response.ResponseCode;
import com.yc.response.ServerResponse;
import com.yc.service.IUserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    private IUserService iUserService;


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
        BeanUtils.copyProperties(userResult.getData(),user);
       
        User detailUser = new User(user.getUsername(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        
        return detailUser;
    }

}
