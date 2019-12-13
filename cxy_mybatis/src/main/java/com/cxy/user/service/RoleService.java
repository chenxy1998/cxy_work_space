package com.cxy.user.service;

import com.cxy.user.entity.RoleEntity;

import java.util.Set;

public interface RoleService {

    Set<String> getRoles(String username);

    RoleEntity queryRoleName(String userName);
}
