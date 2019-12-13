package com.cxy.user.service;

import java.util.Set;

public interface PermissionService {

    Set<String> getPermissions(String username);
}
