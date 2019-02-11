package com.yc.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class BCryptUtils {

	private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String encode(String password) {

		return passwordEncoder.encode(password);
	}
	
	public static boolean checkPassword(String plaintext, String hashed) {
		return BCrypt.checkpw(plaintext, hashed);
	}
}
