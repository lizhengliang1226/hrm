package com.hrm.company;

import com.hrm.company.dao.CompanyDao;
import com.hrm.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description
 * @Author LZL
 * @Date 2022/1/12-10:03
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyDaoTest {
    @Autowired
    private CompanyDao companyDao;
    @Test
    public void test(){
        final Company company = companyDao.findById("12345").get();
        System.out.println(company.toString());
    }
}
