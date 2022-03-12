package com.hrm.domain.system.response;

import com.hrm.domain.system.Permission;
import com.hrm.domain.system.Role;
import com.hrm.domain.system.User;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @Description 登录成功返回结果
 * @Author LZL
 * @Date 2022/3/11-9:43
 */
@Setter
@Getter
public class ProfileResult {
    private String mobile;
    private String username;
    private String company;
    private Map<String, Object> roles = new HashMap<>();

    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        final Set<Role> userRoles = user.getRoles();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        userRoles.forEach(r -> {
            final Set<Permission> permissionSet = r.getPermissions();
            permissionSet.forEach(p -> {
                if (p.getType() == 1) {
                    menus.add(p.getCode());
                } else if (p.getType() == 2) {
                    points.add(p.getCode());
                } else {
                    apis.add(p.getCode());
                }
            });
        });
        this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);
    }
    public ProfileResult(User user, List<Permission> permissionList){
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        permissionList.forEach(p -> {
            if (p.getType() == 1) {
                menus.add(p.getCode());
            } else if (p.getType() == 2) {
                points.add(p.getCode());
            } else {
                apis.add(p.getCode());
            }
        });
        this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);
    }
}
