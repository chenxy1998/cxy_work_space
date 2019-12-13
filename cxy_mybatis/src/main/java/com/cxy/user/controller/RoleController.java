package com.cxy.user.controller;

import com.cxy.user.entity.UserEntity;
import com.cxy.user.service.RoleService;
import com.cxy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;


}
