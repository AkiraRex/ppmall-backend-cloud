package com.yc.service;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface PermissionService {
	boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
