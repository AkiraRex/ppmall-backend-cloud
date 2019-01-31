package com.yc.web.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthController {
    @RequestMapping("/user")
    public Principal auth(Principal user) {
        return user;
    }
}
