package com.cxy.shiro;

import com.cxy.user.entity.PermissionEntity;
import com.cxy.user.entity.RoleEntity;
import com.cxy.user.entity.UserEntity;
import com.cxy.user.service.PermissionService;
import com.cxy.user.service.RoleService;
import com.cxy.user.service.UserService;
import com.cxy.utils.JWTToken;
import com.cxy.utils.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Permissions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description :  自定以 reamle
 * @param
 * @return
 * @throws
 * @author chenxy
 * @date 2019/12/12 14:29
 */
@Service
public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = JWTUtil.getUsername(principals.toString());
        RoleEntity roleEntity = roleService.queryRoleName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(roleEntity.getRoleName());
        PermissionEntity permissionEntity = permissionService.queryPermissionName(userName);
        Set<String> permission = new HashSet<>(Arrays.asList(permissionEntity.getPermissionName().split(",")));
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token失效");
        }

        //UserBean userBean = userService.getUser(username);
        UserEntity userEntity = userService.getByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationException("用户不存在!");
        }

        if (! JWTUtil.verify(token, username, userEntity.getPassWord())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        return new SimpleAuthenticationInfo(token, token, "通过");
    }

}
