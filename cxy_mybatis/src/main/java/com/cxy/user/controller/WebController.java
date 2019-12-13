package com.cxy.user.controller;

import com.cxy.utils.UnauthorizedException;
import com.cxy.user.entity.UserEntity;
import com.cxy.user.service.UserService;
import com.cxy.utils.JWTUtil;
import com.cxy.user.entity.ResponseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("web")
public class WebController {

    private static final Logger LOGGER = LogManager.getLogger(WebController.class);

    private UserService userService;

    @Autowired
    public void setService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam("userName") String userName,
                                @RequestParam("passWord") String passWord) {
        UserEntity userEntity = userService.getByUserName(userName);
        if (userEntity.getPassWord().equals(passWord)) {
            return new ResponseEntity(200, "登陆成功", JWTUtil.sign(userName, passWord));
        } else {
            throw new UnauthorizedException();
        }
    }

    @GetMapping("/article")
    public ResponseEntity article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ResponseEntity(200, "您已经登录了", null);
        } else {
            return new ResponseEntity(200, "请登录", null);
        }
    }

    @GetMapping("/require_auth")
    @RequiresAuthentication
    public ResponseEntity requireAuth() {
        return new ResponseEntity(200, "经过验证", null);
    }

    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public ResponseEntity requireRole() {
        return new ResponseEntity(200, "您正在访问角色", null);
    }

    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public ResponseEntity requirePermission() {
        return new ResponseEntity(200, "您正在访问权限", null);
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity unauthorized() {
        return new ResponseEntity(401, "未授权", null);
    }
}
