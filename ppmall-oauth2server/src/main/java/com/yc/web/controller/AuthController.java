package com.yc.web.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.response.ServerResponse;

import java.security.Principal;

@RestController
public class AuthController {
    @RequestMapping("/user")
    public ServerResponse<Principal> auth(Principal user) {
        return ServerResponse.createSuccess(user);
    }
}
