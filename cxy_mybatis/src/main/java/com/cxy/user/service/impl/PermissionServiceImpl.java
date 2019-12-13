package com.cxy.user.service.impl;

import com.cxy.user.dao.PermissionDao;
import com.cxy.user.dao.RoleDao;
import com.cxy.user.service.PermissionService;
import com.cxy.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("PermissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;


    @Override
    public Set<String> getPermissions(String userName) {
        return permissionDao.getPermissions(userName);
    }
}
