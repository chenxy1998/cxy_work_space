package com.cxy.user.dao;

import com.cxy.user.entity.PermissionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface PermissionDao {

    Set<String> getPermissions(String userName);

    PermissionEntity queryPermissionName(String userName);
}
