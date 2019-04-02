package com.system.started.shiro.session.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.system.started.shiro.session.SessionStatus;

@Component("shiroSessionDao")
public class ShiroSessionRedisDAO extends AbstractSessionDAO {
	private static final Logger logger = LoggerFactory.getLogger(ShiroSessionRedisDAO.class);

	public static final String REDIS_SHIRO_SESSION = "oss-shiro-session:";
    public static final String REDIS_SHIRO_SESSION_ALL_REG = "*oss-shiro-session:*";
	public static final String REDIS_SHIRO_SESSION_STATUS ="oss-online-status";
	public static final String REDIS_SHIRO_SESSION_ONLINE_STATUS = "oss-shiro-session-online-status";

	@Autowired
	private RedisTemplate<String,Session> redisTemplate;

	@Override
	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
	}

	@Override
	public void delete(Session session) {
		if (session == null) {
			logger.error("Session 不能为null");
			return;
		}
		Serializable sessionId = session.getId();
		if (sessionId != null)
			redisTemplate.delete(buildRedisSessionKey(sessionId));
	}
	
	public void delete(Serializable sessionId) {
		redisTemplate.delete(buildRedisSessionKey(sessionId));
	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();
		Optional.ofNullable(redisTemplate.keys(REDIS_SHIRO_SESSION_ALL_REG))
				.orElse(new HashSet<String>())
				.forEach(t -> sessions.add(redisTemplate.opsForValue().get(t)));
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.saveSession(session);

		return sessionId;
	}
	
	private void saveSession(Session session) {
		try {
			String key = buildRedisSessionKey(Optional.of(session).map(t -> t.getId()).get());

			SessionStatus sessionStatus = (SessionStatus) Optional.ofNullable(session).map(t -> t.getAttribute(REDIS_SHIRO_SESSION_STATUS)).orElse(new SessionStatus());
			session.setAttribute(REDIS_SHIRO_SESSION_STATUS, sessionStatus);

			redisTemplate.opsForValue().set(key, session, (int) (session.getTimeout() / 1000), TimeUnit.MINUTES);
		} catch (Exception e) {
			logger.error("save session error，id:" + session.getId(), e);
		}
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		return redisTemplate.opsForValue().get(buildRedisSessionKey(sessionId));
	}
	
	public Session getSession(Serializable sessionId) {
		return redisTemplate.opsForValue().get(sessionId);
	}

	private String buildRedisSessionKey(Serializable sessionId) {
		return REDIS_SHIRO_SESSION + sessionId;
	}
}
