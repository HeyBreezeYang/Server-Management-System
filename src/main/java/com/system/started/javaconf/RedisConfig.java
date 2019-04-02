package com.system.started.javaconf;

import java.net.UnknownHostException;

import org.apache.shiro.session.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Session> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
		RedisTemplate<String, Session> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}
}
