package com.cxy.user.service.impl;

import com.cxy.user.config.RedisService;
import com.cxy.user.dao.UserDao;
import com.cxy.user.entity.UserEntity;
import com.cxy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Override
    public UserEntity queryUser(UserEntity userEntity) {
        UserEntity userEntitys =  userDao.queryUser(userEntity);
        redisService.set("bbb", userEntitys.getId(),30L);
        System.out.println(redisService.get("bbb"));
        return userEntitys;
    }

    @Override
    public UserEntity getByUserName(String userName) {
        return userDao.getByUserName(userName);
    }
}
