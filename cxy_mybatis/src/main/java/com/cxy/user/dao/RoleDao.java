package com.cxy.user.dao;

import com.cxy.user.entity.RoleEntity;
import com.cxy.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface RoleDao {

    Set<String> getRoles(String userName);

}
