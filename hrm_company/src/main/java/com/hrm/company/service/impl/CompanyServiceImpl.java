package com.hrm.company.service.impl;

import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.IdWorker;
import com.hrm.company.dao.CompanyDao;
import com.hrm.company.service.CompanyService;
import com.hrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author LZL
 * @Date 2022/1/12-10:17
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyDao companyDao;
    private IdWorker idWorker;

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }


    @Override
    public Result add(Company company) {
        //设置id
        String id= idWorker.nextId()+"";
        company.setId(id);
        //设置默认状态
        company.setAuditState("0");//审核状态 1-已审核 0-未审核
        company.setState(1);//公司状态 1-可用 0-不可用
        //保存
        companyDao.save(company);
        return Result.SUCCESS();
    }

    @Override
    public Result delete(String id) {
        companyDao.deleteById(id);
        return Result.SUCCESS();
    }

    @Override
    public Result update(Company company) {
        Company temp=companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setBalance(company.getBalance());
        companyDao.save(temp);
        return Result.SUCCESS();
    }

    @Override
    public Result<Company> findById(String id) {
        final Company company = companyDao.findById(id).get();
        Result<Company> result = Result.SUCCESS();
        result.setData(company);
        return result;
    }

    @Override
    public Result<List<Company>> findAll() {
        final List<Company> all = companyDao.findAll();
        Result<List<Company>> result = Result.SUCCESS();
        result.setData(all);
        return result;
    }


}
