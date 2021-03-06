package com.hrm.system.service.impl;

import antlr.StringUtils;
import cn.hutool.core.util.StrUtil;
import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;
import com.hrm.common.utils.PermissionConstants;
import com.hrm.domain.system.Permission;
import com.hrm.domain.system.Role;
import com.hrm.domain.system.User;
import com.hrm.system.dao.*;
import com.hrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/9-1:07
 */
@Service
public class RoleServiceImpl extends BaseService<Role> implements RoleService {
    private IdWorker idWorker;
    private RoleDao roleDao;
    private PermissionDao permissionDao;

    @Autowired
    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void save(Role role) {
        String id = idWorker.nextId() + "";
        role.setId(id);
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        final Role role1 = roleDao.findById(role.getId()).get();
        role1.setName(role.getName());
        role1.setDescription(role.getDescription());
        roleDao.save(role1);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id).get();
    }

    @Override
    public Page<Role> findSearch(Map<String, Object> map) {
        String page = String.valueOf(map.get("page"));
        String size = String.valueOf(map.get("size"));
        Specification<Role> specification = (root, criteriaQuery, criteriaBuilder) ->
                StrUtil.isNotEmpty((CharSequence) map.get("name")) ?
                        criteriaBuilder.like(root.get("name").as(String.class), "%" + map.get("name") + "%")
                        : null;
        return roleDao.findAll(specification.and(getSpec(String.valueOf(map.get("companyId")))),
                PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size)));
    }

    @Override
    public List<Role> findAll(Map<String, Object> map) {
        return roleDao.findAll(getSpec(String.valueOf(map.get("companyId"))));
    }

    @Override
    public void deleteById(String id) {
        roleDao.deleteById(id);
    }

    @Override
    public void assignPerms(String id, List<String> permissions) {
        Role role = roleDao.findById(id).get();
        Set<Permission> perms = new HashSet<>();
        permissions.forEach(permId -> {
            // ???????????????????????????api??????????????????????????????
            Permission permission = permissionDao.findById(permId).get();
            //????????????????????????????????????????????????????????????????????????????????????
            // ?????????????????????API????????????????????????????????????????????????????????????
            final List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PY_API, permission.getId());
            //??????api??????
            perms.addAll(apiList);
            //??????????????????,????????????????????????????????????
            perms.add(permission);
        });
        role.setPermissions(perms);
        roleDao.save(role);
    }
}
