package com.hrm.system.service.impl;

import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;

import com.hrm.domain.system.User;
import com.hrm.system.dao.UserDao;
import com.hrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/8-20:34
 */
@Service
public class UserServiceImpl  implements UserService {
    public static final String  COMPANY_ID="companyId";
    public static final String  DEPARTMENT_ID="departmentId";
    public static final String  HAS_DEPT="hasDept";
    private IdWorker idWorker;

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Override
    public void save(User user) {
        String id = idWorker.nextId() + "";
        user.setId(id);
        user.setCreateTime(new Date());
        user.setEnableState(1);
        user.setPassword("123456");
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        User user1 = userDao.findById(user.getId()).get();
        user1.setPassword(user.getPassword());
        user1.setMobile(user.getMobile());
        user1.setUsername(user.getUsername());
        user1.setWorkingCity(user.getWorkingCity());
        user1.setInServiceStatus(user.getInServiceStatus());
        user1.setDepartmentId(user.getDepartmentId());
        user1.setDepartmentName(user.getDepartmentName());
        user1.setCorrectionTime(user.getCorrectionTime());
        userDao.save(user1);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    @Override
    public Page<User> findAll(Map<String, Object> map) {
        String page = String.valueOf(map.get("page"));
        String size = String.valueOf(map.get("size"));
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>(3);
            map.forEach((k, v) -> {
                if (COMPANY_ID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), v));
                }
                if (DEPARTMENT_ID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), v));
                }
                if (HAS_DEPT.equals(k)) {
                    if ("0".equals(v)) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
            });
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        };
        return userDao.findAll(specification, PageRequest.of(Integer.parseInt(page)-1, Integer.parseInt(size)));
    }

    @Override
    public void deleteById(String id) {
        userDao.deleteById(id);
    }
}
