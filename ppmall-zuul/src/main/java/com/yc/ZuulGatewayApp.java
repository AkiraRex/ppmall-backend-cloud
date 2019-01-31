package com.yc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient// 注册到服务治理组件
@EnableZuulProxy // 路由功能
public class ZuulGatewayApp {

	public static void main(String[] args) {
		SpringApplication.run(ZuulGatewayApp.class, args);
	}
}
