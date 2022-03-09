package com.hrm.system.service.impl;

import antlr.StringUtils;
import cn.hutool.core.util.StrUtil;
import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;
import com.hrm.domain.system.Role;
import com.hrm.system.dao.RoleDao;
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
import java.util.Date;
import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/9-1:07
 */
@Service
public class RoleServiceImpl extends BaseService<Role> implements RoleService {
    private IdWorker idWorker;
    private RoleDao roleDao;
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
                        criteriaBuilder.like(root.get("name").as(String.class), "%"+map.get("name")+"%")
                        : null;
        return roleDao.findAll( specification.and(getSpec(String.valueOf(map.get("companyId")))),
                    PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size)));
    }

    @Override
    public void deleteById(String id) {
        roleDao.deleteById(id);
    }
}
