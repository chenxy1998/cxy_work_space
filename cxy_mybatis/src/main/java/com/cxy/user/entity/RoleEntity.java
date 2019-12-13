package com.cxy.user.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("RoleEntity")
public class RoleEntity {

    private Integer id;
    private String roleName;

}
