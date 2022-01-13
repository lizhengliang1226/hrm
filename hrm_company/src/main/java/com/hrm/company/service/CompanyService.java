package com.hrm.company.service;

import com.hrm.common.entity.Result;
import com.hrm.domain.company.Company;

import java.util.List;

/**
 * @Description 企业服务
 * @Author LZL
 * @Date 2022/1/12-10:17
 */
public interface CompanyService {
    /**
     * 保存企业
     */
    public Result add(Company company);
    /**
     * 删除企业
     */
    public Result delete(String id);
    /**
     * 更新企业
     */
    public Result update(Company company);
    /**
     * 根据id查询企业
     */
    public Result<Company> findById(String id);
    /**
     * 查询企业列表
     */
    public Result<List<Company>> findAll();
}
