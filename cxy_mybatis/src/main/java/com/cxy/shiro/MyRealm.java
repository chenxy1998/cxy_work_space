package com.cxy.shiro;

import com.cxy.user.entity.RoleEntity;
import com.cxy.user.entity.UserEntity;
import com.cxy.user.service.PermissionService;
import com.cxy.user.service.RoleService;
import com.cxy.user.service.UserService;
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

import javax.annotation.Resource;
import java.security.Permissions;

/**
 * @Description :  自定以 reamle
 * @param
 * @return
 * @throws
 * @author chenxy
 * @date 2019/12/12 14:29
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * @Description : 为当前登录成功的用户授予权限和分配角色
     * @param
     * @param principalCollection
     * @return org.apache.shiro.authz.AuthorizationInfo
     * @throws
     * @author chenxy
     * @date 2019/12/12 14:58
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 给该用户设置角色，角色信息存在 t_role 表中取
        authorizationInfo.setRoles(roleService.getRoles(userName));
        // 给该用户设置权限，权限信息存在 t_permission 表中取
        authorizationInfo.setStringPermissions(permissionService.getPermissions(userName));
        return authorizationInfo;
    }


    /**
     * @Description : 用来验证当前登录的用户，获取认证信息。
     * @param
     * @param authenticationToken
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @throws
     * @author chenxy
     * @date 2019/12/12 14:58
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 根据 Token 获取用户名，如果您不知道该 Token 怎么来的，先可以不管，下文会解释
        String userName = (String) authenticationToken.getPrincipal();
        // 根据用户名从数据库中查询该用户
        UserEntity userEntity = userService.getByUserName(userName);
        if(userEntity != null) {
            // 把当前用户存到 Session 中
            SecurityUtils.getSubject().getSession().setAttribute("userEntity", userEntity);
            // 传入用户名和密码进行身份认证，并返回认证信息
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userEntity.getUserName(), userEntity.getPassWord(), "myRealm");
            return authcInfo;
        } else {
            return null;
        }
    }

}
