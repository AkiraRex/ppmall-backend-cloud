package com.yc.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.yc.errorhandler.CustomerWebResponseExceptionTranslator;
import com.yc.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource dataSource;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	RedisTokenStore redisTokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	// token存储数据库
	// @Bean
	// public JdbcTokenStore jdbcTokenStore(){
	// return new JdbcTokenStore(dataSource);
	// }

	/**
	 * 用来配置客户端详情服务（ClientDetailsService），
	 * 客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
	 * clientId：（必须的）用来标识客户的Id。 secret：（需要值得信任的客户端）客户端安全码，如果有的话。
	 * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
	 * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。 authorities：此客户端可以使用的权限（基于Spring
	 * Security authorities）
	 * 
	 * 
	 * 客户端详情（Client
	 * Details）能够在应用程序运行的时候进行更新，可以通过访问底层的存储服务（例如将客户端详情存储在一个关系数据库的表中，就可以使用
	 * JdbcClientDetailsService） 或者通过 ClientDetailsManager 接口 （同时你也可以实现
	 * ClientDetailsService 接口）来进行管理。
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetails());
	}

	/**
	 * 固定表明字段oauth_client_details
	 * 
	 * @return
	 */
	@Bean
	public ClientDetailsService clientDetails() {
		return new JdbcClientDetailsService(dataSource);
	}

	@Bean  
	public WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator() {
		return new CustomerWebResponseExceptionTranslator();
	}

	/**
	 * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(redisTokenStore()).userDetailsService(userDetailsService)
				.authenticationManager(authenticationManager);
		endpoints.tokenServices(defaultTokenServices());
		endpoints.exceptionTranslator(webResponseExceptionTranslator());// 认证异常翻译
	}

	/**
	 * <p>
	 * 注意，自定义TokenServices的时候，需要设置@Primary，否则报错，
	 * </p>
	 * 
	 * @return
	 */
	@Primary
	@Bean
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(redisTokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setClientDetailsService(clientDetails());
		tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12); // token有效期自定义设置，默认12小时
		tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);// 默认30天，这里修改
		return tokenServices;
	}

	/**
	 * 用来配置令牌端点(Token Endpoint)的安全约束.
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()");
		security.checkTokenAccess("isAuthenticated()");
		security.allowFormAuthenticationForClients();
	}
}
