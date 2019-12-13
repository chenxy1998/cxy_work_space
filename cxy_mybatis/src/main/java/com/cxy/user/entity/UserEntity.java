package com.cxy.user.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("UserEntity")
public class UserEntity {

    private Integer id;
    private String userName;
    private String passWord;
    private Integer roleId;

}
