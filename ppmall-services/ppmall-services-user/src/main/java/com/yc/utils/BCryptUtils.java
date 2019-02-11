package com.yc.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtils {

	private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String encode(String password) {

		return passwordEncoder.encode(password);
	}
}
