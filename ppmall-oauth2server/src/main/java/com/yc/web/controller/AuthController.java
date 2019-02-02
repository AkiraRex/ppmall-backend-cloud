package com.yc.web.controller;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.common.response.ServerResponse;

@RestController
public class AuthController {
	
    @RequestMapping("/user")
    public Principal auth(Principal user) {
        return user;
    }
    
    @GetMapping("/principal")
    public ServerResponse<Map<String, Object>> principal(Principal principal) {
    	Map<String, Object> map = new HashMap<>();
    	map.put("name", principal.getName());
        return ServerResponse.createSuccess(map);
    }
 
    
}
