package com.cxy.user.service.impl;

import com.cxy.user.config.RedisService;
import com.cxy.user.dao.RoleDao;
import com.cxy.user.dao.UserDao;
import com.cxy.user.entity.RoleEntity;
import com.cxy.user.entity.UserEntity;
import com.cxy.user.service.RoleService;
import com.cxy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;


    @Override
    public Set<String> getRoles(String userName) {
        return roleDao.getRoles(userName);
    }
}
