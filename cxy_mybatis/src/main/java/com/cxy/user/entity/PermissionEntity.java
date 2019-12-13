package com.cxy.user.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("PermissionEntity")
public class PermissionEntity {

    private Integer id;
    private String permissionName;
    private String roleId;

}
