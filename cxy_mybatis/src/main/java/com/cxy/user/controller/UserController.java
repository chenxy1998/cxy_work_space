package com.cxy.user.controller;

import com.cxy.user.entity.UserEntity;
import com.cxy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("queryUser")
    public UserEntity queryUser(UserEntity userEntity){
        return userService.queryUser(userEntity);
    }
}
