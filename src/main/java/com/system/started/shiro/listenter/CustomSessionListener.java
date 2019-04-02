package com.system.started.shiro.listenter;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.started.shiro.session.dao.ShiroSessionRedisDAO;

/**
 * shiro 回话 监听
 */
@Component
public class CustomSessionListener implements SessionListener {

	@Autowired
	private ShiroSessionRedisDAO shiroSessionDao;
    /**
     * 一个回话的生命周期开始
     */
    @Override
    public void onStart(Session session) {
        //TODO
        System.out.println("on start");
    }
    /**
     * 一个回话的生命周期结束
     */
    @Override
    public void onStop(Session session) {
        //TODO
        System.out.println("on stop");
    }

    @Override
    public void onExpiration(Session session) {
        System.out.println("onExpiration");
        shiroSessionDao.delete(session);
    }
}

