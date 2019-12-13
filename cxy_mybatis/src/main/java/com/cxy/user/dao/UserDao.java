package com.cxy.user.dao;

import com.cxy.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    UserEntity queryUser(UserEntity userEntity);

    UserEntity getByUserName(String userName);
}
