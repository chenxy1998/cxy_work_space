package com.cxy.user.service;

import com.cxy.user.entity.PermissionEntity;

import java.util.Set;

public interface PermissionService {

    Set<String> getPermissions(String username);

    PermissionEntity queryPermissionName(String userName);
}
